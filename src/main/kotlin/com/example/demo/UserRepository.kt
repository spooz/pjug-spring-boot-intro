package com.example.demo

import org.springframework.data.repository.CrudRepository


interface UserRepository {
    fun save(user: UserDocument): UserDocument
    fun findById(id: String): UserDocument?
    fun findAll(): Iterable<UserDocument>
}

class AuditedUserRepository(private val mongoUserRepository: MongoUserRepository,
                            private val auditService: AuditService) : UserRepository {
    override fun save(user: UserDocument): UserDocument {
        auditService.log("save")
        return mongoUserRepository.save(user)
    }

    override fun findById(id: String): UserDocument? {
        auditService.log("findById")
        return mongoUserRepository.findById(id).orElse(null)
    }

    override fun findAll(): Iterable<UserDocument> {
        auditService.log("findAll")
        return mongoUserRepository.findAll()
    }
}

interface MongoUserRepository : CrudRepository<UserDocument, String>
data class UserDocument(var id: String,
                        val email: String)