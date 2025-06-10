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

import br.tech.desiderati.sample.user_management.domain.Group
import br.tech.desiderati.sample.user_management.domain.credentials.IsAdministrator
import br.tech.desiderati.sample.user_management.graphql.input.GroupInput
import br.tech.desiderati.sample.user_management.graphql.mapper.GroupMapper
import br.tech.desiderati.sample.user_management.service.GroupService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated

@Validated
@Controller
class GroupResolver(
    private val groupService: GroupService
) {
    /**
     * Creates a new group.
     *
     * @param groupInput the group input data
     * @return the created group
     */
    @IsAdministrator
    @MutationMapping
    fun createGroup(
        @Argument @Valid groupInput: GroupInput
    ): Group {
        val group = GroupMapper.INSTANCE.toGroup(groupInput)
        return groupService.createGroup(group)
    }

    /**
     * Finds all groups with pagination.
     *
     * @param pageable the pagination information
     * @return a page of groups
     */
    @QueryMapping
    fun findAllGroups(pageable: Pageable): Page<Group> = groupService.findAllGroups(pageable)

    /**
     * Updates an existing group.
     *
     * @param groupInput the group input data
     * @return the updated group
     */
    @IsAdministrator
    @MutationMapping
    fun updateGroup(
        @Argument @Valid groupInput: GroupInput
    ): Group {
        val group = GroupMapper.INSTANCE.toGroup(groupInput)
        return groupService.updateGroup(group)
    }

    /**
     * Deletes a group by ID.
     *
     * @param id the ID of the group to delete
     * @return true if the group was successfully deleted
     */
    @IsAdministrator
    @MutationMapping
    fun deleteGroup(
        @Argument id: Long
    ): Boolean {
        groupService.deleteGroupById(id)
        return true
    }
}
