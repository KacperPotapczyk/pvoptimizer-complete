CREATE TABLE task_production_revisions (
    task_id BIGINT NOT NULL,
    revision_id BIGINT NOT NULL,
    CONSTRAINT pk_task_production_revisions PRIMARY KEY (task_id, revision_id)
);
ALTER TABLE task_production_revisions ADD CONSTRAINT fk_task_production_rev_on_task FOREIGN KEY (task_id) REFERENCES task (id);
ALTER TABLE task_production_revisions ADD CONSTRAINT fk_task_production_rev_on_production_revision FOREIGN KEY (revision_id) REFERENCES production_revision (id);
