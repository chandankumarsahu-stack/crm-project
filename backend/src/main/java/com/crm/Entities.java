package com.crm;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

// ---------- users ----------

@Entity
@Table(name = "users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // No-Args Constructor
    public User() {
    }

    // All-Args Constructor
    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, username, password);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        return java.util.Objects.equals(id, other.id)
                && java.util.Objects.equals(username, other.username)
                && java.util.Objects.equals(password, other.password);
    }
}

// ---------- lead_type (unlimited, user-defined lead categories) ----------
@Entity
@Table(name = "lead_type")
class LeadType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    // No-Args Constructor
    public LeadType() {
    }

    // All-Args Constructor
    public LeadType(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "LeadType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, description);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof LeadType))
            return false;
        LeadType other = (LeadType) obj;
        return java.util.Objects.equals(id, other.id)
                && java.util.Objects.equals(name, other.name)
                && java.util.Objects.equals(description, other.description);
    }
}

// ---------- customer_lead ----------
@Entity
@Table(name = "customer_lead")
class CustomerLead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String mobile;
    private String alternateNumber;
    private String email;

    @ManyToOne
    @JoinColumn(name = "lead_type_id")
    private LeadType leadType;

    private String city;
    private String address;

    @Column(length = 2000)
    private String requirement;

    private String leadSource;
    private String assignedExecutive;

    @Column(length = 2000)
    private String discussionDetails;

    private LocalDate visitDate;
    private LocalDate nextFollowupDate;

    private String status;
    private String priority;

    private LocalDateTime createdDate;

    // No-Args Constructor
    public CustomerLead() {
    }

    // All-Args Constructor
    public CustomerLead(Long id, String customerName, String mobile,
                        String alternateNumber, String email,
                        LeadType leadType, String city, String address,
                        String requirement, String leadSource,
                        String assignedExecutive, String discussionDetails,
                        LocalDate visitDate, LocalDate nextFollowupDate,
                        String status, String priority,
                        LocalDateTime createdDate) {

        this.id = id;
        this.customerName = customerName;
        this.mobile = mobile;
        this.alternateNumber = alternateNumber;
        this.email = email;
        this.leadType = leadType;
        this.city = city;
        this.address = address;
        this.requirement = requirement;
        this.leadSource = leadSource;
        this.assignedExecutive = assignedExecutive;
        this.discussionDetails = discussionDetails;
        this.visitDate = visitDate;
        this.nextFollowupDate = nextFollowupDate;
        this.status = status;
        this.priority = priority;
        this.createdDate = createdDate;
    }

    @PrePersist
    void onCreate() {
        this.createdDate = LocalDateTime.now();
        if (this.status == null) {
            this.status = "New";
        }
    }

    // ---------- Getters ----------

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public String getEmail() {
        return email;
    }

    public LeadType getLeadType() {
        return leadType;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getRequirement() {
        return requirement;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public String getAssignedExecutive() {
        return assignedExecutive;
    }

    public String getDiscussionDetails() {
        return discussionDetails;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public LocalDate getNextFollowupDate() {
        return nextFollowupDate;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    // ---------- Setters ----------

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLeadType(LeadType leadType) {
        this.leadType = leadType;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

    public void setAssignedExecutive(String assignedExecutive) {
        this.assignedExecutive = assignedExecutive;
    }

    public void setDiscussionDetails(String discussionDetails) {
        this.discussionDetails = discussionDetails;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public void setNextFollowupDate(LocalDate nextFollowupDate) {
        this.nextFollowupDate = nextFollowupDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "CustomerLead{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CustomerLead)) return false;
        CustomerLead other = (CustomerLead) obj;
        return java.util.Objects.equals(id, other.id);
    }
}

// ---------- follow_up (history of every follow-up made on a lead) ----------
@Entity
@Table(name = "follow_up")
class FollowUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_lead_id", nullable = false)
    private CustomerLead customerLead;

    private LocalDate followupDate;

    @Column(length = 2000)
    private String remarks;

    private String statusAtFollowup;

    private LocalDate nextFollowupDate;

    private LocalDateTime createdDate;

    // No-Args Constructor
    public FollowUp() {
    }

    // All-Args Constructor
    public FollowUp(Long id,
                    CustomerLead customerLead,
                    LocalDate followupDate,
                    String remarks,
                    String statusAtFollowup,
                    LocalDate nextFollowupDate,
                    LocalDateTime createdDate) {

        this.id = id;
        this.customerLead = customerLead;
        this.followupDate = followupDate;
        this.remarks = remarks;
        this.statusAtFollowup = statusAtFollowup;
        this.nextFollowupDate = nextFollowupDate;
        this.createdDate = createdDate;
    }

    @PrePersist
    void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    // Getters

    public Long getId() {
        return id;
    }

    public CustomerLead getCustomerLead() {
        return customerLead;
    }

    public LocalDate getFollowupDate() {
        return followupDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getStatusAtFollowup() {
        return statusAtFollowup;
    }

    public LocalDate getNextFollowupDate() {
        return nextFollowupDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerLead(CustomerLead customerLead) {
        this.customerLead = customerLead;
    }

    public void setFollowupDate(LocalDate followupDate) {
        this.followupDate = followupDate;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setStatusAtFollowup(String statusAtFollowup) {
        this.statusAtFollowup = statusAtFollowup;
    }

    public void setNextFollowupDate(LocalDate nextFollowupDate) {
        this.nextFollowupDate = nextFollowupDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "FollowUp{" +
                "id=" + id +
                ", followupDate=" + followupDate +
                ", statusAtFollowup='" + statusAtFollowup + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FollowUp)) return false;
        FollowUp other = (FollowUp) obj;
        return java.util.Objects.equals(id, other.id);
    }
}

// ---------- notes (unlimited notes per lead) ----------
@Entity
@Table(name = "notes")
class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_lead_id", nullable = false)
    private CustomerLead customerLead;

    @Column(length = 3000)
    private String noteText;

    private LocalDateTime createdDate;

    // No-Args Constructor
    public Note() {
    }

    // All-Args Constructor
    public Note(Long id,
                CustomerLead customerLead,
                String noteText,
                LocalDateTime createdDate) {

        this.id = id;
        this.customerLead = customerLead;
        this.noteText = noteText;
        this.createdDate = createdDate;
    }

    @PrePersist
    void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    // ---------- Getters ----------

    public Long getId() {
        return id;
    }

    public CustomerLead getCustomerLead() {
        return customerLead;
    }

    public String getNoteText() {
        return noteText;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    // ---------- Setters ----------

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerLead(CustomerLead customerLead) {
        this.customerLead = customerLead;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", noteText='" + noteText + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Note))
            return false;
        Note other = (Note) obj;
        return java.util.Objects.equals(id, other.id);
    }
}
