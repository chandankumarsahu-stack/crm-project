-- Reference schema. Not required to run manually — Spring Boot's
-- spring.jpa.hibernate.ddl-auto=update creates/updates these automatically on startup.
-- Kept here to satisfy the "MySQL Script" submission requirement.

CREATE DATABASE IF NOT EXISTS crm_db;
USE crm_db;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS lead_type (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  description VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS customer_lead (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_name VARCHAR(255),
  mobile VARCHAR(50),
  alternate_number VARCHAR(50),
  email VARCHAR(255),
  lead_type_id BIGINT,
  city VARCHAR(255),
  address VARCHAR(1000),
  requirement VARCHAR(2000),
  lead_source VARCHAR(255),
  assigned_executive VARCHAR(255),
  discussion_details VARCHAR(2000),
  visit_date DATE,
  next_followup_date DATE,
  status VARCHAR(50),
  priority VARCHAR(50),
  created_date DATETIME,
  FOREIGN KEY (lead_type_id) REFERENCES lead_type(id)
);

CREATE TABLE IF NOT EXISTS follow_up (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_lead_id BIGINT NOT NULL,
  followup_date DATE,
  remarks VARCHAR(2000),
  status_at_followup VARCHAR(50),
  next_followup_date DATE,
  created_date DATETIME,
  FOREIGN KEY (customer_lead_id) REFERENCES customer_lead(id)
);

CREATE TABLE IF NOT EXISTS notes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_lead_id BIGINT NOT NULL,
  note_text VARCHAR(3000),
  created_date DATETIME,
  FOREIGN KEY (customer_lead_id) REFERENCES customer_lead(id)
);

-- Default login
INSERT INTO users (username, password) VALUES ('admin', 'admin123');
