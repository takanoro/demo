## Infrastructure preparation

* Install `docker`, `docker-compose`
* Exec command `docker-compose -f docker/docker-compose.yml up -d` from project root
* Exec command `./gradlew command:bootRun > command.log 2>&1 &` (to cancel background process simply exec `fg` and `ctrl+c`)
* Exec command `./gradlew query:bootRun > query.log 2>&1 &` (to cancel background process simply exec `fg` and `ctrl+c`)

## Talking with a service

* Save transfer

```shell
curl -X POST localhost:1111/transfers \
    -H 'Content-Type: application/json' \
    -d '{"amount": "1.11","datetime":"2009-01-04T19:30:00+00:00"}'
```

* Get transfer history by the end of each hour

```shell
curl -X POST localhost:2222/transfers/history \
    -H 'Content-Type: application/json' \
    -d '{"startDatetime":"2009-01-04T11:52:32.988+00:00","endDatetime":"2009-01-05T20:52:32.988+00:00"}'
```