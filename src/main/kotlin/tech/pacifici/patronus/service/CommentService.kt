package tech.pacifici.patronus.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import tech.pacifici.patronus.mapper.CommentMapper
import tech.pacifici.patronus.model.CommentRequest
import tech.pacifici.patronus.model.CommentResponse
import tech.pacifici.patronus.model.tables.records.CommentsRecord
import tech.pacifici.patronus.repository.CommentsRepository

@Service
class CommentService(
    private val commentRepository: CommentsRepository,
    private val mapper: CommentMapper,
) {
    @Transactional
    fun create(request: CommentRequest): CommentResponse {
        val savedComment: CommentsRecord = commentRepository.save(mapper.toCommentRecord(request))
        return mapper.toCommentResponse(savedComment)
    }
}
