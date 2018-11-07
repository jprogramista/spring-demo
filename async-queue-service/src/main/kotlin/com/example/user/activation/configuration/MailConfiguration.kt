package com.example.user.activation.configuration

import org.springframework.context.annotation.Configuration
import org.apache.tomcat.jni.SSL.setPassword
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.JavaMailSender



@Configuration
class MailConfiguration(
        @Value("\${mailSender.host}") val host : String,
        @Value("\${mailSender.port}") val port : Int,
        @Value("\${mailSender.username}") val username : String,
        @Value("\${mailSender.password}") val password : String,
        @Value("\${mailSender.props.mail.transport.protocol}") val protocol : String,
        @Value("\${mailSender.props.mail.smtp.auth}") val auth : String,
        @Value("\${mailSender.props.mail.smtp.starttls.enable}") val enable : String,
        @Value("\${mailSender.props.mail.debug}") val debug : String) {

    @Bean
    fun getJavaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port

        mailSender.username = username
        mailSender.password = password

        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = protocol
        props["mail.smtp.auth"] = auth
        props["mail.smtp.starttls.enable"] = enable
        props["mail.debug"] = debug

        return mailSender
    }
}