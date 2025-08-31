package tech.pacifici.patronus.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import tech.pacifici.patronus.api.PatronusApi
import tech.pacifici.patronus.model.CommentRequest
import tech.pacifici.patronus.model.CommentResponse
import tech.pacifici.patronus.model.FriendshipRequest
import tech.pacifici.patronus.model.FriendshipResponse
import tech.pacifici.patronus.model.FriendshipUpdateRequest
import tech.pacifici.patronus.model.PostRequest
import tech.pacifici.patronus.model.PostResponse
import tech.pacifici.patronus.model.UserRequest
import tech.pacifici.patronus.model.UserResponse

class PatronusControllerApi : PatronusApi {

    override fun createComment(commentRequest: CommentRequest): ResponseEntity<CommentResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun createPost(postRequest: PostRequest): ResponseEntity<PostResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun createUsers(userRequest: UserRequest): ResponseEntity<UserResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun deletePostById(id: Long): ResponseEntity<Unit> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun deleteUserbyId(id: Long): ResponseEntity<Unit> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun getPostById(id: Long): ResponseEntity<PostResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun getPosts(): ResponseEntity<List<PostResponse>> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun getUsers(): ResponseEntity<List<UserResponse>> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun getUsersById(id: Long): ResponseEntity<UserResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun postFriendRequest(friendshipRequest: FriendshipRequest): ResponseEntity<FriendshipResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun updateFriendship(friendshipUpdateRequest: FriendshipUpdateRequest): ResponseEntity<FriendshipResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun updatePostById(id: Long, postRequest: PostRequest): ResponseEntity<PostResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    override fun updateUserById(id: Long, userRequest: UserRequest): ResponseEntity<UserResponse> {
        // TODO: Implement your business logic here
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

}