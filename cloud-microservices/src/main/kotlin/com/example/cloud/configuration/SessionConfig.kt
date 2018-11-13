package com.example.cloud.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@Configuration
@EnableRedisHttpSession
class SessionConfig : AbstractHttpSessionApplicationInitializer()
