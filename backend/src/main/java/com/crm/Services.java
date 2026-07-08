package com.crm;

import jakarta.persistence.criteria.Predicate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
class AuthService {
    @Autowired
    private UserRepository userRepository;

    LoginResponse login(LoginRequest req) {
        LoginResponse resp = new LoginResponse();
        var userOpt = userRepository.findByUsername(req.getUsername());
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(req.getPassword())) {
            resp.setSuccess(true);
            resp.setMessage("Login successful");
            resp.setUsername(req.getUsername());
        } else {
            resp.setSuccess(false);
            resp.setMessage("Invalid username or password");
        }
        return resp;
    }
}

@Service
class LeadTypeService {
    @Autowired
    private LeadTypeRepository leadTypeRepository;

    List<LeadType> findAll() {
        return leadTypeRepository.findAll();
    }

    LeadType create(LeadTypeRequest req) {
        LeadType lt = new LeadType();
        lt.setName(req.getName());
        lt.setDescription(req.getDescription());
        return leadTypeRepository.save(lt);
    }

    LeadType update(Long id, LeadTypeRequest req) {
        LeadType lt = leadTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead type not found: " + id));
        lt.setName(req.getName());
        lt.setDescription(req.getDescription());
        return leadTypeRepository.save(lt);
    }

    void delete(Long id) {
        if (!leadTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lead type not found: " + id);
        }
        leadTypeRepository.deleteById(id);
    }
}

@Service
class CustomerLeadService {
	@Autowired
    private CustomerLeadRepository leadRepository;

    // 1. EXPORT TO EXCEL
    public ByteArrayInputStream exportLeadsToExcel() throws IOException {
        List<CustomerLead> leads = leadRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Leads");

            // Row Header
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Name", "Email", "Phone", "Status"};
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
            }

            // Data Rows
            int rowIdx = 1;
            for (CustomerLead lead : leads) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(lead.getId() != null ? lead.getId() : 0);
                row.createCell(1).setCellValue(lead.getCustomerName());
                row.createCell(2).setCellValue(lead.getEmail());
                row.createCell(3).setCellValue(lead.getMobile());
                row.createCell(4).setCellValue(lead.getStatus());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
    
 // 2. IMPORT FROM EXCEL
    public void importLeadsFromExcel(MultipartFile file) throws IOException {
        List<CustomerLead> leads = new ArrayList<>();
        
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) rows.next();

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                CustomerLead lead = new CustomerLead();

                // Assuming columns: Name (0), Email (1), Phone (2), Status (3)
                lead.setCustomerName(currentRow.getCell(0).getStringCellValue());
                lead.setEmail(currentRow.getCell(1).getStringCellValue());
                lead.setMobile(currentRow.getCell(2).getStringCellValue());
                lead.setStatus(currentRow.getCell(3).getStringCellValue());

                leads.add(lead);
            }
            leadRepository.saveAll(leads);
        }
    }

    @Autowired
    private CustomerLeadRepository customerLeadRepository;
    @Autowired
    private LeadTypeRepository leadTypeRepository;

    List<CustomerLead> findAll() {
        return customerLeadRepository.findAll();
    }

    CustomerLead findById(Long id) {
        return customerLeadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + id));
    }

    CustomerLead create(CustomerLeadRequest req) {
        CustomerLead c = new CustomerLead();
        applyRequest(c, req);
        return customerLeadRepository.save(c);
    }

    CustomerLead update(Long id, CustomerLeadRequest req) {
        CustomerLead c = findById(id);
        applyRequest(c, req);
        return customerLeadRepository.save(c);
    }

    void delete(Long id) {
        if (!customerLeadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lead not found: " + id);
        }
        customerLeadRepository.deleteById(id);
    }

    private void applyRequest(CustomerLead c, CustomerLeadRequest req) {
        c.setCustomerName(req.getCustomerName());
        c.setMobile(req.getMobile());
        c.setAlternateNumber(req.getAlternateNumber());
        c.setEmail(req.getEmail());
        if (req.getLeadTypeId() != null) {
            LeadType lt = leadTypeRepository.findById(req.getLeadTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead type not found: " + req.getLeadTypeId()));
            c.setLeadType(lt);
        }
        c.setCity(req.getCity());
        c.setAddress(req.getAddress());
        c.setRequirement(req.getRequirement());
        c.setLeadSource(req.getLeadSource());
        c.setAssignedExecutive(req.getAssignedExecutive());
        c.setDiscussionDetails(req.getDiscussionDetails());
        c.setVisitDate(req.getVisitDate());
        c.setNextFollowupDate(req.getNextFollowupDate());
        if (req.getStatus() != null) c.setStatus(req.getStatus());
        c.setPriority(req.getPriority());
    }

    // Dynamic search/filter: lead type, status, priority, city, date range, name, mobile
    List<CustomerLead> search(LeadSearchCriteria f) {
        Specification<CustomerLead> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (f.getName() != null && !f.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("customerName")), "%" + f.getName().toLowerCase() + "%"));
            }
            if (f.getMobile() != null && !f.getMobile().isBlank()) {
                predicates.add(cb.like(root.get("mobile"), "%" + f.getMobile() + "%"));
            }
            if (f.getLeadTypeId() != null) {
                predicates.add(cb.equal(root.get("leadType").get("id"), f.getLeadTypeId()));
            }
            if (f.getStatus() != null && !f.getStatus().isBlank()) {
                predicates.add(cb.equal(root.get("status"), f.getStatus()));
            }
            if (f.getPriority() != null && !f.getPriority().isBlank()) {
                predicates.add(cb.equal(root.get("priority"), f.getPriority()));
            }
            if (f.getCity() != null && !f.getCity().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("city")), "%" + f.getCity().toLowerCase() + "%"));
            }
            if (f.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate").as(LocalDate.class), f.getFromDate()));
            }
            if (f.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdDate").as(LocalDate.class), f.getToDate()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return customerLeadRepository.findAll(spec);
    }

    DashboardStats dashboardStats() {
        DashboardStats stats = new DashboardStats();
        LocalDate today = LocalDate.now();
        stats.setTotalLeads(customerLeadRepository.count());
        stats.setTodaysFollowups(customerLeadRepository.findByNextFollowupDate(today).size());
        stats.setPendingFollowups(customerLeadRepository.findByNextFollowupDateBefore(today).stream()
                .filter(c -> !"Closed Won".equals(c.getStatus()) && !"Closed Lost".equals(c.getStatus()))
                .count());
        stats.setHotCustomers(customerLeadRepository.countByPriority("Hot"));
        stats.setClosedDeals(customerLeadRepository.countByStatus("Closed Won"));
        return stats;
    }

    // Reminder page: today's + overdue follow-ups (not yet closed)
    List<CustomerLead> reminders() {
        LocalDate today = LocalDate.now();
        List<CustomerLead> due = new ArrayList<>();
        due.addAll(customerLeadRepository.findByNextFollowupDateBefore(today.plusDays(1)));
        due.removeIf(c -> "Closed Won".equals(c.getStatus()) || "Closed Lost".equals(c.getStatus()));
        return due;
    }
}

@Service
class FollowUpService {
    @Autowired
    private FollowUpRepository followUpRepository;
    @Autowired
    private CustomerLeadRepository customerLeadRepository;

    List<FollowUp> historyFor(Long customerLeadId) {
        return followUpRepository.findByCustomerLeadIdOrderByCreatedDateDesc(customerLeadId);
    }

    FollowUp create(FollowUpRequest req) {
        CustomerLead lead = customerLeadRepository.findById(req.getCustomerLeadId())
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + req.getCustomerLeadId()));

        FollowUp f = new FollowUp();
        f.setCustomerLead(lead);
        f.setFollowupDate(req.getFollowupDate());
        f.setRemarks(req.getRemarks());
        f.setStatusAtFollowup(req.getStatusAtFollowup());
        f.setNextFollowupDate(req.getNextFollowupDate());
        FollowUp saved = followUpRepository.save(f);

        // Keep the lead's own status/next-followup-date in sync with the latest follow-up
        if (req.getStatusAtFollowup() != null) lead.setStatus(req.getStatusAtFollowup());
        lead.setNextFollowupDate(req.getNextFollowupDate());
        customerLeadRepository.save(lead);

        return saved;
    }
}

@Service
class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private CustomerLeadRepository customerLeadRepository;

    List<Note> notesFor(Long customerLeadId) {
        return noteRepository.findByCustomerLeadIdOrderByCreatedDateDesc(customerLeadId);
    }

    Note create(NoteRequest req) {
        CustomerLead lead = customerLeadRepository.findById(req.getCustomerLeadId())
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + req.getCustomerLeadId()));
        Note n = new Note();
        n.setCustomerLead(lead);
        n.setNoteText(req.getNoteText());
        return noteRepository.save(n);
    }

    void delete(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note not found: " + id);
        }
        noteRepository.deleteById(id);
    }
}

// Simple custom exception used across services
class ResourceNotFoundException extends RuntimeException {
    ResourceNotFoundException(String message) {
        super(message);
    }
}
