CREATE table if not exists verification_token (
    id uuid not null,
    token varchar(255) not null,
    user_id uuid not null,
    expiry_date timestamp not null
)