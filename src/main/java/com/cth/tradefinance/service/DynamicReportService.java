package com.cth.tradefinance.service;

import com.cth.tradefinance.model.CustomReport;
import com.cth.tradefinance.repository.CustomReportRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class DynamicReportService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CustomReportRepository reportRepository;

    private static final Set<String> ALLOWED_TABLES = Set.of("trade_transactions", "app_users");
    private static final Set<String> ALLOWED_FIELDS = Set.of(
            "transaction_ref", "ti_reference", "customer_id", "customer_name", "product_type",
            "currency", "principal_amount", "fee_income", "interest_income", "operational_cost",
            "risk_provision", "net_profit", "margin_percentage", "status", "processed_at",
            "id", "username", "full_name", "email", "role", "account_non_locked", "created_at"
    );

    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

    public DynamicReportService(CustomReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Map<String, String>> getAvailableTablesAndFields() {
        List<Map<String, String>> tables = new ArrayList<>();

        tables.add(Map.of("table", "trade_transactions", "field", "transaction_ref", "type", "STRING"));
        tables.add(Map.of("table", "trade_transactions", "field", "ti_reference", "type", "STRING"));
        tables.add(Map.of("table", "trade_transactions", "field", "customer_name", "type", "STRING"));
        tables.add(Map.of("table", "trade_transactions", "field", "product_type", "type", "STRING"));
        tables.add(Map.of("table", "trade_transactions", "field", "principal_amount", "type", "DECIMAL"));
        tables.add(Map.of("table", "trade_transactions", "field", "fee_income", "type", "DECIMAL"));
        tables.add(Map.of("table", "trade_transactions", "field", "net_profit", "type", "DECIMAL"));
        tables.add(Map.of("table", "trade_transactions", "field", "margin_percentage", "type", "DECIMAL"));
        tables.add(Map.of("table", "trade_transactions", "field", "status", "type", "STRING"));

        tables.add(Map.of("table", "app_users", "field", "username", "type", "STRING"));
        tables.add(Map.of("table", "app_users", "field", "full_name", "type", "STRING"));
        tables.add(Map.of("table", "app_users", "field", "role", "type", "STRING"));

        return tables;
    }

    public List<?> executeCustomQuery(String tableName, List<String> fields) {
        if (tableName == null || !ALLOWED_TABLES.contains(tableName.toLowerCase())) {
            throw new IllegalArgumentException("Invalid or untrusted table name: " + tableName);
        }

        List<String> validatedFields = new ArrayList<>();
        if (fields == null || fields.isEmpty()) {
            validatedFields.add("*");
        } else {
            for (String f : fields) {
                String cleanField = f.trim().toLowerCase();
                if (ALLOWED_FIELDS.contains(cleanField) && IDENTIFIER_PATTERN.matcher(cleanField).matches()) {
                    validatedFields.add(cleanField);
                } else {
                    throw new IllegalArgumentException("Invalid field identifier: " + f);
                }
            }
        }

        String fieldClause = String.join(", ", validatedFields);
        String sql = "SELECT " + fieldClause + " FROM " + tableName.toLowerCase();

        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

    public CustomReport saveReport(CustomReport report) {
        return reportRepository.save(report);
    }

    public List<CustomReport> getAllReports() {
        return reportRepository.findAll();
    }
}
