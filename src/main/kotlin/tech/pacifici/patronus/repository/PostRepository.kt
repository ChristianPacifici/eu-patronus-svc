package tech.pacifici.patronus.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import tech.pacifici.patronus.model.tables.Posts
import tech.pacifici.patronus.model.tables.Posts.Companion.POSTS
import tech.pacifici.patronus.model.tables.records.PostsRecord
import java.util.UUID

@Repository
class PostRepository(
    private val dsl: DSLContext,
) {
    fun findAll(): List<PostsRecord> =
        dsl
            .selectFrom(POSTS)
            .fetch()

    fun findById(id: UUID): PostsRecord? =
        dsl
            .selectFrom(POSTS)
            .where(POSTS.ID.eq(id))
            .fetchOne()

    fun save(Post: PostsRecord): PostsRecord =
        dsl
            .insertInto(Posts.POSTS)
            .set(POSTS.ID, UUID.randomUUID())
            .set(POSTS.USER_ID, Post.userId)
            .set(POSTS.CONTENT, Post.content)
            .set(POSTS.CREATED_AT, Post.createdAt)
            .set(POSTS.UPDATED_AT, Post.updatedAt)
            .returning()
            .fetchOne()!!

    fun update(post: PostsRecord): PostsRecord {
        dsl
            .update(POSTS)
            .set(POSTS.CONTENT, post.content)
            .where(POSTS.ID.eq(post.id))
            .execute()
        return post
    }

    fun deleteById(id: UUID): Int =
        dsl
            .deleteFrom(POSTS)
            .where(POSTS.ID.eq(id))
            .execute()
}
