package xyz.btc.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QueryApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<QueryApplication>(*args)
        }
    }
}
