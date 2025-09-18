import org.springframework.stereotype.Service
import tech.pacifici.patronus.model.UserRequest
import tech.pacifici.patronus.model.UserResponse
import tech.pacifici.patronus.model.tables.records.UsersRecord
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun getAllUsers(): List<UserDTO> =
        userRepository
            .findAll()
            .map { it.toDTO() }

    fun getUserById(id: UUID): UserDTO? = userRepository.findById(id)?.toDTO()

    fun createUser(userDTO: UserRequest): UserDTO {
        val record = userDTO.toRecord()
        return userRepository.save(record).toDTO()
    }

    fun updateUser(
        id: UUID,
        userDTO: UserDTO,
    ): UserDTO? {
        val record = userDTO.toRecord()
        userRepository.update(id, record)
        return userRepository.findById(id)?.toDTO()
    }

    fun deleteUser(id: UUID): Boolean = userRepository.deleteById(id) > 0

    // Extension functions for mapping
    private fun UsersRecord.toDTO(): UserDTO =
        UserDTO(
            id = this.id,
            username = this.username,
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName,
            password = this.password,
        )
}

data class UserDTO(
    val id: UUID?,
    val username: String?,
    val email: String?,
    val password: String?,
    val firstName: String?,
    val lastName: String?,
)

fun UserDTO.toUserResponse(): UserResponse =
    UserResponse(
        id = this.id,
        username = this.username,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        createdAt = null,
        updatedAt = null,
    )

fun UserDTO.toRecord(): UsersRecord =
    UsersRecord(
        id = this.id,
        username = this.username,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        createdAt = null,
        updatedAt = null,
    )

fun UserRequest.toRecord(): UsersRecord =
    UsersRecord(
        id = UUID.randomUUID(),
        username = this.username,
        email = this.email,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
    )
