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

package br.tech.desiderati.sample.user_management.graphql.resolver

import br.tech.desiderati.sample.user_management.SampleUserMgmtException
import br.tech.desiderati.sample.user_management.domain.Group
import br.tech.desiderati.sample.user_management.domain.User
import br.tech.desiderati.sample.user_management.domain.credentials.IsAdministrator
import br.tech.desiderati.sample.user_management.graphql.input.UserInput
import br.tech.desiderati.sample.user_management.graphql.mapper.UserMapper
import br.tech.desiderati.sample.user_management.service.UserService
import dev.springbloom.web.security.support.AuthenticatedUsername
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated

@Validated
@Controller
class UserResolver(
    private val userService: UserService
) {
    /**
     * Creates a new user.
     *
     * @param userInput the user input data
     * @return the created user
     */
    @IsAdministrator
    @MutationMapping
    fun createUser(
        @Argument @Valid userInput: UserInput
    ): User {
        val user = UserMapper.INSTANCE.toUser(userInput)
        return userService.createUser(user)
    }

    /**
     * Finds all users with pagination.
     *
     * @param pageable the pagination information
     * @return a page of users
     */
    @QueryMapping
    fun findAllUsers(pageable: Pageable): Page<User> = userService.findAllUsers(pageable)

    /**
     * Updates an existing user.
     *
     * @param userInput the user input data
     * @return the updated user
     */
    @IsAdministrator
    @MutationMapping
    fun updateUser(
        @Argument @Valid userInput: UserInput
    ): User {
        val user = UserMapper.INSTANCE.toUser(userInput)
        return userService.updateUser(user)
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete
     * @return true if the user was successfully deleted
     */
    @IsAdministrator
    @MutationMapping
    fun deleteUser(
        @Argument id: Long
    ): Boolean {
        userService.deleteUserById(id)
        return true
    }

    @QueryMapping
    fun getLoggedUserGroups(
        pageable: Pageable,
        @AuthenticatedUsername loggedUsername: String
    ): Page<Group> {
        val user = userService.findUserByUsername(loggedUsername)
        if (user == null) {
            throw SampleUserMgmtException("user.notFoundByLogin", loggedUsername)
        }

        // Ideally, the query should already be performed with pagination at the database level.
        val groups = user.groups().toList()
        val start = pageable.offset.toInt()
        val end = minOf(start + pageable.pageSize, groups.size)
        val pageContent = if (start <= end) groups.subList(start, end) else emptyList()
        return PageImpl(pageContent, pageable, groups.size.toLong())
    }
}
