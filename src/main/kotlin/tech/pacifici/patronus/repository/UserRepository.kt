package tech.pacifici.patronus.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import tech.pacifici.patronus.model.tables.Users
import tech.pacifici.patronus.model.tables.records.UsersRecord
import java.util.UUID

@Repository
class UserRepository(
    private val dsl: DSLContext,
) {
    fun findAll(): List<UsersRecord> = dsl.selectFrom(Users.USERS).fetch()

    fun findById(id: UUID): UsersRecord? =
        dsl
            .selectFrom(Users.USERS)
            .where(Users.USERS.ID.eq(id))
            .fetchOne()

    fun save(user: UsersRecord): UsersRecord =
        dsl
            .insertInto(Users.USERS)
            .set(Users.USERS.ID, user.id)
            .set(Users.USERS.USERNAME, user.username)
            .set(Users.USERS.EMAIL, user.email)
            .set(Users.USERS.PASSWORD, user.password)
            .set(Users.USERS.FIRST_NAME, user.firstName)
            .set(Users.USERS.LAST_NAME, user.lastName)
            .returning()
            .fetchOne()!!

    fun update(
        id: UUID,
        user: UsersRecord,
    ): Int =
        dsl
            .update(Users.USERS)
            .set(Users.USERS.USERNAME, user.username)
            .set(Users.USERS.EMAIL, user.email)
            .set(Users.USERS.PASSWORD, user.password)
            .set(Users.USERS.FIRST_NAME, user.firstName)
            .set(Users.USERS.LAST_NAME, user.lastName)
            .where(Users.USERS.ID.eq(id))
            .execute()

    fun deleteById(id: UUID): Int =
        dsl
            .deleteFrom(Users.USERS)
            .where(Users.USERS.ID.eq(id))
            .execute()
}
