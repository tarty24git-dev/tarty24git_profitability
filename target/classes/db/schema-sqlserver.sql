-- Microsoft SQL Server Schema for Finastra TI Plus Trade Finance Profitability Engine

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='app_users' AND xtype='U')
BEGIN
    CREATE TABLE app_users (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        username VARCHAR(100) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        full_name VARCHAR(150),
        email VARCHAR(150),
        phone_number VARCHAR(50),
        role VARCHAR(50) DEFAULT 'USER',
        account_non_locked BIT DEFAULT 1,
        failed_attempts INT DEFAULT 0,
        password_expiration_date DATETIME,
        two_factor_enabled BIT DEFAULT 0,
        created_at DATETIME DEFAULT GETDATE()
    );
END

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='trade_transactions' AND xtype='U')
BEGIN
    CREATE TABLE trade_transactions (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
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
        processed_at DATETIME DEFAULT GETDATE()
    );
END

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='system_configs' AND xtype='U')
BEGIN
    CREATE TABLE system_configs (
        config_key VARCHAR(100) PRIMARY KEY,
        config_value VARCHAR(500),
        description VARCHAR(255),
        updated_at DATETIME DEFAULT GETDATE()
    );
END

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='custom_reports' AND xtype='U')
BEGIN
    CREATE TABLE custom_reports (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        report_name VARCHAR(150) NOT NULL,
        selected_tables VARCHAR(255),
        selected_fields VARCHAR(500),
        sql_query VARCHAR(1000),
        created_by VARCHAR(100),
        created_at DATETIME DEFAULT GETDATE()
    );
END
