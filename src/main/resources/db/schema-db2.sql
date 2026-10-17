-- DB2 Schema for Finastra TI Plus Trade Finance Profitability Engine

CREATE TABLE app_users (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
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
    created_at TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE trade_transactions (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
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
    processed_at TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE system_configs (
    config_key VARCHAR(100) NOT NULL,
    config_value VARCHAR(500),
    description VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    PRIMARY KEY (config_key)
);

CREATE TABLE custom_reports (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    report_name VARCHAR(150) NOT NULL,
    selected_tables VARCHAR(255),
    selected_fields VARCHAR(500),
    sql_query VARCHAR(1000),
    created_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    PRIMARY KEY (id)
);
