package com.example.demo.configuration

import org.springframework.context.annotation.Configuration


@Configuration
class MessagingConfiguration {

    companion object {
        const val topicExchangeName = "activation-exchange"
        const val queueName = "activation"
        const val fanoutExchangeName = "activation-fanout.exchange"
    }

}