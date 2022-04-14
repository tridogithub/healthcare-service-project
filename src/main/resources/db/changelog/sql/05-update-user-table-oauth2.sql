SET search_path to healthcare_service_schema;

ALTER TABLE healthcare_service_schema."user"
    ADD COLUMN provider    varchar(255) NOT NULL default 'LOCAL',
    ADD COLUMN provider_id varchar(255),
    ALTER COLUMN password DROP NOT NULL ;
