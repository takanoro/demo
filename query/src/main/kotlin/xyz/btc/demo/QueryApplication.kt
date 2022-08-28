package xyz.btc.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QueryApplication

fun main(args: Array<String>) {
	runApplication<QueryApplication>(*args)
}
