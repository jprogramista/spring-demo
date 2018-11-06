package com.example.user.activation.controller

import com.example.common.dto.ActivationMessageDto
import com.example.user.activation.service.ActivationTokenService
import com.example.user.activation.service.MessagingService
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class ActivationController(private val activationTokenService: ActivationTokenService, private val messagingService: MessagingService) {

    /*
    exposed for testing purposes - the other microservices will put messages on queue directly
     */
    @PostMapping("/activation-email")
    fun createActivationEmail(@RequestBody @Valid activationMessageDto: ActivationMessageDto, bindingResult: BindingResult) : ResponseEntity<Unit> {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build()
        }

        messagingService.sendActivationData(activationMessageDto)

        return ResponseEntity.ok(Unit)
    }

    @PostMapping("/activate")
    fun activateUser(@RequestParam userId: Long, @RequestParam token: String) : ResponseEntity<Boolean> {
        return ResponseEntity.ok(activationTokenService.activateUser(userId, token))
    }
}