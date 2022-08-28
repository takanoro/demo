create table transfer
(
    id       uuid default gen_random_uuid(),
    amount   numeric(16, 8),
    datetime timestamp,
    timezone varchar(10)
        check (datetime >= '2009-01-03 00:00:00'::timestamp)
);

create table outbox
(
    id         uuid,
    aggregate  varchar(64),
    message    text,
    created_at timestamp
);
