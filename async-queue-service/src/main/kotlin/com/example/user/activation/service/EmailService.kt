package com.example.user.activation.service

import com.example.common.dto.ActivationMessageDto
import com.example.user.activation.model.ActivationToken
import org.springframework.stereotype.Service

@Service
class EmailService {

    fun sendEmail(activationMessageDto: ActivationMessageDto, activationToken: ActivationToken) {

    }

}