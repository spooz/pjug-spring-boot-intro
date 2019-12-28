package com.example.demo

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(classes = [UsersApplication::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersApplicationTests {

    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var userRepository: MongoUserRepository
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup(wac: WebApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
        userRepository.deleteAll()
    }

    @Test
    fun shouldRegisterUser() {
        //when
        mockMvc.post("/users") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(RegisterUserRequest("test@test.com"))
            accept = MediaType.APPLICATION_JSON
        }

        //then
         .andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { exists() }
        }
    }

    data class RegisterUserRequest(val email: String)

    @Test
    fun shouldGetUserById() {
        //given
        val userId = "test-id"
        userRepository.save(UserDocument(userId, "test1@test.com"))

        //when
        mockMvc.get("/users/$userId") {
            accept = MediaType.APPLICATION_JSON
        }

         //then
         .andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { value(userId) }
            jsonPath("$.email") { value("test1@test.com") }
        }
    }

    @Test
    fun shouldReturn404WhenUserDoesNotExistsWithId() {
        //given
        userRepository.deleteAll()

        //when
        mockMvc.get("/users/1") {
            accept = MediaType.APPLICATION_JSON
        }

        //then
         .andExpect {
            status { isNotFound }
        }
    }

    @Test
    fun shouldLoadUsers() {
        //given
        userRepository.save(UserDocument("test-id-1", "test1@test.com"))
        userRepository.save(UserDocument("test-id-2", "test2@test.com"))

        //when
        mockMvc.get("/users") {
            accept = MediaType.APPLICATION_JSON
        }

        //then
          .andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$[0].id") { value("test-id-1") }
            jsonPath("$[0].email") { value("test1@test.com") }
            jsonPath("$[1].id") { value("test-id-2") }
            jsonPath("$[1].email") { value("test2@test.com") }
        }

    }
}
