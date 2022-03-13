CREATE TABLE if not exists patient (
    id uuid PRIMARY KEY,
    email varchar(255) NULL,
    phone_number varchar(255) not null,
    active BOOL default 't' not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    gender CHAR not null,
    birth_date DATE not null,
    avatar_url VARCHAR(255),
    created_by uuid not null,
    created_on TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_by uuid not null,
    updated_on TIMESTAMP not null,
    is_deleted BOOL default 'f' not null
    );

CREATE TABLE if not exists  practitioner (
    id uuid PRIMARY KEY,
    active BOOL default 't' not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(255) not null,
    gender CHAR not null,
    birth_date DATE not null,
    experience integer not null,
    avatar_url VARCHAR(255),
    created_by uuid not null,
    created_on TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_by uuid not null,
    updated_on TIMESTAMP not null,
    is_deleted BOOL default 'f' not null
    );
