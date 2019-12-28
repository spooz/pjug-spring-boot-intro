package com.example.demo

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestConfiguration {

    @Bean
    @Primary
    fun testAuditService() : AuditService {
        return TestAuditService()
    }
}