package com.platimee.spring_platimee.lists.model

object UserListDTOMapper {

    fun toResponseDTO(userList: UserList): UserListResponseDTO {
        return UserListResponseDTO(
            listId = userList.listId ?: 0L,
            name = userList.name,
            description = userList.description,
            userId = userList.user.userId ?: 0L,
            animeIds = userList.anime.mapNotNull { it.animeId }
        )
    }
}
