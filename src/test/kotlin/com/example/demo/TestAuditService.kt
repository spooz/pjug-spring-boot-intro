package com.example.demo

import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class TestAuditService : AuditService {
    private val actions: MutableMap<Instant, String> = ConcurrentHashMap()

    override fun log(action: String) {
        actions[Instant.now()] = action
    }

    fun logSize(): Int {
        return actions.size
    }

    fun clear() {
        actions.clear()
    }
}