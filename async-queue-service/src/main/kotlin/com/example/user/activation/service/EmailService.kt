package com.example.user.activation.service

import com.example.common.dto.ActivationMessageDto
import com.example.user.activation.model.ActivationToken
import org.springframework.context.MessageSource
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmailService(private val mailSender: MailSender, private val messageSource: MessageSource) {

    companion object {
        const val emailFromKey = "email.from"
        const val emailActivationSubjectKey = "email.activation.subject"
        const val emailActivationBodyKey = "email.activation.body"
    }

    fun sendEmail(activationMessageDto: ActivationMessageDto, activationToken: ActivationToken) {
        val simpleMailMessage = SimpleMailMessage()
        simpleMailMessage.setFrom(messageSource.getMessage(emailFromKey, arrayOf(), "key: $emailFromKey", Locale.ENGLISH)!!)
        simpleMailMessage.setTo(activationMessageDto.email)
        simpleMailMessage.setSubject(messageSource.getMessage(emailActivationSubjectKey, arrayOf(), "key: $emailActivationSubjectKey", Locale.ENGLISH)!!)
        val link = "http://localhost:8080/activate?userId=${activationMessageDto.userId}&token=${activationToken.token}"
        simpleMailMessage.setText(messageSource.getMessage(emailActivationBodyKey, arrayOf(link), "key: $emailActivationBodyKey", Locale.ENGLISH)!!)
        mailSender.send(simpleMailMessage)
    }

}