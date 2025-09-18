package tech.pacifici.patronus.repository

import org.jooq.DSLContext
import tech.pacifici.patronus.model.tables.records.FriendshipsRecord
import tech.pacifici.patronus.model.tables.references.FRIENDSHIPS
import java.util.UUID

class FriendshipRepository(private val dsl: DSLContext) {

    fun save(friendShip: FriendshipsRecord): FriendshipsRecord =
        dsl.insertInto(FRIENDSHIPS)
            .set(FRIENDSHIPS.ID, UUID.randomUUID())
            .set(FRIENDSHIPS.USER_ID, friendShip.userId)
            .set(FRIENDSHIPS.FRIEND_ID, friendShip.friendId)
            .set(FRIENDSHIPS.STATUS, friendShip.status)
            .set(FRIENDSHIPS.CREATED_AT, friendShip.createdAt)
            .returning()
            .fetchOne()!!
}