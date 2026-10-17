package com.cth.tradefinance.service;

import com.cth.tradefinance.model.TradeTransaction;
import com.cth.tradefinance.repository.TradeTransactionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProfitabilityEngineService {

    private final TradeTransactionRepository transactionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ProfitabilityEngineService(TradeTransactionRepository transactionRepository,
                                       SimpMessagingTemplate messagingTemplate) {
        this.transactionRepository = transactionRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<TradeTransaction> processInputCsv(InputStream inputStream, OutputStream outputStream) throws IOException {
        List<TradeTransaction> processedList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(outputStream != null ? outputStream : baos);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                     .setHeader("TRANSACTION_REF", "TI_REFERENCE", "CUSTOMER_NAME", "PRODUCT_TYPE",
                             "PRINCIPAL_AMOUNT", "FEE_INCOME", "INTEREST_INCOME", "OPERATIONAL_COST",
                             "RISK_PROVISION", "NET_PROFIT", "MARGIN_PERCENTAGE", "STATUS", "RESPONSE_MESSAGE")
                     .build())) {

            for (CSVRecord record : csvParser) {
                String txRef = record.get("TRANSACTION_REF");
                String tiRef = record.get("TI_REFERENCE");
                String customerId = record.isSet("CUSTOMER_ID") ? record.get("CUSTOMER_ID") : "CUST-001";
                String customerName = record.get("CUSTOMER_NAME");
                String productType = record.get("PRODUCT_TYPE");
                String currency = record.isSet("CURRENCY") ? record.get("CURRENCY") : "USD";

                BigDecimal principal = new BigDecimal(record.get("PRINCIPAL_AMOUNT"));
                BigDecimal fee = new BigDecimal(record.get("FEE_INCOME"));
                BigDecimal interest = new BigDecimal(record.get("INTEREST_INCOME"));
                BigDecimal opCost = new BigDecimal(record.get("OPERATIONAL_COST"));
                BigDecimal riskProv = new BigDecimal(record.get("RISK_PROVISION"));

                // Profitability Calculation: Total Revenue = Fee + Interest
                // Net Profit = (Fee + Interest) - (Operational Cost + Risk Provision)
                BigDecimal totalRevenue = fee.add(interest);
                BigDecimal totalCosts = opCost.add(riskProv);
                BigDecimal netProfit = totalRevenue.subtract(totalCosts);

                BigDecimal marginPercentage = BigDecimal.ZERO;
                if (principal.compareTo(BigDecimal.ZERO) > 0) {
                    marginPercentage = netProfit.divide(principal, 6, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(4, RoundingMode.HALF_UP);
                }

                TradeTransaction tx = transactionRepository.findByTransactionRef(txRef)
                        .orElse(new TradeTransaction());

                tx.setTransactionRef(txRef);
                tx.setTiReference(tiRef);
                tx.setCustomerId(customerId);
                tx.setCustomerName(customerName);
                tx.setProductType(productType);
                tx.setCurrency(currency);
                tx.setPrincipalAmount(principal);
                tx.setFeeIncome(fee);
                tx.setInterestIncome(interest);
                tx.setOperationalCost(opCost);
                tx.setRiskProvision(riskProv);
                tx.setNetProfit(netProfit);
                tx.setMarginPercentage(marginPercentage);
                tx.setStatus("PROCESSED_SUCCESS");
                tx.setProcessedAt(LocalDateTime.now());

                transactionRepository.save(tx);
                processedList.add(tx);

                // Write to Output CSV Response
                csvPrinter.printRecord(
                        txRef, tiRef, customerName, productType, principal, fee, interest,
                        opCost, riskProv, netProfit, marginPercentage, "SUCCESS",
                        "Transaction processed successfully by TI Plus Profitability Engine"
                );
            }
            csvPrinter.flush();
        }

        // Broadcast real-time metric update via WebSocket
        broadcastDashboardMetrics();

        return processedList;
    }

    public Map<String, Object> getDashboardMetrics() {
        List<TradeTransaction> all = transactionRepository.findAll();

        long totalCount = all.size();
        BigDecimal totalVolume = all.stream().map(TradeTransaction::getPrincipalAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalNetProfit = all.stream().map(TradeTransaction::getNetProfit).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalFeeIncome = all.stream().map(TradeTransaction::getFeeIncome).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageMargin = BigDecimal.ZERO;
        if (totalVolume.compareTo(BigDecimal.ZERO) > 0) {
            averageMargin = totalNetProfit.divide(totalVolume, 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalCount", totalCount);
        metrics.put("totalVolume", totalVolume);
        metrics.put("totalNetProfit", totalNetProfit);
        metrics.put("totalFeeIncome", totalFeeIncome);
        metrics.put("averageMargin", averageMargin);
        metrics.put("timestamp", LocalDateTime.now().toString());

        return metrics;
    }

    public void broadcastDashboardMetrics() {
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/metrics", getDashboardMetrics());
        }
    }
}
