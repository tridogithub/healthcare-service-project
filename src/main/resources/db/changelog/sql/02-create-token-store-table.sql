set search_path to healthcare_service_schema;
CREATE table if not exists healthcare_service_schema.user_refresh_token(
    username                    VARCHAR(255)    NOT NULL PRIMARY KEY,
    refresh_token_id            uuid    NOT NULL,
    refresh_token               bytea   NOT NULL,
    access_token_id             uuid    NOT NULL,
    refresh_token_expiration    integer NOT NULL
) tablespace pg_default;