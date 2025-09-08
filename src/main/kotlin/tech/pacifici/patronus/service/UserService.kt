import org.springframework.stereotype.Service
import tech.pacifici.patronus.model.tables.records.UsersRecord

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<UserDTO> = userRepository.findAll()
        .map { it.toDTO() }

    fun getUserById(id: Long): UserDTO? = userRepository.findById(id)?.toDTO()

    fun createUser(userDTO: UserDTO): UserDTO {
        val record = userDTO.toRecord()
        return userRepository.save(record).toDTO()
    }

    fun updateUser(id: Long, userDTO: UserDTO): UserDTO? {
        val record = userDTO.toRecord()
        userRepository.update(id, record)
        return userRepository.findById(id)?.toDTO()
    }

    fun deleteUser(id: Long): Boolean = userRepository.deleteById(id) > 0

    // Extension functions for mapping
    private fun UsersRecord.toDTO(): UserDTO = UserDTO(
        id = this.id,
        username = this.username,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        password = this.password
    )

    private fun UserDTO.toRecord(): UsersRecord = UsersRecord(
        id = this.id,
        username = this.username,
        email = this.email,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName
    )
}

data class UserDTO(
    val id: Long? = null,
    val username: String?,
    val email: String?,
    val password: String?,
    val firstName: String?,
    val lastName: String?
)
