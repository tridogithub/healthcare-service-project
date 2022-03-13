CREATE table if not exists appointment (
    id uuid not null,
    patient_id uuid not null,
    practitioner_id uuid not null ,
    status varchar(255),
    start_time timestamp not null,
    minutes_duration int not null,
    patient_instruction varchar(1024) null,
    created_by uuid not null,
    created_on TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_by uuid not null,
    updated_on TIMESTAMP not null,
    is_deleted BOOL default 'f' not null
)