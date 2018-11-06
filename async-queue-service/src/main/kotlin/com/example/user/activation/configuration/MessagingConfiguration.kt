package com.example.user.activation.configuration

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessagingConfiguration {

    companion object {
        const val queueName = "activation"
        const val logQueueName = "activationlog"
        const val fanoutExchangeName = "activation-fanout.exchange"
    }

    @Bean
    fun fanoutBindings(): Declarables {
        val fanoutQueue1 = Queue(queueName, false)
        val fanoutQueue2 = Queue(logQueueName, false)

        val fanoutExchange = FanoutExchange(fanoutExchangeName)

        return Declarables(listOf(
                fanoutQueue1,
                fanoutQueue2,
                fanoutExchange,
                BindingBuilder.bind(fanoutQueue1).to(fanoutExchange),
                BindingBuilder.bind(fanoutQueue2).to(fanoutExchange)))
    }

    @Bean
    fun amqpAdmin(connectionFactory: ConnectionFactory): AmqpAdmin {
        return RabbitAdmin(connectionFactory)
    }

    @Bean
    fun container(connectionFactory: ConnectionFactory, configurer: SimpleRabbitListenerContainerFactoryConfigurer): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        configurer.configure(factory, connectionFactory)
        return factory
    }
}