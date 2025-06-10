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
import br.tech.desiderati.sample.user_management.domain.Role
import br.tech.desiderati.sample.user_management.repository.RoleRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {

    /**
     * Creates a new role.
     *
     * @param role the role to create
     * @return the created role
     * @throws SampleUserMgmtException if a role with the same name already exists
     */
    @Transactional
    fun createRole(role: Role): Role {
        if (roleRepository.findByName(role.name) != null) {
            throw SampleUserMgmtException("role.nameAlreadyExists", role.name)
        }
        return roleRepository.save(role)
    }

    /**
     * Finds all roles with pagination.
     *
     * @param pageable the pagination information
     * @return a page of roles
     */
    fun findAllRoles(pageable: Pageable): Page<Role> = roleRepository.findAll(pageable)

    /**
     * Finds a role by its unique identifier.
     *
     * @param id the ID of the role to be retrieved
     * @return the role if found, or null if no role exists with the specified ID
     * @throws SampleUserMgmtException if the role is not found
     */
    fun findRoleById(id: Long): Role =
        roleRepository.findById(id).orElseThrow {
            SampleUserMgmtException("role.notFoundById", id)
        }

    /**
     * Updates an existing role.
     *
     * @param role the role to update
     * @return the updated role
     * @throws SampleUserMgmtException if the role does not exist
     */
    @Transactional
    fun updateRole(role: Role): Role {
        val existingRole =
            role.getId()?.let {
                roleRepository.findById(it).orElseThrow {
                    SampleUserMgmtException("role.notFoundById", it)
                }
            } ?: throw SampleUserMgmtException("role.withNullId")

        // Check if the name is being changed and if the new name already exists.
        if (existingRole.name != role.name && roleRepository.findByName(role.name) != null) {
            throw SampleUserMgmtException("role.nameAlreadyExists", role.name)
        }

        return roleRepository.save(role)
    }

    /**
     * Deletes a role by ID.
     *
     * @param id the ID of the role to delete
     * @throws SampleUserMgmtException if the role does not exist
     */
    @Transactional
    fun deleteRoleById(id: Long) {
        val role =
            roleRepository.findById(id).orElseThrow {
                SampleUserMgmtException("role.notFoundById", id)
            }
        roleRepository.delete(role)
    }
}
