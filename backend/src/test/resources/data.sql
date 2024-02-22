------------------------------------------------------------------------ Demands
INSERT INTO demand(name) VALUES('queryOnly');   -- Demand #1
INSERT INTO demand_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-05T12:00:00', 1);  -- DemandRevision #1
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(1, '2023-10-05T12:00:00', 4.5);
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(1, '2023-10-05T13:00:00', 4.1);

INSERT INTO demand_revision(revision_number, created_date, base_id) VALUES(2, '2023-10-06T12:00:00', 1);  -- DemandRevision #2
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(2, '2023-10-05T12:00:00', 4.4);
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(2, '2023-10-05T13:00:00', 4.0);
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(2, '2023-10-05T14:00:00', 3.2);
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(2, '2023-10-05T15:00:00', 6.8);

INSERT INTO demand_revision(revision_number, created_date, base_id, is_deleted) VALUES(3, '2023-10-05T12:00:00', 1, TRUE);  -- DemandRevision #3
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(3, '2023-09-01T12:00:00', 2.1);


INSERT INTO demand(name) VALUES('addRevisionTest');  -- Demand #2
INSERT INTO demand_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-07T12:00:00', 2);  -- DemandRevision #4
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(4, '2023-10-06T12:00:00', 1.5);
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(4, '2023-10-06T13:00:00', 1.1);


INSERT INTO demand(name, is_deleted) VALUES('deleted', TRUE);  -- Demand #3 deleted
INSERT INTO demand_revision(revision_number, created_date, base_id, is_deleted) VALUES(1, '2023-10-07T12:00:00', 3, TRUE);  -- DemandRevision #5
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(5, '2023-10-06T13:00:00', 1.1);


INSERT INTO demand(name) VALUES('toBeDeleted');  -- Demand #4
INSERT INTO demand_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-07T12:00:00', 4);  -- DemandRevision #6
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(6, '2023-10-06T13:00:00', 1.1);


INSERT INTO demand(name) VALUES('revisionToBeDeleted');  -- Demand #5
INSERT INTO demand_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-07T12:00:00', 5);  -- DemandRevision #7
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(7, '2023-10-06T13:00:00', 1.1);


INSERT INTO demand(name) VALUES('revisionToBeDeletedHttpRequest');  -- Demand #6
INSERT INTO demand_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-07T12:00:00', 6);  -- DemandRevision #8
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(8, '2023-10-06T13:00:00', 1.1);

INSERT INTO demand_revision(revision_number, created_date, base_id) VALUES(2, '2023-10-07T12:00:00', 6);  -- DemandRevision #9
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(9, '2023-10-06T14:00:00', 5.1);


INSERT INTO demand(name) VALUES('toBeDeletedHttpRequest');  -- Demand #7
INSERT INTO demand_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-07T12:00:00', 7);  -- DemandRevision #10
INSERT INTO demand_value(revision_id, date_time, demand) VALUES(10, '2023-10-06T13:00:00', 1.1);


------------------------------------------------------------------------ Productions
INSERT INTO production(name) VALUES('queryOnly');   -- production #1
INSERT INTO production_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-05T12:00:00', 1);  -- productionRevision #1
INSERT INTO production_value(revision_id, date_time, production) VALUES(1, '2023-10-05T12:00:00', 4.5);
INSERT INTO production_value(revision_id, date_time, production) VALUES(1, '2023-10-05T13:00:00', 4.1);

INSERT INTO production_revision(revision_number, created_date, base_id) VALUES(2, '2023-10-06T12:00:00', 1);  -- productionRevision #2
INSERT INTO production_value(revision_id, date_time, production) VALUES(2, '2023-10-05T12:00:00', 4.4);
INSERT INTO production_value(revision_id, date_time, production) VALUES(2, '2023-10-05T13:00:00', 4.0);
INSERT INTO production_value(revision_id, date_time, production) VALUES(2, '2023-10-05T14:00:00', 3.2);
INSERT INTO production_value(revision_id, date_time, production) VALUES(2, '2023-10-05T15:00:00', 6.8);

INSERT INTO production_revision(revision_number, created_date, base_id, is_deleted) VALUES(3, '2023-10-05T12:00:00', 1, TRUE);  -- productionRevision #3
INSERT INTO production_value(revision_id, date_time, production) VALUES(3, '2023-09-01T12:00:00', 2.1);


INSERT INTO production(name) VALUES('addRevisionTest');  -- production #2
INSERT INTO production_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-07T12:00:00', 2);  -- productionRevision #4
INSERT INTO production_value(revision_id, date_time, production) VALUES(4, '2023-10-06T12:00:00', 1.5);
INSERT INTO production_value(revision_id, date_time, production) VALUES(4, '2023-10-06T13:00:00', 1.1);


INSERT INTO production(name, is_deleted) VALUES('deleted', TRUE);  -- production #3 deleted
INSERT INTO production_revision(revision_number, created_date, base_id, is_deleted) VALUES(1, '2023-10-07T12:00:00', 3, TRUE);  -- productionRevision #5
INSERT INTO production_value(revision_id, date_time, production) VALUES(5, '2023-10-06T13:00:00', 1.1);


INSERT INTO production(name) VALUES('toBeDeleted');  -- production #4
INSERT INTO production_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-07T12:00:00', 4);  -- productionRevision #6
INSERT INTO production_value(revision_id, date_time, production) VALUES(6, '2023-10-06T13:00:00', 1.1);


INSERT INTO production(name) VALUES('revisionToBeDeleted');  -- production #5
INSERT INTO production_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-07T12:00:00', 5);  -- productionRevision #7
INSERT INTO production_value(revision_id, date_time, production) VALUES(7, '2023-10-06T13:00:00', 1.1);


INSERT INTO production(name) VALUES('revisionToBeDeletedHttpRequest');  -- production #6
INSERT INTO production_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-07T12:00:00', 6);  -- productionRevision #8
INSERT INTO production_value(revision_id, date_time, production) VALUES(8, '2023-10-06T13:00:00', 1.1);

INSERT INTO production_revision(revision_number, created_date, base_id) VALUES(2, '2023-10-07T12:00:00', 6);  -- productionRevision #9
INSERT INTO production_value(revision_id, date_time, production) VALUES(9, '2023-10-06T14:00:00', 5.1);


INSERT INTO production(name) VALUES('toBeDeletedHttpRequest');  -- production #7
INSERT INTO production_revision(revision_number, created_date, base_id) VALUES(1, '2023-10-07T12:00:00', 7);  -- productionRevision #10
INSERT INTO production_value(revision_id, date_time, production) VALUES(10, '2023-10-06T13:00:00', 1.1);


------------------------------------------------------------------------ Tariffs
INSERT INTO tariff(name) VALUES('queryOnly');   -- Tariff #1
INSERT INTO tariff_revision(revision_number, created_date, base_id, default_price) VALUES(1, '2023-10-15T12:00:00', 1, 0.02);  -- TariffRevision #1

INSERT INTO cyclical_daily_value(tariff_revision, day_of_the_week) VALUES(1, 8);    -- CyclicalDailyValue #1
INSERT INTO daily_time_value(daily_value_id, start_time, current_value) VALUES(1, '06:00:00', 0.03);  -- DailyTimeValue #1
INSERT INTO daily_time_value(daily_value_id, start_time, current_value) VALUES(1, '22:00:00', 0.01);  -- DailyTimeValue #2

INSERT INTO cyclical_daily_value(tariff_revision, day_of_the_week) VALUES(1, 9);    -- CyclicalDailyValue #2
INSERT INTO daily_time_value(daily_value_id, start_time, current_value) VALUES(2, '08:00:00', 0.025);  -- DailyTimeValue #3
INSERT INTO daily_time_value(daily_value_id, start_time, current_value) VALUES(2, '23:30:00', 0.005);  -- DailyTimeValue #4

INSERT INTO tariff_revision(revision_number, created_date, base_id, default_price) VALUES(2, '2023-10-15T13:42:00', 1, 0.056);  -- TariffRevision #2


INSERT INTO tariff(name) VALUES('addRevisionTest');   -- Tariff #2
INSERT INTO tariff_revision(revision_number, created_date, base_id, default_price) VALUES(1, '2023-10-15T12:00:00', 2, 0.02);  -- TariffRevision #3


INSERT INTO tariff(name, is_deleted) VALUES('deleted', TRUE);  -- Tariff #3 deleted
INSERT INTO tariff_revision(revision_number, created_date, base_id, is_deleted) VALUES(1, '2023-10-07T12:00:00', 3, TRUE);  -- TariffRevision #4


INSERT INTO tariff(name) VALUES('toBeDeleted');  -- Tariff #4
INSERT INTO tariff_revision(revision_number, created_date, base_id, default_price) VALUES(1, '2023-10-07T12:00:00', 4, 123.3);  -- TariffRevision #5


INSERT INTO tariff(name) VALUES('revisionToBeDeleted');  -- Tariff #5
INSERT INTO tariff_revision(revision_number, created_date, base_id, default_price) VALUES(1, '2023-10-07T12:00:00', 5, 0.002);  -- TariffRevision #6


INSERT INTO tariff(name) VALUES('toBeDeletedHttpRequest');  -- Tariff #6
INSERT INTO tariff_revision(revision_number, created_date, base_id, default_price) VALUES(1, '2023-10-07T12:00:00', 6, 123.3);  -- TariffRevision #7


INSERT INTO tariff(name) VALUES('revisionToBeDeletedHttpRequest');  -- Tariff #7
INSERT INTO tariff_revision(revision_number, created_date, base_id, default_price) VALUES(1, '2023-10-07T12:00:00', 7, 0.002);  -- TariffRevision #8
INSERT INTO tariff_revision(revision_number, created_date, base_id, default_price) VALUES(2, '2023-10-07T13:00:00', 7, 1.002);  -- TariffRevision #9


------------------------------------------------------------------------ Contracts
INSERT INTO contract(name, tariff_base_id, contract_type) VALUES('queryOnly', 1, 0); -- Contract #1
INSERT INTO contract_revision(revision_number, created_date, base_id) VALUES(1, '2023-11-23T17:00:00', 1);   -- ContractRevision #1
INSERT INTO contract_min_energy_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(1.0, '2023-12-23T17:00:00', '2023-12-24T17:00:00', 1);
INSERT INTO contract_min_energy_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(0.9, '2023-12-24T15:00:00', '2023-12-25T19:00:00', 1);

INSERT INTO contract_revision(revision_number, created_date, base_id) VALUES(2, '2023-11-23T17:10:00', 1);   -- ContractRevision #2
INSERT INTO contract_min_power_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(1.0, '2023-12-23T17:00:00', '2023-12-24T17:00:00', 2);
INSERT INTO contract_min_power_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(0.9, '2023-12-24T15:00:00', '2023-12-25T19:00:00', 2);
INSERT INTO contract_max_power_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(1.9, '2023-12-24T15:00:00', '2023-12-25T19:00:00', 2);
INSERT INTO contract_min_energy_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(1.0, '2023-12-23T17:00:00', '2023-12-24T17:00:00', 2);
INSERT INTO contract_max_energy_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(3.9, '2023-12-24T15:00:00', '2023-12-25T19:00:00', 2);

INSERT INTO contract_revision(revision_number, created_date, base_id, is_deleted) VALUES(3, '2023-11-23T17:10:00', 1, TRUE);   -- ContractRevision #3

INSERT INTO contract(name, tariff_base_id, contract_type) VALUES('addRevisionTest', 1, 1); -- Contract #2
INSERT INTO contract_revision(revision_number, created_date, base_id) VALUES(1, '2023-11-23T17:00:00', 2);   -- ContractRevision #4


INSERT INTO contract(name, tariff_base_id, contract_type, is_deleted) VALUES('deleted', 1, 0, TRUE); -- Contract #3
INSERT INTO contract_revision(revision_number, created_date, base_id, is_deleted) VALUES(1, '2023-11-23T17:00:00', 3, TRUE);   -- ContractRevision #5
INSERT INTO contract_min_energy_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(1.0, '2023-12-23T17:00:00', '2023-12-24T17:00:00', 5);


INSERT INTO contract(name, tariff_base_id, contract_type) VALUES('toBeDeleted', 1, 0); -- Contract #4
INSERT INTO contract_revision(revision_number, created_date, base_id) VALUES(1, '2023-11-23T17:00:00', 4);   -- ContractRevision #6
INSERT INTO contract_min_energy_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(1.0, '2023-12-23T17:00:00', '2023-12-24T17:00:00', 6);


INSERT INTO contract(name, tariff_base_id, contract_type) VALUES('revisionToBeDeleted', 1, 1); -- Contract #5
INSERT INTO contract_revision(revision_number, created_date, base_id) VALUES(1, '2023-11-23T17:00:00', 5);   -- ContractRevision #7
INSERT INTO contract_min_energy_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(1.0, '2023-12-23T17:00:00', '2023-12-24T17:00:00', 7);


INSERT INTO contract(name, tariff_base_id, contract_type) VALUES('toBeDeletedHttpRequest', 1, 0); -- Contract #6
INSERT INTO contract_revision(revision_number, created_date, base_id) VALUES(1, '2023-11-23T17:00:00', 6);   -- ContractRevision #8
INSERT INTO contract_max_power_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(1.0, '2023-12-23T17:00:00', '2023-12-24T17:00:00', 8);


INSERT INTO contract(name, tariff_base_id, contract_type) VALUES('revisionToBeDeletedHttpRequest', 1, 1); -- Contract #7
INSERT INTO contract_revision(revision_number, created_date, base_id) VALUES(1, '2023-11-23T17:00:00', 7);   -- ContractRevision #9
INSERT INTO contract_min_power_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(1.0, '2023-12-23T17:00:00', '2023-12-24T17:00:00', 9);

INSERT INTO contract_revision(revision_number, created_date, base_id) VALUES(2, '2023-11-23T17:00:00', 7);   -- ContractRevision #10
INSERT INTO contract_max_energy_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(1.0, '2023-12-23T17:00:00', '2023-12-24T17:00:00', 10);


------------------------------------------------------------------------ Storages
INSERT INTO storage(name, capacity, max_charge, max_discharge) VALUES('queryOnly', 100, 10, 20);                   -- storage #1
INSERT INTO storage_revision(revision_number, initial_energy, created_date, base_id) VALUES(1, 40.0, '2024-02-09T17:00:00', 1); -- storageRevision #1
INSERT INTO storage_min_charge_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(2, '2024-02-09T17:00:00', '2024-02-09T17:15:00', 1);
INSERT INTO storage_max_energy_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(95, '2024-02-09T17:00:00', '2024-02-09T17:15:00', 1);

INSERT INTO storage_revision(revision_number, initial_energy, created_date, base_id) VALUES(2, 45.0, '2024-02-09T17:00:00', 1); -- storageRevision #2
INSERT INTO storage_max_charge_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(8, '2024-02-09T17:00:00', '2024-02-09T17:15:00', 2);
INSERT INTO storage_max_discharge_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(15, '2024-02-09T17:00:00', '2024-02-09T17:15:00', 2);
INSERT INTO storage_min_energy_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(10, '2024-02-09T17:00:00', '2024-02-09T17:15:00', 2);

INSERT INTO storage_revision(revision_number, initial_energy, created_date, base_id, is_deleted) VALUES(3, 40.0, '2024-02-09T17:01:00', 1, TRUE); -- storageRevision #3


INSERT INTO storage(name, capacity, max_charge, max_discharge) VALUES('addRevisionTest', 100, 10, 20);                   -- storage #2
INSERT INTO storage_revision(revision_number, initial_energy, created_date, base_id) VALUES(1, 40.0, '2024-02-09T17:00:00', 2); -- storageRevision #4


INSERT INTO storage(name, capacity, max_charge, max_discharge) VALUES('toBeDeleted', 110, 15, 26);                   -- storage #3
INSERT INTO storage_revision(revision_number, initial_energy, created_date, base_id) VALUES(1, 42.0, '2024-02-09T17:00:00', 3); -- storageRevision #5


INSERT INTO storage(name, capacity, max_charge, max_discharge) VALUES('revisionToBeDeleted', 110, 15, 26);                   -- storage #4
INSERT INTO storage_revision(revision_number, initial_energy, created_date, base_id) VALUES(1, 42.0, '2024-02-09T17:00:00', 4); -- storageRevision #6
INSERT INTO storage_max_charge_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(8, '2024-02-09T17:00:00', '2024-02-09T17:15:00', 6);


INSERT INTO storage(name, capacity, max_charge, max_discharge) VALUES('toBeDeletedHttpRequest', 110, 15, 26);                   -- storage #5
INSERT INTO storage_revision(revision_number, initial_energy, created_date, base_id) VALUES(1, 42.0, '2024-02-09T17:00:00', 5); -- storageRevision #7


INSERT INTO storage(name, capacity, max_charge, max_discharge) VALUES('revisionToBeDeletedHttpRequest', 110, 15, 26);                   -- storage #6
INSERT INTO storage_revision(revision_number, initial_energy, created_date, base_id) VALUES(1, 42.0, '2024-02-09T17:00:00', 6); -- storageRevision #8
INSERT INTO storage_min_energy_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(10, '2024-02-09T17:00:00', '2024-02-09T17:15:00', 8);

INSERT INTO storage_revision(revision_number, initial_energy, created_date, base_id) VALUES(2, 42.0, '2024-02-09T17:00:00', 6); -- storageRevision #9
INSERT INTO storage_max_discharge_constraint(constraint_value, date_time_start, date_time_end, revision_id)
            VALUES(15, '2024-02-09T17:00:00', '2024-02-09T17:15:00', 9);


------------------------------------------------------------------------ Tasks
INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('queryOnly', '2023-12-24T14:00:00', '2023-12-24T17:00:00', FALSE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #1

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(1, 1);
INSERT INTO task_production_revisions(task_id, revision_id) VALUES(1, 1);
INSERT INTO task_tariff_revisions(task_id, revision_id) VALUES(1, 1);
INSERT INTO task_contract_revisions(task_id, revision_id) VALUES(1, 1);
INSERT INTO task_contract_revisions(task_id, revision_id) VALUES(1, 3);
INSERT INTO task_storage_revisions(task_id, revision_id) VALUES(1, 1);


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('readOnly', '2023-12-24T14:00:00', '2023-12-24T17:00:00', TRUE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #2

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(2, 1);
INSERT INTO task_tariff_revisions(task_id, revision_id) VALUES(2, 1);
INSERT INTO task_contract_revisions(task_id, revision_id) VALUES(2, 1);


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('toBeDeleted', '2023-12-24T14:00:00', '2023-12-24T17:00:00', FALSE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #3

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(3, 1);
INSERT INTO task_tariff_revisions(task_id, revision_id) VALUES(3, 1);
INSERT INTO task_contract_revisions(task_id, revision_id) VALUES(3, 1);


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('toBeDeletedHttpRequest', '2023-12-24T14:00:00', '2023-12-24T17:00:00', FALSE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #4

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(4, 1);
INSERT INTO task_tariff_revisions(task_id, revision_id) VALUES(4, 1);
INSERT INTO task_contract_revisions(task_id, revision_id) VALUES(4, 1);


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('toBeUpdatedHttpRequest', '2023-12-24T14:00:00', '2023-12-24T17:00:00', FALSE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #5

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(5, 1);


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('sendToCompute', '2023-12-24T14:00:00', '2023-12-24T17:00:00', FALSE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #6

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(6, 1);
INSERT INTO task_tariff_revisions(task_id, revision_id) VALUES(6, 1);
INSERT INTO task_contract_revisions(task_id, revision_id) VALUES(6, 1);


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('sendToComputeHttpRequest', '2023-12-24T14:00:00', '2023-12-24T17:00:00', FALSE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #7

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(7, 1);


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('toCreateResults', '2023-12-24T14:00:00', '2023-12-24T17:00:00', TRUE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #8


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('toAddValidationMessage', '2023-12-24T14:00:00', '2023-12-24T17:00:00', TRUE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #9


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('toAddCalculationResult', '2023-01-01T10:00:00', '2023-01-01T10:30:00', TRUE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #10

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(10, 1);
INSERT INTO task_tariff_revisions(task_id, revision_id) VALUES(10, 1);
INSERT INTO task_contract_revisions(task_id, revision_id) VALUES(10, 1);
INSERT INTO task_storage_revisions(task_id, revision_id) VALUES(10, 1);


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('addResultToValidationError', '2023-01-01T10:00:00', '2023-01-01T10:30:00', TRUE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #11

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(11, 1);
INSERT INTO task_tariff_revisions(task_id, revision_id) VALUES(11, 1);
INSERT INTO task_contract_revisions(task_id, revision_id) VALUES(11, 1);


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('addResultSolutionNotFound', '2023-01-01T10:00:00', '2023-01-01T10:30:00', TRUE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #12

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(12, 1);
INSERT INTO task_tariff_revisions(task_id, revision_id) VALUES(12, 1);
INSERT INTO task_contract_revisions(task_id, revision_id) VALUES(12, 1);


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('getTaskResultWithContractResult', '2023-01-01T10:00:00', '2023-01-01T10:30:00', TRUE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #13

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(13, 1);
INSERT INTO task_tariff_revisions(task_id, revision_id) VALUES(13, 1);
INSERT INTO task_contract_revisions(task_id, revision_id) VALUES(13, 1);


INSERT INTO task(name, date_time_start, date_time_end, read_only, created_date_time, update_date_time)
            VALUES('getTaskResultWithStorageResult', '2023-01-01T10:00:00', '2023-01-01T10:30:00', TRUE, '2023-12-23T17:00:00', '2023-12-23T17:00:00'); -- Task #14

INSERT INTO task_demand_revisions(task_id, revision_id) VALUES(14, 1);
INSERT INTO task_storage_revisions(task_id, revision_id) VALUES(14, 1);


------------------------------------------------------------------------ Task results
INSERT INTO task_result(task_id, result_status, created_date_time, update_date_time)
            VALUES(2, 1, '2023-12-24T14:00:00', '2023-12-24T17:00:00');  -- taskResult #1

INSERT INTO validation_message(task_result_id, level, object_type, object_name, object_id, object_revision, message)
            VALUES(1, 1, 0, 'contract1', 1, 1, 'test message');  -- validationMessage #1


INSERT INTO task_result(task_id, result_status, created_date_time, update_date_time)
            VALUES(9, 0, '2023-12-24T14:00:00', '2023-12-24T17:00:00');  -- taskResult #2


INSERT INTO task_result(task_id, result_status, created_date_time, update_date_time)
            VALUES(10, 1, '2023-12-23T17:01:00', '2023-12-23T17:01:00');  -- taskResult #3


INSERT INTO task_result(task_id, result_status, created_date_time, update_date_time)
            VALUES(11, 3, '2023-12-23T17:01:00', '2023-12-23T17:01:00');  -- taskResult #4

INSERT INTO validation_message(task_result_id, level, object_type, object_name, object_id, object_revision, message)
            VALUES(4, 3, 0, 'contract1', 1, 1, 'error message');  -- validationMessage #2


INSERT INTO task_result(task_id, result_status, created_date_time, update_date_time)
            VALUES(12, 1, '2023-12-23T17:01:00', '2023-12-23T17:01:00');  -- taskResult #5


INSERT INTO task_result(task_id, result_status, created_date_time, update_date_time, objective_function_value, relative_gap, elapsed_time, optimizer_message)
            VALUES(13, 5, '2023-12-23T17:01:00', '2023-12-23T17:01:00', 200.0, 0.01, 12.1, 'Optimal solution found');  -- taskResult #6

INSERT INTO contract_result(contract_revision_id, task_result_id)
            VALUES(1, 6);  -- ContractResult #1

INSERT INTO contract_result_value(contract_result_id, date_time_start, date_time_end, power, energy, cost)
            VALUES(1, '2023-01-01T10:00:00', '2023-01-01T10:15:00', 10.0, 2.5, 0.25); -- ContractResultValue #1

INSERT INTO contract_result_value(contract_result_id, date_time_start, date_time_end, power, energy, cost)
            VALUES(1, '2023-01-01T10:15:00', '2023-01-01T10:30:00', 20.0, 5.0, 0.5); -- ContractResultValue #2


INSERT INTO task_result(task_id, result_status, created_date_time, update_date_time, objective_function_value, relative_gap, elapsed_time, optimizer_message)
            VALUES(14, 5, '2023-12-23T17:01:00', '2023-12-23T17:01:00', 0.0, 0.01, 1.1, 'Optimal solution found');  -- taskResult #7

INSERT INTO storage_result(storage_revision_id, task_result_id)
            VALUES(1, 7);  -- StorageResult #1

INSERT INTO storage_result_value(storage_result_id, date_time_start, date_time_end, charge, discharge, energy, storage_mode)
            VALUES(1, '2023-01-01T10:00:00', '2023-01-01T10:15:00', 10.0, 0.0, 30, 1); -- StorageResultValue #1

INSERT INTO storage_result_value(storage_result_id, date_time_start, date_time_end, charge, discharge, energy, storage_mode)
            VALUES(1, '2023-01-01T10:15:00', '2023-01-01T10:30:00', 0.0, 5, 25, 2); -- StorageResultValue #2