package tech.pacifici.patronus.dto

import tech.pacifici.patronus.model.UserRequest
import tech.pacifici.patronus.model.UserResponse
import tech.pacifici.patronus.model.tables.records.UsersRecord
import java.util.UUID

data class UserDTO(
    val id: UUID?,
    val username: String?,
    val email: String?,
    val password: String?,
    val firstName: String?,
    val lastName: String?,
)

fun UserDTO.toUserResponse(): UserResponse =
    UserResponse(
        id = this.id,
        username = this.username,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        createdAt = null,
        updatedAt = null,
    )

fun UserDTO.toRecord(): UsersRecord =
    UsersRecord(
        id = this.id,
        username = this.username,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        createdAt = null,
        updatedAt = null,
    )

fun UserRequest.toRecord(): UsersRecord =
    UsersRecord(
        id = UUID.randomUUID(),
        username = this.username,
        email = this.email,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
    )
