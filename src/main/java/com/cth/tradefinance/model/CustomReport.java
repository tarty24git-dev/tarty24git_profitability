package com.cth.tradefinance.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "custom_reports")
public class CustomReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reportName;

    private String selectedTables;
    private String selectedFields;

    @Column(length = 1000)
    private String sqlQuery;

    private String createdBy;
    private LocalDateTime createdAt = LocalDateTime.now();

    public CustomReport() {}

    public CustomReport(String reportName, String selectedTables, String selectedFields, String sqlQuery, String createdBy) {
        this.reportName = reportName;
        this.selectedTables = selectedTables;
        this.selectedFields = selectedFields;
        this.sqlQuery = sqlQuery;
        this.createdBy = createdBy;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }

    public String getSelectedTables() { return selectedTables; }
    public void setSelectedTables(String selectedTables) { this.selectedTables = selectedTables; }

    public String getSelectedFields() { return selectedFields; }
    public void setSelectedFields(String selectedFields) { this.selectedFields = selectedFields; }

    public String getSqlQuery() { return sqlQuery; }
    public void setSqlQuery(String sqlQuery) { this.sqlQuery = sqlQuery; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
