package com.cth.tradefinance;

import com.cth.tradefinance.model.SystemConfig;
import com.cth.tradefinance.model.User;
import com.cth.tradefinance.repository.SystemConfigRepository;
import com.cth.tradefinance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class TradeFinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeFinanceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository,
                                         SystemConfigRepository configRepository,
                                         PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed default admin user
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User(
                    "admin",
                    passwordEncoder.encode("Qwerty123$"),
                    "System Administrator",
                    "admin@cth.com",
                    "ADMIN"
                );
                admin.setAccountNonLocked(true);
                admin.setPasswordExpirationDate(LocalDateTime.now().plusDays(90));
                admin.setTwoFactorEnabled(false);
                userRepository.save(admin);
                System.out.println("Default admin user created successfully (username: admin, password: Qwerty123$)");
            }

            // Seed default system configurations
            if (configRepository.count() == 0) {
                configRepository.save(new SystemConfig("app.name", "CTH TI Plus Profitability Engine", "Configurable application name"));
                configRepository.save(new SystemConfig("auth.ldap.enabled", "false", "Enable LDAP Active Directory Auth"));
                configRepository.save(new SystemConfig("auth.2fa.enabled", "false", "Enable 2FA SMS/Email Authentication"));
                configRepository.save(new SystemConfig("db.type", "H2", "Active Database Platform (DB2, Oracle, Postgres, SQLServer, H2, CSV)"));
            }
        };
    }
}
