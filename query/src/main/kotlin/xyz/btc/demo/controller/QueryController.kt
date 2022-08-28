package xyz.btc.demo.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import xyz.btc.demo.mapper.TransferMapper
import xyz.btc.demo.model.GetTransfersQuery
import xyz.btc.demo.service.TransferService
import javax.validation.Valid

@RestController
class QueryController(
    private val transferService: TransferService,
    private val mapper: TransferMapper
) {

    @PostMapping("transfers/history")
    suspend fun getHistory(@Valid @RequestBody command: GetTransfersQuery) =
        transferService.calculateAmountForRange(command.startDatetime!!, command.endDatetime!!)
            .map { entity -> mapper.map(entity) }

}
