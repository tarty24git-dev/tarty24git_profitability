package com.cth.tradefinance;

import com.cth.tradefinance.model.TradeTransaction;
import com.cth.tradefinance.model.User;
import com.cth.tradefinance.repository.UserRepository;
import com.cth.tradefinance.service.ProfitabilityEngineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TradeFinanceApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfitabilityEngineService engineService;

    @Test
    void testDefaultAdminUserExists() {
        Optional<User> adminOpt = userRepository.findByUsername("admin");
        assertTrue(adminOpt.isPresent(), "Default admin user should exist");
        assertTrue(adminOpt.get().isAccountNonLocked(), "Admin account should be active and non-locked");
    }

    @Test
    void testAccountLockingLogic() {
        User user = new User("testuser", "password123", "Test User", "test@cth.com", "USER");
        user.setAccountNonLocked(true);
        user.setFailedAttempts(2);
        userRepository.save(user);

        // Simulate 3rd failed attempt
        user.setFailedAttempts(user.getFailedAttempts() + 1);
        if (user.getFailedAttempts() >= 3) {
            user.setAccountNonLocked(false);
        }
        userRepository.save(user);

        User updatedUser = userRepository.findByUsername("testuser").orElseThrow();
        assertFalse(updatedUser.isAccountNonLocked(), "User account should be locked after 3 failed attempts");
    }

    @Test
    void testProfitabilityCalculationFromCsv() throws Exception {
        String csvContent = "TRANSACTION_REF,TI_REFERENCE,CUSTOMER_ID,CUSTOMER_NAME,PRODUCT_TYPE,CURRENCY,PRINCIPAL_AMOUNT,FEE_INCOME,INTEREST_INCOME,OPERATIONAL_COST,RISK_PROVISION\n" +
                "TX-1001,TI-8801,CUST-01,ACME Corp,LETTER_OF_CREDIT,USD,100000.00,2500.00,1500.00,500.00,300.00\n";

        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        List<TradeTransaction> transactions = engineService.processInputCsv(inputStream, outputStream);

        assertEquals(1, transactions.size());
        TradeTransaction tx = transactions.get(0);
        assertEquals("TX-1001", tx.getTransactionRef());

        // Net profit = (2500 + 1500) - (500 + 300) = 4000 - 800 = 3200
        assertEquals(0, new java.math.BigDecimal("3200.00").compareTo(tx.getNetProfit()));

        // Margin % = (3200 / 100000) * 100 = 3.2000%
        assertEquals(0, new java.math.BigDecimal("3.2000").compareTo(tx.getMarginPercentage()));

        String responseCsv = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(responseCsv.contains("TX-1001"));
        assertTrue(responseCsv.contains("SUCCESS"));
    }
}
