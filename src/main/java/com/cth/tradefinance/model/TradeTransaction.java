package com.cth.tradefinance.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trade_transactions")
public class TradeTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionRef;

    private String tiReference;
    private String customerId;
    private String customerName;
    private String productType; // LC, BG, Trade Loan, Export Bill
    private String currency;

    private BigDecimal principalAmount;
    private BigDecimal feeIncome;
    private BigDecimal interestIncome;
    private BigDecimal operationalCost;
    private BigDecimal riskProvision;
    private BigDecimal netProfit;
    private BigDecimal marginPercentage;

    private String status = "PROCESSED";
    private LocalDateTime processedAt = LocalDateTime.now();

    public TradeTransaction() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public String getTiReference() { return tiReference; }
    public void setTiReference(String tiReference) { this.tiReference = tiReference; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public BigDecimal getFeeIncome() { return feeIncome; }
    public void setFeeIncome(BigDecimal feeIncome) { this.feeIncome = feeIncome; }

    public BigDecimal getInterestIncome() { return interestIncome; }
    public void setInterestIncome(BigDecimal interestIncome) { this.interestIncome = interestIncome; }

    public BigDecimal getOperationalCost() { return operationalCost; }
    public void setOperationalCost(BigDecimal operationalCost) { this.operationalCost = operationalCost; }

    public BigDecimal getRiskProvision() { return riskProvision; }
    public void setRiskProvision(BigDecimal riskProvision) { this.riskProvision = riskProvision; }

    public BigDecimal getNetProfit() { return netProfit; }
    public void setNetProfit(BigDecimal netProfit) { this.netProfit = netProfit; }

    public BigDecimal getMarginPercentage() { return marginPercentage; }
    public void setMarginPercentage(BigDecimal marginPercentage) { this.marginPercentage = marginPercentage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
