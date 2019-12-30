package com.example.demo

import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

interface AuditService {
    fun log(action: String)
}

class InMemoryAuditService : AuditService {

    private val actions: MutableMap<Instant, String> = ConcurrentHashMap()

    override fun log(action: String) {
        actions[Instant.now()] = action
    }
}