CREATE TABLE task_movable_demand_revisions (
    task_id BIGINT NOT NULL,
    revision_id BIGINT NOT NULL,
    CONSTRAINT pk_task_movable_demand_revisions PRIMARY KEY (task_id, revision_id)
);
ALTER TABLE task_movable_demand_revisions ADD CONSTRAINT fk_task_movable_demand_rev_on_task FOREIGN KEY (task_id) REFERENCES task (id);
ALTER TABLE task_movable_demand_revisions ADD CONSTRAINT fk_task_movable_demand_rev_on_movable_demand_revision FOREIGN KEY (revision_id) REFERENCES movable_demand_revision (id);
