package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@SpringBootApplication
class UsersApplication {
    fun main(args: Array<String>) {
        runApplication<UsersApplication>(*args)
    }
}

@RestController
class RestController(private val userRepository: UserRepository) {

    @PostMapping("/users", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun registerUser(request: UserRequest): UserId {
        val uuid = UUID.randomUUID().toString()
        userRepository.save(UserDocument(id = uuid, email = request.email))
        return UserId(uuid)
    }

    @GetMapping("/users/{id}", produces = [APPLICATION_JSON_VALUE])
    fun getUser(@PathVariable id: String): ResponseEntity<UserResponse> {
        return userRepository.findById(id)
                .map { UserResponse(it.id, it.email) }
                .map { ResponseEntity.ok(it) }
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
    }

    @GetMapping("/users", produces = [APPLICATION_JSON_VALUE])
    fun listUsers(): Collection<UserResponse> {
        return userRepository.findAll()
                .map { UserResponse(it.id, it.email) }
    }

    data class UserRequest(val email: String) //TODO: validation
    data class UserResponse(val id: String,
                            val email: String)

    data class UserId(val value: String)
}

interface UserRepository : CrudRepository<UserDocument, String>
data class UserDocument(var id: String,
                        val email: String)