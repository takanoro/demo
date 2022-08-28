create table inbox
(
    id         uuid primary key,
    aggregate  varchar(64),
    message    text,
    created_at timestamp,
    proceed    boolean default false
);

create table transfer_hour_view
(
    datetime timestamp,
    amount   numeric(16, 8)
);

insert into transfer_hour_view(datetime, amount)
values ('2009-01-02 23:00:00', 1000.0);

create unique index on transfer_hour_view (datetime);
