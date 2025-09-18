package tech.pacifici.patronus.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import tech.pacifici.patronus.model.tables.records.PostsRecord
import tech.pacifici.patronus.repository.PostRepository
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository,
) {
    fun getAll(): List<PostsRecord> = postRepository.findAll()

    fun getById(id: UUID): PostsRecord? = postRepository.findById(id)

    @Transactional
    fun create(post: PostsRecord): PostsRecord {
        if (post.content!!.isBlank()) throw IllegalArgumentException("Content cannot be empty")
        return postRepository.save(post)
    }

    @Transactional
    fun update(post: PostsRecord): PostsRecord {
        if (post.id == null) throw IllegalArgumentException("ID required for update")
        postRepository.findById(post.id!!) ?: throw NoSuchElementException("Post not found")
        return postRepository.update(post)
    }

    @Transactional
    fun deleteById(id: UUID) {
        postRepository.deleteById(id)
    }
}
