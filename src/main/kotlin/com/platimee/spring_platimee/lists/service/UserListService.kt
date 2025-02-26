package com.platimee.spring_platimee.lists.service

import com.platimee.spring_platimee.anime.repository.AnimeRepository
import com.platimee.spring_platimee.lists.model.UserList
import com.platimee.spring_platimee.lists.model.UserListCreateDTO
import com.platimee.spring_platimee.lists.model.UserListDTOMapper
import com.platimee.spring_platimee.lists.model.UserListResponseDTO
import com.platimee.spring_platimee.lists.repository.UserListRepository
import com.platimee.spring_platimee.users.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserListService(
    private val userListRepository: UserListRepository,
    private val userRepository: UserRepository,
    private val animeRepository: AnimeRepository
) {

    @Transactional
    fun createUserList(dto: UserListCreateDTO): UserListResponseDTO {
        // Verify the user exists
        val user = userRepository.findById(dto.userId)
            .orElseThrow { EntityNotFoundException("User with ID ${dto.userId} not found") }

        // Fetch the anime to add to the list
        val animeList = animeRepository.findAllById(dto.animeIds)

        // Create the entity
        val userList = UserList(
            name = dto.name,
            description = dto.description,
            user = user
        )

        // Add the anime to this list
        userList.anime.addAll(animeList)

        // Save and convert to DTO
        val savedUserList = userListRepository.save(userList)
        return UserListDTOMapper.toResponseDTO(savedUserList)
    }

    // Example: retrieve a single list by ID
    @Transactional(readOnly = true)
    fun getUserList(listId: Long): UserListResponseDTO {
        val userList = userListRepository.findById(listId)
            .orElseThrow { EntityNotFoundException("UserList with ID $listId not found") }
        return UserListDTOMapper.toResponseDTO(userList)
    }

    // Example: add an anime to an existing list
    @Transactional
    fun addAnimeToList(listId: Long, animeId: Long): UserListResponseDTO {
        val userList = userListRepository.findById(listId)
            .orElseThrow { EntityNotFoundException("UserList with ID $listId not found") }

        val anime = animeRepository.findById(animeId)
            .orElseThrow { EntityNotFoundException("Anime with ID $animeId not found") }

        userList.anime.add(anime)
        return UserListDTOMapper.toResponseDTO(userListRepository.save(userList))
    }

    // Add more methods for removing anime, updating list name, deleting list, etc.
}
