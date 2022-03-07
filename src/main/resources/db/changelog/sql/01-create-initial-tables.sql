CREATE EXTENSION IF NOT EXISTS "uuid-ossp" with schema public;

SET search_path to healthcare_service_schema;

CREATE TABLE IF NOT EXISTS healthcare_service_schema.role
(
    id          uuid      default public.uuid_generate_v4() PRIMARY KEY,
    description VARCHAR(255),
    role_name   VARCHAR(255),
    created_by  uuid                                not null,
    created_on  TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_by  uuid,
    updated_on  TIMESTAMP,
    is_deleted  BOOL      default 'f'               not null
)
    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS healthcare_service_schema."user"
(
    id                      uuid      default public.uuid_generate_v4() PRIMARY KEY,
    account_non_expired     boolean                             NOT NULL,
    account_non_locked      boolean                             NOT NULL,
    authority               VARCHAR(255),
    credentials_non_expired boolean                             NOT NULL,
    enabled                 boolean                             NOT NULL,
    password                VARCHAR(255)                        NOT NULL,
    role_id                 uuid                                NOT NULL,
    username                VARCHAR(255)                        NOT NULL UNIQUE,
    created_by              uuid                                not null,
    created_on              TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_by              uuid,
    updated_on              TIMESTAMP,
    is_deleted              BOOL      default 'f'               not null
)
    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS healthcare_service_schema.user_role
(
    role_id uuid NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT user_role_pkey PRIMARY KEY (role_id, user_id),
    CONSTRAINT user_role_roleid_fkey FOREIGN KEY (role_id) REFERENCES healthcare_service_schema.role (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
) Tablespace pg_default;

--Insert sample data to role
INSERT INTO healthcare_service_schema.role(description, is_deleted, role_name,created_by,created_on)
VALUES  ( 'admin', FALSE, 'ADMIN',public.uuid_generate_v4(),NOW()),
        ( 'user', FALSE, 'USER',public.uuid_generate_v4(),NOW()),
        ( 'patient', FALSE, 'PATIENT',public.uuid_generate_v4(),NOW()),
        ( 'practitioner', FALSE, 'PRACTITIONER',public.uuid_generate_v4(),NOW());

--Insert sample data to user
INSERT INTO healthcare_service_schema."user" (account_non_expired,account_non_locked,authority,credentials_non_expired,enabled,
                                       "password","role_id",username,created_by,created_on)
    (SELECT true,true,'admin',true,true,'$2a$10$KL.hdvo5vj9jAGMBM2OLEeLxoSlpNAxCfA65dNZhCm9dZf.kRcHhi',id,'admin',public.uuid_generate_v4(),NOW()  from healthcare_service_schema.role where role_name ='ADMIN');
INSERT INTO healthcare_service_schema."user" (account_non_expired,account_non_locked,authority,credentials_non_expired,enabled,
                                       "password","role_id",username,created_by,created_on)
    (SELECT true,true,'user',true,true,'$2a$10$Ws.gp4aAmX07DcWTrYl1reuUYmabPdkw86Y2Uh6/f9lOUNgOP5gGu',id,'user',public.uuid_generate_v4(),NOW()  from healthcare_service_schema.role where role_name ='USER');

--Insert sample data to user_role
INSERT INTO healthcare_service_schema.user_role (role_id,user_id)
    (SELECT role_id ,id
     from healthcare_service_schema.user);

