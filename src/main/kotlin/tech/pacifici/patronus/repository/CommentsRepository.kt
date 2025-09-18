package tech.pacifici.patronus.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import tech.pacifici.patronus.model.tables.records.CommentsRecord
import tech.pacifici.patronus.model.tables.references.COMMENTS
import java.util.UUID

@Repository
class CommentsRepository(
    private val dsl: DSLContext,
) {
    fun save(comment: CommentsRecord): CommentsRecord =
        dsl
            .insertInto(COMMENTS)
            .set(COMMENTS.ID, UUID.randomUUID())
            .set(COMMENTS.POST_ID, comment.postId)
            .set(COMMENTS.USER_ID, comment.userId)
            .set(COMMENTS.CONTENT, comment.content)
            .set(COMMENTS.CREATED_AT, comment.createdAt)
            .returning()
            .fetchOne()!!
}
