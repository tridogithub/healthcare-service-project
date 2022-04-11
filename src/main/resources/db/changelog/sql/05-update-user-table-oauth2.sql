SET search_path to healthcare_service_schema;

ALTER TABLE healthcare_service_schema."user"
    ADD COLUMN provider   varchar(255) NOT NULL,
    ADD COLUMN providerId varchar(255) NOT NULL;