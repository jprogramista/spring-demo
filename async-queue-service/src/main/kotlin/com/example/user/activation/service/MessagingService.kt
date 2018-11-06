package com.example.user.activation.service

import com.example.common.dto.ActivationMessageDto
import com.example.user.activation.configuration.MessagingConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class MessagingService(private val activationTokenService: ActivationTokenService,
                       private val emailService: EmailService,
                       private val rabbitTemplate: RabbitTemplate) {

    val logger: Logger = LoggerFactory.getLogger(MessagingService::class.java)

    fun sendActivationData(activationMessageDto: ActivationMessageDto) {
        rabbitTemplate.convertAndSend(MessagingConfiguration.fanoutExchangeName, "", activationMessageDto)
    }

    @RabbitListener(queues = [ MessagingConfiguration.logQueueName ])
    fun eventLog(activationMessageDto: ActivationMessageDto) {
        logger.info(activationMessageDto.toString())
    }

    @RabbitListener(queues = [ MessagingConfiguration.queueName ])
    fun receiveMessage(activationMessageDto: ActivationMessageDto) {
        val activationToken = activationTokenService.createActivationToken(activationMessageDto)

        emailService.sendEmail(activationMessageDto, activationToken)
    }
}