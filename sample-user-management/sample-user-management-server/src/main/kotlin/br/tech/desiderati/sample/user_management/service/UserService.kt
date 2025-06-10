/*
 * Copyright (c) 2025 - Felipe Desiderati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package br.tech.desiderati.sample.user_management.service

import br.tech.desiderati.sample.user_management.SampleUserMgmtException
import br.tech.desiderati.sample.user_management.domain.User
import br.tech.desiderati.sample.user_management.repository.UserRepository
import dev.springbloom.web.security.support.UserDataRetriever
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) : UserDataRetriever<User> {

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return the created user
     * @throws SampleUserMgmtException if a user with the same login already exists
     */
    @Transactional
    fun createUser(user: User): User {
        if (userRepository.findByLogin(user.login) != null) {
            throw SampleUserMgmtException("user.loginAlreadyExists", user.login)
        }

        // TODO Felipe Desiderati: See UserService_README.md for further information.
        return userRepository.save(user)
    }

    /**
     * Finds all users with pagination.
     *
     * @param pageable the pagination information
     * @return a page of users
     */
    fun findAllUsers(pageable: Pageable): Page<User> = userRepository.findAll(pageable)

    /**
     * Finds a user based on their username.
     *
     * @param username the username of the user to find
     * @return the user object if found, or null if no user with the specified username exists
     */
    override fun findUserByUsername(username: String): User? = userRepository.findByLogin(username)

    /**
     * Updates an existing user.
     *
     * @param user the user to update
     * @return the updated user
     * @throws SampleUserMgmtException if the user does not exist
     */
    @Transactional
    fun updateUser(user: User): User {
        val existingUser =
            user.getId()?.let {
                userRepository.findById(it).orElseThrow {
                    SampleUserMgmtException("user.notFoundById", it)
                }
            } ?: throw SampleUserMgmtException("user.withNullId")

        // Check if the name is being changed and if the new name already exists.
        if (existingUser.login != user.login && userRepository.findByLogin(user.login) != null) {
            throw SampleUserMgmtException("user.loginAlreadyExists", user.login)
        }

        return userRepository.save(user)
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete
     * @throws SampleUserMgmtException if the user does not exist
     */
    @Transactional
    fun deleteUserById(id: Long) {
        val user =
            userRepository.findById(id).orElseThrow {
                SampleUserMgmtException("user.notFoundById", id)
            }
        userRepository.delete(user)
    }
}
