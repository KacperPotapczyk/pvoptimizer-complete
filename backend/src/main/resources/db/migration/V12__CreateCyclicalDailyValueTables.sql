CREATE TABLE cyclical_daily_value (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    tariff_revision BIGINT NOT NULL,
    day_of_the_week INTEGER NOT NULL,
    CONSTRAINT pk_cyclical_daily_value PRIMARY KEY (id)
);
ALTER TABLE cyclical_daily_value ADD CONSTRAINT FK_CYCLICAL_DAILY_VALUE_ON_TARIFF_REVISION FOREIGN KEY (tariff_revision) REFERENCES tariff_revision (id);
CREATE INDEX idx_cyclical_daily_value_tariff_revision ON cyclical_daily_value(tariff_revision);

CREATE TABLE daily_time_value (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    daily_value_id BIGINT NOT NULL,
    start_time time WITHOUT TIME ZONE NOT NULL,
    current_value DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_daily_time_value PRIMARY KEY (id)
);
ALTER TABLE daily_time_value ADD CONSTRAINT FK_DAILY_TIME_VALUE_ON_DAILY_VALUE FOREIGN KEY (daily_value_id) REFERENCES cyclical_daily_value (id);
CREATE INDEX idx_daily_time_value_daily_value_id ON daily_time_value(daily_value_id);