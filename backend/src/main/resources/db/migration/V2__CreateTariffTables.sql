CREATE TABLE tariff (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT pk_tariff PRIMARY KEY (id)
);
ALTER TABLE tariff ADD CONSTRAINT uc_tariff_name_is_deleted UNIQUE (name, is_deleted);

CREATE TABLE tariff_revision (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    revision_number BIGINT NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    base_id BIGINT,
    default_price FLOAT(53) DEFAULT 0 NOT NULL,
    CONSTRAINT pk_tariff_revision PRIMARY KEY (id)
);
ALTER TABLE tariff_revision ADD CONSTRAINT uc_tariff_revision_number UNIQUE (base_id, revision_number);
ALTER TABLE tariff_revision ADD CONSTRAINT FK_TARIFF_REVISION_ON_BASE FOREIGN KEY (base_id) REFERENCES tariff (id);