package xyz.btc.demo.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import xyz.btc.demo.mapper.TransferMapper
import xyz.btc.demo.model.SaveTransferCommand
import xyz.btc.demo.service.TransferService


@RestController
class CommandController(
    private val transferService: TransferService,
    private val mapper: TransferMapper
) {

    @PostMapping("transfers")
    suspend fun saveTransfer(@Valid @RequestBody command: SaveTransferCommand) =
        transferService.receiveTransfer(mapper.map(command))

}
