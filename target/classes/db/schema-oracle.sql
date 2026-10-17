-- Oracle Schema for Finastra TI Plus Trade Finance Profitability Engine

CREATE TABLE app_users (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR2(100) NOT NULL UNIQUE,
    password VARCHAR2(255) NOT NULL,
    full_name VARCHAR2(150),
    email VARCHAR2(150),
    phone_number VARCHAR2(50),
    role VARCHAR2(50) DEFAULT 'USER',
    account_non_locked NUMBER(1) DEFAULT 1,
    failed_attempts NUMBER(10) DEFAULT 0,
    password_expiration_date TIMESTAMP,
    two_factor_enabled NUMBER(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE trade_transactions (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transaction_ref VARCHAR2(100) NOT NULL UNIQUE,
    ti_reference VARCHAR2(100),
    customer_id VARCHAR2(50),
    customer_name VARCHAR2(150),
    product_type VARCHAR2(50),
    currency VARCHAR2(10),
    principal_amount NUMBER(18, 2),
    fee_income NUMBER(18, 2),
    interest_income NUMBER(18, 2),
    operational_cost NUMBER(18, 2),
    risk_provision NUMBER(18, 2),
    net_profit NUMBER(18, 2),
    margin_percentage NUMBER(8, 4),
    status VARCHAR2(50),
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE system_configs (
    config_key VARCHAR2(100) PRIMARY KEY,
    config_value VARCHAR2(500),
    description VARCHAR2(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE custom_reports (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    report_name VARCHAR2(150) NOT NULL,
    selected_tables VARCHAR2(255),
    selected_fields VARCHAR2(500),
    sql_query VARCHAR2(1000),
    created_by VARCHAR2(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
