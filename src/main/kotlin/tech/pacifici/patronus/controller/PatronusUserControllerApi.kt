package tech.pacifici.patronus.controller

import UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import tech.pacifici.patronus.api.PatronusUserManagementApi
import tech.pacifici.patronus.model.FriendshipRequest
import tech.pacifici.patronus.model.FriendshipResponse
import tech.pacifici.patronus.model.FriendshipUpdateRequest
import tech.pacifici.patronus.model.UserRequest
import tech.pacifici.patronus.model.UserResponse
import toUserResponse
import java.util.UUID

/**
 * Controller for managing user-related operations in the Patronus Social Network API.
 * Implements the [PatronusUserManagementApi] interface to handle user creation,
 * retrieval, updates, deletion, and friendship management.
 */
@RestController
class PatronusUserControllerApi(
    private val userService: UserService,
) : PatronusUserManagementApi {
    /**
     * Creates a new user in the Patronus platform.
     *
     * @param xRequestId The unique identifier for the request.
     * @param xCorrelationId The identifier for correlating requests across services.
     * @param userRequest The request body containing user details (username, email, password, firstName, lastName).
     * @return A [ResponseEntity] containing the created [UserResponse] with HTTP status 201 (Created).
     * @throws RuntimeException If user creation fails due to invalid input or server issues.
     */
    override fun createUsers(
        xRequestId: UUID,
        xCorrelationId: UUID,
        userRequest: UserRequest,
    ): ResponseEntity<UserResponse> {
        val userResponse = userService.createUser(userRequest).toUserResponse()
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse)
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id The UUID of the user to delete.
     * @param xRequestId The unique identifier for the request.
     * @param xCorrelationId The identifier for correlating requests across services.
     * @return A [ResponseEntity] with HTTP status 204 (No Content) on success.
     * @throws RuntimeException If the user does not exist or deletion fails.
     */
    override fun deleteUserbyId(
        id: UUID,
        xRequestId: UUID,
        xCorrelationId: UUID,
    ): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    /**
     * Retrieves a list of all users in the Patronus platform.
     *
     * @param xRequestId The unique identifier for the request.
     * @param xCorrelationId The identifier for correlating requests across services.
     * @return A [ResponseEntity] containing a list of [UserResponse] with HTTP status 200 (OK).
     * @throws RuntimeException If retrieval fails due to server issues.
     */
    override fun getUsers(
        xRequestId: UUID,
        xCorrelationId: UUID,
    ): ResponseEntity<List<UserResponse>> {
        // TODO: Implement your business logic here
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build()
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The UUID of the user to retrieve.
     * @param xRequestId The unique identifier for the request.
     * @param xCorrelationId The identifier for correlating requests across services.
     * @return A [ResponseEntity] containing the [UserResponse] with HTTP status 200 (OK).
     * @throws RuntimeException If the user is not found or retrieval fails.
     */
    override fun getUsersById(
        id: UUID,
        xRequestId: UUID,
        xCorrelationId: UUID,
    ): ResponseEntity<UserResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build()
    }

    /**
     * Updates an existing user by their unique identifier.
     *
     * @param id The UUID of the user to update.
     * @param xRequestId The unique identifier for the request.
     * @param xCorrelationId The identifier for correlating requests across services.
     * @param userRequest The request body containing updated user details.
     * @return A [ResponseEntity] containing the updated [UserResponse] with HTTP status 200 (OK).
     * @throws RuntimeException If the user is not found or update fails.
     */
    override fun updateUserById(
        id: UUID,
        xRequestId: UUID,
        xCorrelationId: UUID,
        userRequest: UserRequest,
    ): ResponseEntity<UserResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build()
    }

    /**
     * Sends a friend request from one user to another.
     *
     * @param id The UUID of the user sending the friend request.
     * @param xRequestId The unique identifier for the request.
     * @param xCorrelationId The identifier for correlating requests across services.
     * @param friendshipRequest The request body containing the friend request details (userId, friendId).
     * @return A [ResponseEntity] containing the [FriendshipResponse] with HTTP status 201 (Created).
     * @throws RuntimeException If the friend request fails due to invalid input or server issues.
     */
    override fun postFriendRequest(
        id: UUID,
        xRequestId: UUID,
        xCorrelationId: UUID,
        friendshipRequest: FriendshipRequest,
    ): ResponseEntity<FriendshipResponse> {
        // TODO: implement the logic here
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build()
    }

    /**
     * Updates the status of an existing friendship (e.g., accept or reject).
     *
     * @param id The UUID of the user whose friendship status is being updated.
     * @param xRequestId The unique identifier for the request.
     * @param xCorrelationId The identifier for correlating requests across services.
     * @param friendshipUpdateRequest The request body containing the updated friendship details (userId, friendId, status).
     * @return A [ResponseEntity] containing the updated [FriendshipResponse] with HTTP status 200 (OK).
     * @throws RuntimeException If the friendship is not found or update fails.
     */
    override fun updateFriendship(
        id: UUID,
        xRequestId: UUID,
        xCorrelationId: UUID,
        friendshipUpdateRequest: FriendshipUpdateRequest,
    ): ResponseEntity<FriendshipResponse> {
        // TODO: implement the logic here
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build()
    }
}
