CREATE TABLE task_storage_revisions (
    task_id BIGINT NOT NULL,
    revision_id BIGINT NOT NULL,
    CONSTRAINT pk_task_storage_revisions PRIMARY KEY (revision_id, task_id)
);
ALTER TABLE task_storage_revisions ADD CONSTRAINT fk_task_storage_rev_on_task FOREIGN KEY (task_id) REFERENCES task (id);
ALTER TABLE task_storage_revisions ADD CONSTRAINT fk_task_storage_rev_on_storage_revision FOREIGN KEY (revision_id) REFERENCES storage_revision (id);
