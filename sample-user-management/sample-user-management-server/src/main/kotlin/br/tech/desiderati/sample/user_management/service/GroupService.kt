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
import br.tech.desiderati.sample.user_management.domain.Group
import br.tech.desiderati.sample.user_management.repository.GroupRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GroupService(
    private val groupRepository: GroupRepository
) {
    /**
     * Creates a new group.
     *
     * @param group the group to create
     * @return the created group
     * @throws SampleUserMgmtException if a group with the same name already exists
     */
    @Transactional
    fun createGroup(group: Group): Group {
        if (groupRepository.findByName(group.name) != null) {
            throw SampleUserMgmtException("group.nameAlreadyExists", group.name)
        }
        return groupRepository.save(group)
    }

    /**
     * Finds all groups with pagination.
     *
     * @param pageable the pagination information
     * @return a page of groups
     */
    fun findAllGroups(pageable: Pageable): Page<Group> = groupRepository.findAll(pageable)

    /**
     * Finds a group by its unique identifier.
     *
     * @param id the unique identifier of the group to be retrieved
     * @return the group corresponding to the given ID
     * @throws SampleUserMgmtException if no group is found with the provided ID
     */
    fun findGroupById(id: Long): Group =
        groupRepository.findById(id).orElseThrow {
            SampleUserMgmtException("group.notFoundById", id)
        }

    /**
     * Updates an existing group.
     *
     * @param group the group to update
     * @return the updated group
     * @throws SampleUserMgmtException if the group does not exist
     */
    @Transactional
    fun updateGroup(group: Group): Group {
        val existingGroup =
            group.getId()?.let {
                groupRepository.findById(it).orElseThrow {
                    SampleUserMgmtException("group.notFoundById", it)
                }
            } ?: throw SampleUserMgmtException("group.withNullId")

        // Check if the name is being changed and if the new name already exists.
        if (existingGroup.name != group.name && groupRepository.findByName(group.name) != null) {
            throw SampleUserMgmtException("group.nameAlreadyExists", group.name)
        }

        return groupRepository.save(group)
    }

    /**
     * Deletes a group by ID.
     *
     * @param id the ID of the group to delete
     * @throws SampleUserMgmtException if the group does not exist
     */
    @Transactional
    fun deleteGroupById(id: Long) {
        val group =
            groupRepository.findById(id).orElseThrow {
                SampleUserMgmtException("group.notFoundById", id)
            }
        groupRepository.delete(group)
    }
}
