create materialized view transfer_amount_hour_view
as
select gs.datetime, sum(coalesce(thv.amount, 0)) over (order by gs.datetime) as amount
from (select datetime
      from generate_series('2009-01-02 23:00:00'::timestamp, (current_date + time '23:00' + interval '1 day'),
                           interval '1 hour') gs(datetime)) gs
         left join transfer_hour_view thv ON gs.datetime = thv.datetime
group by gs.datetime, thv.amount
order by datetime;

create unique index transfer_amount_hour_view_idx on transfer_amount_hour_view (datetime);

create or replace procedure refresh_transfer_amount_materialized_view()
    language sql
as
$$
refresh materialized view transfer_amount_hour_view;
$$;
