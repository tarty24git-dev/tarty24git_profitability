-- H2 In-Memory Schema for Finastra TI Plus Trade Finance Profitability Engine

CREATE TABLE IF NOT EXISTS app_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(150),
    email VARCHAR(150),
    phone_number VARCHAR(50),
    role VARCHAR(50) DEFAULT 'USER',
    account_non_locked BOOLEAN DEFAULT TRUE,
    failed_attempts INT DEFAULT 0,
    password_expiration_date TIMESTAMP,
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS trade_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_ref VARCHAR(100) NOT NULL UNIQUE,
    ti_reference VARCHAR(100),
    customer_id VARCHAR(50),
    customer_name VARCHAR(150),
    product_type VARCHAR(50),
    currency VARCHAR(10),
    principal_amount DECIMAL(18, 2),
    fee_income DECIMAL(18, 2),
    interest_income DECIMAL(18, 2),
    operational_cost DECIMAL(18, 2),
    risk_provision DECIMAL(18, 2),
    net_profit DECIMAL(18, 2),
    margin_percentage DECIMAL(8, 4),
    status VARCHAR(50),
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS system_configs (
    config_key VARCHAR(100) PRIMARY KEY,
    config_value VARCHAR(500),
    description VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS custom_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_name VARCHAR(150) NOT NULL,
    selected_tables VARCHAR(255),
    selected_fields VARCHAR(500),
    sql_query VARCHAR(1000),
    created_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
