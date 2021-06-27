package br.com.devcave.grpc.devh.repository

import br.com.devcave.grpc.devh.entity.User
import br.com.devcave.grpc.proto.common.Genre
import org.springframework.stereotype.Repository

@Repository
class UserRepository {

    fun findById(loginId: String): User? =
        userList.firstOrNull { it.login == loginId }

    fun save(user: User) {
        userList.filter { it.login == user.login }
            .forEach { it.genre = user.genre }
    }

    companion object {
        private val userList =
            listOf(
                User("001", "user1", Genre.ACTION.name),
                User("002", "user2", Genre.CRIME.name),
                User("003", "user3", Genre.DRAMA.name),
                User("004", "user4", Genre.WESTERN.name),
                User("005", "user5", Genre.ACTION.name),
                User("006", "user6", Genre.CRIME.name)
            )
    }
}