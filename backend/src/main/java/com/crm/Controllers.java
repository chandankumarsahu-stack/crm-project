package com.crm;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        LoginResponse resp = authService.login(req);
        return resp.isSuccess() ? ResponseEntity.ok(resp) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }
}

@RestController
@RequestMapping("/api/lead-types")
class LeadTypeController {
    @Autowired
    private LeadTypeService leadTypeService;

    @GetMapping
    List<LeadType> all() { return leadTypeService.findAll(); }

    @PostMapping
    LeadType create(@RequestBody LeadTypeRequest req) { return leadTypeService.create(req); }

    @PutMapping("/{id}")
    LeadType update(@PathVariable Long id, @RequestBody LeadTypeRequest req) { return leadTypeService.update(id, req); }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        leadTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

@RestController
@RequestMapping("/api/leads")
class CustomerLeadController {
	@Autowired
    private CustomerLeadService leadService;

    // Endpoint to Export Excel
    @GetMapping("/download/excel")
    public ResponseEntity<Resource> downloadExcel() throws IOException {
        String filename = "leads.xlsx";
        ByteArrayInputStream data = leadService.exportLeadsToExcel();
        InputStreamResource file = new InputStreamResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

    // Endpoint to Import Excel
    @PostMapping("/upload/excel")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            leadService.importLeadsFromExcel(file);
            return ResponseEntity.ok("Leads imported successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to parse Excel file: " + e.getMessage());
        }
    }

    @Autowired
    private CustomerLeadService customerLeadService;
    @Autowired
    private FollowUpService followUpService;
    @Autowired
    private NoteService noteService;

    @GetMapping
    List<CustomerLead> all() { return customerLeadService.findAll(); }

    @GetMapping("/{id}")
    CustomerLead one(@PathVariable Long id) { return customerLeadService.findById(id); }

    @PostMapping
    CustomerLead create(@RequestBody CustomerLeadRequest req) { return customerLeadService.create(req); }

    @PutMapping("/{id}")
    CustomerLead update(@PathVariable Long id, @RequestBody CustomerLeadRequest req) { return customerLeadService.update(id, req); }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        customerLeadService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    List<CustomerLead> search(@RequestBody LeadSearchCriteria criteria) { return customerLeadService.search(criteria); }

    @GetMapping("/dashboard")
    DashboardStats dashboard() { return customerLeadService.dashboardStats(); }

    @GetMapping("/reminders")
    List<CustomerLead> reminders() { return customerLeadService.reminders(); }

    // Follow-up history for a lead, and adding a new follow-up
    @GetMapping("/{id}/followups")
    List<FollowUp> followupHistory(@PathVariable Long id) { return followUpService.historyFor(id); }

    @PostMapping("/{id}/followups")
    FollowUp addFollowup(@PathVariable Long id, @RequestBody FollowUpRequest req) {
        req.setCustomerLeadId(id);
        return followUpService.create(req);
    }

    // Notes for a lead
    @GetMapping("/{id}/notes")
    List<Note> notes(@PathVariable Long id) { return noteService.notesFor(id); }

    @PostMapping("/{id}/notes")
    Note addNote(@PathVariable Long id, @RequestBody NoteRequest req) {
        req.setCustomerLeadId(id);
        return noteService.create(req);
    }

    @DeleteMapping("/notes/{noteId}")
    ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        noteService.delete(noteId);
        return ResponseEntity.noContent().build();
    }
}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
    }
}
