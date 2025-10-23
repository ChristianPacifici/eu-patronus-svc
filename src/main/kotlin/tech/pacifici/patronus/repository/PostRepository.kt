package tech.pacifici.patronus.repository

import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.SortField
import org.jooq.impl.DSL
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

    fun findAllPaginated(
        page: Int,
        size: Int,
        sortField: String?,
        sortDirection: String?,
        userId: UUID?,
        search: String?,
    ): List<PostsRecord> {
        val sort: SortField<*> =
            when (sortField?.lowercase()) {
                "content" -> if (sortDirection?.lowercase() == "desc") POSTS.CONTENT.desc() else POSTS.CONTENT.asc()
                "createdat", "created_at" -> if (sortDirection?.lowercase() == "desc") POSTS.CREATED_AT.desc() else POSTS.CREATED_AT.asc()
                else -> POSTS.CREATED_AT.desc() // Default sorting
            }

        val conditions = mutableListOf<Condition>()
        userId?.let { conditions.add(POSTS.USER_ID.eq(it)) }
        search?.let { conditions.add(POSTS.CONTENT.like("%${it.lowercase()}%")) }

        // Combine conditions with AND
        val combinedCondition =
            if (conditions.isNotEmpty()) {
                conditions.reduce { acc, condition -> acc.and(condition) }
            } else {
                DSL.noCondition()
            }

        return dsl
            .selectFrom(POSTS)
            .where(combinedCondition)
            .orderBy(sort)
            .offset(page * size)
            .limit(size)
            .fetch()
    }

    fun count(
        userId: UUID?,
        search: String?,
    ): Long =
        dsl
            .selectCount()
            .from(POSTS)
            .where(
                when {
                    userId != null && search != null -> POSTS.USER_ID.eq(userId).and(POSTS.CONTENT.like("%${search.lowercase()}%"))
                    userId != null -> POSTS.USER_ID.eq(userId)
                    search != null -> POSTS.CONTENT.like("%${search.lowercase()}%")
                    else -> DSL.noCondition()
                },
            ).fetchOne(0, Long::class.java)!!

    fun findById(id: UUID): PostsRecord? =
        dsl
            .selectFrom(POSTS)
            .where(POSTS.ID.eq(id))
            .fetchOne()

    fun save(post: PostsRecord): PostsRecord =
        dsl
            .insertInto(Posts.POSTS)
            .set(POSTS.ID, UUID.randomUUID())
            .set(POSTS.USER_ID, post.userId)
            .set(POSTS.CONTENT, post.content)
            .set(POSTS.CREATED_AT, post.createdAt)
            .set(POSTS.UPDATED_AT, post.updatedAt)
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
