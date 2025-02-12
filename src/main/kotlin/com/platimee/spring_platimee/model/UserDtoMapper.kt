package com.platimee.spring_platimee.model

object UserDtoMapper {

    // Convert UserDTO (input) to User entity
    fun toEntity(dto: UserCreateDTO): User {
        return User(
            username = dto.username,
            email = dto.email,
            firstName = dto.firstName,
            lastName = dto.lastName,
            password = dto.password
        )
    }

    // Convert User entity to UserResponseDTO (output)
    fun toResponseDto(user: User): UserResponseDTO {
        return UserResponseDTO(
            userId = user.userId!!,
            username = user.username,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName
        )
    }
}
