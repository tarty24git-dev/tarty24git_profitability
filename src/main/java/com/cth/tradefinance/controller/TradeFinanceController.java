package com.cth.tradefinance.controller;

import com.cth.tradefinance.model.CustomReport;
import com.cth.tradefinance.model.TradeTransaction;
import com.cth.tradefinance.repository.TradeTransactionRepository;
import com.cth.tradefinance.service.DynamicReportService;
import com.cth.tradefinance.service.ProfitabilityEngineService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trade")
public class TradeFinanceController {

    private final ProfitabilityEngineService engineService;
    private final TradeTransactionRepository transactionRepository;
    private final DynamicReportService reportService;

    public TradeFinanceController(ProfitabilityEngineService engineService,
                                  TradeTransactionRepository transactionRepository,
                                  DynamicReportService reportService) {
        this.engineService = engineService;
        this.transactionRepository = transactionRepository;
        this.reportService = reportService;
    }

    @PostMapping("/process-csv")
    public ResponseEntity<?> processCsv(@RequestParam("file") MultipartFile file) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            List<TradeTransaction> processed = engineService.processInputCsv(file.getInputStream(), outputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"TI_Plus_Output_Response.csv\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to process CSV file: " + e.getMessage()));
        }
    }

    @GetMapping("/transactions")
    public List<TradeTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @GetMapping("/metrics")
    public Map<String, Object> getMetrics() {
        return engineService.getDashboardMetrics();
    }

    @GetMapping("/report/metadata")
    public List<Map<String, String>> getReportMetadata() {
        return reportService.getAvailableTablesAndFields();
    }

    @PostMapping("/report/query")
    public ResponseEntity<?> runCustomReport(@RequestBody Map<String, Object> request) {
        String tableName = (String) request.get("tableName");
        @SuppressWarnings("unchecked")
        List<String> fields = (List<String>) request.get("fields");

        List<?> results = reportService.executeCustomQuery(tableName, fields);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/report/save")
    public ResponseEntity<CustomReport> saveReport(@RequestBody CustomReport report) {
        return ResponseEntity.ok(reportService.saveReport(report));
    }

    @GetMapping("/report/saved")
    public List<CustomReport> getSavedReports() {
        return reportService.getAllReports();
    }
}
