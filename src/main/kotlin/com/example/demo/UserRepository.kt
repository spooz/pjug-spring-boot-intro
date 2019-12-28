package com.example.demo

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserDocument, String>
data class UserDocument(var id: String,
                        val email: String)