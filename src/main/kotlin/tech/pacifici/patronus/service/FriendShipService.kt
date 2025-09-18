package tech.pacifici.patronus.service

import jakarta.transaction.Transactional
import tech.pacifici.patronus.model.tables.records.FriendshipsRecord
import tech.pacifici.patronus.repository.FriendshipRepository

class FriendShipService(
    private val friendshipRepository: FriendshipRepository,
) {
    @Transactional
    fun sendRequest(friendship: FriendshipsRecord): FriendshipsRecord {
        if (friendship.userId == friendship.friendId) throw IllegalArgumentException("Cannot friend yourself")
        return friendshipRepository.save(friendship)
    }

    @Transactional
    fun updateStatus(friendship: FriendshipsRecord): FriendshipsRecord {
        if (friendship.id == null) throw IllegalArgumentException("ID required for update")
        return friendshipRepository.save(friendship)
    }
}
