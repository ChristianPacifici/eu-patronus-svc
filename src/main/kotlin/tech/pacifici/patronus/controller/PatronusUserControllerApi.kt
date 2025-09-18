package tech.pacifici.patronus.controller

import UserService
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tech.pacifici.patronus.api.PatronusUserManagementApi
import tech.pacifici.patronus.model.FriendshipRequest
import tech.pacifici.patronus.model.FriendshipResponse
import tech.pacifici.patronus.model.FriendshipUpdateRequest
import tech.pacifici.patronus.model.UserRequest
import tech.pacifici.patronus.model.UserResponse
import toUserResponse

@RestController
class PatronusUserControllerApi(
    private val userService: UserService,
) : PatronusUserManagementApi {
    /**
     * Creates a new user.
     *
     * @param userRequest The request body containing the details of the user to create.
     * @return A [ResponseEntity] containing the created [UserResponse] and HTTP status.
     */
    override fun createUsers(userRequest: UserRequest): ResponseEntity<UserResponse> {
        val userResponse = userService.createUser(userRequest).toUserResponse()
        return ResponseEntity(userResponse, HttpStatus.CREATED)
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return A [ResponseEntity] with HTTP status indicating the result of the operation.
     */
    override fun deleteUserbyId(id: java.util.UUID): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    /**
     * Retrieves all users.
     *
     * @return A [ResponseEntity] containing a list of [UserResponse] and HTTP status.
     */
    override fun getUsers(): ResponseEntity<List<UserResponse>> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A [ResponseEntity] containing the [UserResponse] and HTTP status.
     */
    override fun getUsersById(id: java.util.UUID): ResponseEntity<UserResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    /**
     * Updates a user by their ID.
     *
     * @param id The ID of the user to update.
     * @param userRequest The request body containing the updated details of the user.
     * @return A [ResponseEntity] containing the updated [UserResponse] and HTTP status.
     */
    override fun updateUserById(
        id: java.util.UUID,
        userRequest: UserRequest,
    ): ResponseEntity<UserResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    /**
     * Sends a friendship request.
     *
     * @param friendshipRequest The request body containing the details of the friendship request.
     * @return A [ResponseEntity] containing the [FriendshipResponse] and HTTP status.
     */
    override fun postFriendRequest(@Parameter(description = "", required = true) @PathVariable("id") id: java.util.UUID,@Parameter(description = "", required = true) @Valid @RequestBody friendshipRequest: FriendshipRequest): ResponseEntity<FriendshipResponse> {
        //TODO: implement the logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    /**
     * Updates the status of a friendship.
     *
     * @param friendshipUpdateRequest The request body containing the details of the friendship update.
     * @return A [ResponseEntity] containing the updated [FriendshipResponse] and HTTP status.
     */
    override fun updateFriendship(friendshipUpdateRequest: FriendshipUpdateRequest): ResponseEntity<FriendshipResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
