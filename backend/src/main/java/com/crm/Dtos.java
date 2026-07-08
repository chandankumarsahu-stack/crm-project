package com.crm;

import java.time.LocalDate;

class LoginRequest {

    private String username;
    private String password;

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class LoginResponse {

    private boolean success;
    private String message;
    private String username;

    public LoginResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

class LeadTypeRequest {

    private String name;
    private String description;

    public LeadTypeRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

class CustomerLeadRequest {

    private String customerName;
    private String mobile;
    private String alternateNumber;
    private String email;
    private Long leadTypeId;
    private String city;
    private String address;
    private String requirement;
    private String leadSource;
    private String assignedExecutive;
    private String discussionDetails;
    private LocalDate visitDate;
    private LocalDate nextFollowupDate;
    private String status;
    private String priority;

    public CustomerLeadRequest() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getLeadTypeId() {
        return leadTypeId;
    }

    public void setLeadTypeId(Long leadTypeId) {
        this.leadTypeId = leadTypeId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

    public String getAssignedExecutive() {
        return assignedExecutive;
    }

    public void setAssignedExecutive(String assignedExecutive) {
        this.assignedExecutive = assignedExecutive;
    }

    public String getDiscussionDetails() {
        return discussionDetails;
    }

    public void setDiscussionDetails(String discussionDetails) {
        this.discussionDetails = discussionDetails;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public LocalDate getNextFollowupDate() {
        return nextFollowupDate;
    }

    public void setNextFollowupDate(LocalDate nextFollowupDate) {
        this.nextFollowupDate = nextFollowupDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}

class FollowUpRequest {

    private Long customerLeadId;
    private LocalDate followupDate;
    private String remarks;
    private String statusAtFollowup;
    private LocalDate nextFollowupDate;

    public FollowUpRequest() {
    }

    public Long getCustomerLeadId() {
        return customerLeadId;
    }

    public void setCustomerLeadId(Long customerLeadId) {
        this.customerLeadId = customerLeadId;
    }

    public LocalDate getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(LocalDate followupDate) {
        this.followupDate = followupDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatusAtFollowup() {
        return statusAtFollowup;
    }

    public void setStatusAtFollowup(String statusAtFollowup) {
        this.statusAtFollowup = statusAtFollowup;
    }

    public LocalDate getNextFollowupDate() {
        return nextFollowupDate;
    }

    public void setNextFollowupDate(LocalDate nextFollowupDate) {
        this.nextFollowupDate = nextFollowupDate;
    }
}

class NoteRequest {

    private Long customerLeadId;
    private String noteText;

    public NoteRequest() {
    }

    public Long getCustomerLeadId() {
        return customerLeadId;
    }

    public void setCustomerLeadId(Long customerLeadId) {
        this.customerLeadId = customerLeadId;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}

class LeadSearchCriteria {

    private String name;
    private String mobile;
    private Long leadTypeId;
    private String status;
    private String priority;
    private String city;
    private LocalDate fromDate;
    private LocalDate toDate;

    public LeadSearchCriteria() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getLeadTypeId() {
        return leadTypeId;
    }

    public void setLeadTypeId(Long leadTypeId) {
        this.leadTypeId = leadTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}

class DashboardStats {

    private long totalLeads;
    private long todaysFollowups;
    private long pendingFollowups;
    private long hotCustomers;
    private long closedDeals;

    public DashboardStats() {
    }

    public long getTotalLeads() {
        return totalLeads;
    }

    public void setTotalLeads(long totalLeads) {
        this.totalLeads = totalLeads;
    }

    public long getTodaysFollowups() {
        return todaysFollowups;
    }

    public void setTodaysFollowups(long todaysFollowups) {
        this.todaysFollowups = todaysFollowups;
    }

    public long getPendingFollowups() {
        return pendingFollowups;
    }

    public void setPendingFollowups(long pendingFollowups) {
        this.pendingFollowups = pendingFollowups;
    }

    public long getHotCustomers() {
        return hotCustomers;
    }

    public void setHotCustomers(long hotCustomers) {
        this.hotCustomers = hotCustomers;
    }

    public long getClosedDeals() {
        return closedDeals;
    }

    public void setClosedDeals(long closedDeals) {
        this.closedDeals = closedDeals;
    }
}