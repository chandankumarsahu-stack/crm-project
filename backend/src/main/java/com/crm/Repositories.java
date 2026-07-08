package com.crm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

interface LeadTypeRepository extends JpaRepository<LeadType, Long> {
}

interface CustomerLeadRepository extends JpaRepository<CustomerLead, Long>, JpaSpecificationExecutor<CustomerLead> {
    List<CustomerLead> findByNextFollowupDate(LocalDate date);
    List<CustomerLead> findByNextFollowupDateBefore(LocalDate date);
    long countByStatus(String status);
    long countByPriority(String priority);
}

interface FollowUpRepository extends JpaRepository<FollowUp, Long> {
    List<FollowUp> findByCustomerLeadIdOrderByCreatedDateDesc(Long customerLeadId);
}

interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByCustomerLeadIdOrderByCreatedDateDesc(Long customerLeadId);
}
