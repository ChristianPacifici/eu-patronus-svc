package tech.pacifici.patronus.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import tech.pacifici.patronus.exception.ResourceNotFoundException
import tech.pacifici.patronus.mapper.PostMapper
import tech.pacifici.patronus.model.PagedResponsePostResponse
import tech.pacifici.patronus.model.PostRequest
import tech.pacifici.patronus.model.PostResponse
import tech.pacifici.patronus.model.tables.records.PostsRecord
import tech.pacifici.patronus.repository.PostRepository
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository,
    private val postMapper: PostMapper,
) {
    fun findAll(
        page: Int,
        size: Int,
        sortField: String,
        sortDirection: String,
        userId: UUID?,
        sanitizedSearch: String?,
    ): PagedResponsePostResponse {
        val postRecords: List<PostsRecord> =
            postRepository.findAllPaginated(
                page,
                size,
                sortField,
                sortDirection,
                userId,
                sanitizedSearch,
            )

        val postResponses: List<PostResponse> =
            postMapper.toPostResponseList(postRecords)

        val totalElements: Long = postRepository.count(userId, sanitizedSearch)
        val totalPages: Int = ((totalElements + size - 1) / size).toInt()

        return PagedResponsePostResponse(
            content = postResponses,
            page = page,
            propertySize = size,
            totalElements = totalElements,
            totalPages = totalPages,
        )
    }

    @Transactional
    fun create(post: PostRequest): PostResponse {
        val postRecord = postMapper.toPostsRecord(post)
        return postMapper.toPostResponse(postRepository.save(postRecord))
    }

    @Transactional
    fun update(
        post: PostRequest,
        id: UUID,
    ): PostResponse {
        postRepository.findById(id) ?: throw NoSuchElementException("Post not found")
        val rec: PostsRecord = postMapper.toPostsRecord(post)
        rec.id = id
        return postMapper.toPostResponse(postRepository.update(rec))
    }

    @Transactional
    fun deleteById(id: UUID) {
        val deletedRows: Int = postRepository.deleteById(id)
        if (deletedRows == 0) throw ResourceNotFoundException("Post not found with ID: $id")
    }

    fun findById(id: UUID): PostResponse {
        val postRecord =
            postRepository.findById(id)
                ?: throw ResourceNotFoundException("Post not found")
        return postMapper.toPostResponse(postRecord)
    }
}
