IF DB_ID(N'paralives_db') IS NULL
BEGIN
    CREATE DATABASE paralives_db;
END;
GO

USE paralives_db;
GO

IF OBJECT_ID(N'dbo.players', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.players (
        id BIGINT IDENTITY(1,1) NOT NULL,
        name NVARCHAR(100) NOT NULL,
        email NVARCHAR(255) NOT NULL,
        created_at DATETIME2 NOT NULL
            CONSTRAINT DF_players_created_at DEFAULT SYSUTCDATETIME(),
        CONSTRAINT PK_players PRIMARY KEY (id),
        CONSTRAINT UK_players_email UNIQUE (email)
    );
END;
