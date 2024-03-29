CREATE TABLE storage (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    capacity DOUBLE PRECISION NOT NULL,
    max_charge DOUBLE PRECISION NOT NULL,
    max_discharge DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_storage PRIMARY KEY (id)
);
ALTER TABLE storage ADD CONSTRAINT uc_storage_name_is_deleted UNIQUE (name, is_deleted);

CREATE TABLE storage_revision (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    revision_number BIGINT NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    base_id BIGINT,
    initial_energy DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_storage_revision PRIMARY KEY (id)
);
ALTER TABLE storage_revision ADD CONSTRAINT uc_storage_revision_number UNIQUE (base_id, revision_number);
ALTER TABLE storage_revision ADD CONSTRAINT FK_STORAGE_REVISION_ON_BASE FOREIGN KEY (base_id) REFERENCES storage (id);

CREATE TABLE storage_min_charge_constraint (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    constraint_value DOUBLE PRECISION NOT NULL,
    date_time_start TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    date_time_end TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    revision_id BIGINT,
    CONSTRAINT pk_storage_min_charge_constraint PRIMARY KEY (id)
);
ALTER TABLE storage_min_charge_constraint ADD CONSTRAINT FK_STORAGE_MIN_CHARGE_CONSTRAINT_ON_REVISION FOREIGN KEY (revision_id) REFERENCES storage_revision (id);
CREATE INDEX idx_storage_min_charge_constraint_revision_id ON storage_min_charge_constraint(revision_id);

CREATE TABLE storage_max_charge_constraint (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    constraint_value DOUBLE PRECISION NOT NULL,
    date_time_start TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    date_time_end TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    revision_id BIGINT,
    CONSTRAINT pk_storage_max_charge_constraint PRIMARY KEY (id)
);
ALTER TABLE storage_max_charge_constraint ADD CONSTRAINT FK_STORAGE_MAX_CHARGE_CONSTRAINT_ON_REVISION FOREIGN KEY (revision_id) REFERENCES storage_revision (id);
CREATE INDEX idx_storage_max_charge_constraint_revision_id ON storage_max_charge_constraint(revision_id);

CREATE TABLE storage_min_discharge_constraint (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    constraint_value DOUBLE PRECISION NOT NULL,
    date_time_start TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    date_time_end TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    revision_id BIGINT,
    CONSTRAINT pk_storage_min_discharge_constraint PRIMARY KEY (id)
);
ALTER TABLE storage_min_discharge_constraint ADD CONSTRAINT FK_STORAGE_MIN_DISCHARGE_CONSTRAINT_ON_REVISION FOREIGN KEY (revision_id) REFERENCES storage_revision (id);
CREATE INDEX idx_storage_min_discharge_constraint_revision_id ON storage_min_discharge_constraint(revision_id);

CREATE TABLE storage_max_discharge_constraint (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    constraint_value DOUBLE PRECISION NOT NULL,
    date_time_start TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    date_time_end TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    revision_id BIGINT,
    CONSTRAINT pk_storage_max_discharge_constraint PRIMARY KEY (id)
);
ALTER TABLE storage_max_discharge_constraint ADD CONSTRAINT FK_STORAGE_MAX_DISCHARGE_CONSTRAINT_ON_REVISION FOREIGN KEY (revision_id) REFERENCES storage_revision (id);
CREATE INDEX idx_storage_max_discharge_constraint_revision_id ON storage_max_discharge_constraint(revision_id);

CREATE TABLE storage_min_energy_constraint (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    constraint_value DOUBLE PRECISION NOT NULL,
    date_time_start TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    date_time_end TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    revision_id BIGINT,
    CONSTRAINT pk_storage_min_energy_constraint PRIMARY KEY (id)
);
ALTER TABLE storage_min_energy_constraint ADD CONSTRAINT FK_STORAGE_MIN_ENERGY_CONSTRAINT_ON_REVISION FOREIGN KEY (revision_id) REFERENCES storage_revision (id);
CREATE INDEX idx_storage_min_energy_constraint_revision_id ON storage_min_energy_constraint(revision_id);

CREATE TABLE storage_max_energy_constraint (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    constraint_value DOUBLE PRECISION NOT NULL,
    date_time_start TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    date_time_end TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    revision_id BIGINT,
    CONSTRAINT pk_storage_max_energy_constraint PRIMARY KEY (id)
);
ALTER TABLE storage_max_energy_constraint ADD CONSTRAINT FK_STORAGE_MAX_ENERGY_CONSTRAINT_ON_REVISION FOREIGN KEY (revision_id) REFERENCES storage_revision (id);
CREATE INDEX idx_storage_max_energy_constraint_revision_id ON storage_max_energy_constraint(revision_id);