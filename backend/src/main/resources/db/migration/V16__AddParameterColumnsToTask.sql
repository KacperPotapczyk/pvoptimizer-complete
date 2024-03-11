ALTER TABLE task
    ADD timeout_seconds BIGINT,
    ADD relative_gap DOUBLE PRECISION,
    ADD interval_length_minutes BIGINT;

UPDATE task SET
    timeout_seconds = 60,
    relative_gap = 0.1,
    interval_length_minutes = 15;

ALTER TABLE task ALTER COLUMN timeout_seconds SET NOT NULL;
ALTER TABLE task ALTER COLUMN relative_gap SET NOT NULL;
ALTER TABLE task ALTER COLUMN interval_length_minutes SET NOT NULL;