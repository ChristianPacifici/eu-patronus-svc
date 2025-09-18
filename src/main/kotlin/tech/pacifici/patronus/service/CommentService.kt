package tech.pacifici.patronus.service

import org.springframework.stereotype.Service
import tech.pacifici.patronus.repository.CommentsRepository

@Service
class CommentService(
    private val commentRepository: CommentsRepository,
)
