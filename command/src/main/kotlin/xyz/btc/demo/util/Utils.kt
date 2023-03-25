package xyz.btc.demo.util

import io.r2dbc.spi.Readable
import io.r2dbc.spi.Row
import java.time.ZoneId

inline fun <reified T> Row.getK(name: String): T = this.get(name, T::class.java)!!

inline fun <reified T> Readable.getK(name: String): T = this.get(name, T::class.java)!!

object Consts {
    val UTC_ZONE_ID: ZoneId = ZoneId.of("Z")
}