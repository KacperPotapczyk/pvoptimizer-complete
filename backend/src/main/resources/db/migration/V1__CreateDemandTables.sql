CREATE TABLE demand (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT pk_demand PRIMARY KEY (id)
);
ALTER TABLE demand ADD CONSTRAINT uc_demand_name_is_deleted UNIQUE (name, is_deleted);


CREATE TABLE demand_revision (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    revision_number BIGINT NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    base_id BIGINT,
    CONSTRAINT pk_demand_revision PRIMARY KEY (id)
);
ALTER TABLE demand_revision ADD CONSTRAINT uc_demand_revision_number UNIQUE (base_id, revision_number);
ALTER TABLE demand_revision ADD CONSTRAINT FK_DEMAND_REVISION_ON_DEMAND_BASE FOREIGN KEY (base_id) REFERENCES demand (id);


CREATE TABLE demand_value (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    revision_id BIGINT,
    date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    demand DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_demand_value PRIMARY KEY (id)
);
ALTER TABLE demand_value ADD CONSTRAINT FK_DEMAND_VALUE_ON_REVISION FOREIGN KEY (revision_id) REFERENCES demand_revision (id);
CREATE INDEX idx_demand_value_revision_id ON demand_value(revision_id);