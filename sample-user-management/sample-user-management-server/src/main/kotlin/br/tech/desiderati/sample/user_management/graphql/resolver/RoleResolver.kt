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

import br.tech.desiderati.sample.user_management.domain.Role
import br.tech.desiderati.sample.user_management.domain.credentials.IsAdministrator
import br.tech.desiderati.sample.user_management.graphql.input.RoleInput
import br.tech.desiderati.sample.user_management.graphql.mapper.RoleMapper
import br.tech.desiderati.sample.user_management.service.RoleService
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
class RoleResolver(
    private val roleService: RoleService
) {
    /**
     * Creates a new role.
     *
     * @param roleInput the role input data
     * @return the created role
     */
    @IsAdministrator
    @MutationMapping
    fun createRole(
        @Argument @Valid roleInput: RoleInput
    ): Role {
        val role = RoleMapper.INSTANCE.toRole(roleInput)
        return roleService.createRole(role)
    }

    /**
     * Finds all roles with pagination.
     *
     * @param pageable the pagination information
     * @return a page of roles
     */
    @QueryMapping
    fun findAllRoles(pageable: Pageable): Page<Role> = roleService.findAllRoles(pageable)

    /**
     * Updates an existing role.
     *
     * @param roleInput the role input data
     * @return the updated role
     */
    @IsAdministrator
    @MutationMapping
    fun updateRole(
        @Argument @Valid roleInput: RoleInput
    ): Role {
        val role = RoleMapper.INSTANCE.toRole(roleInput)
        return roleService.updateRole(role)
    }

    /**
     * Deletes a role by ID.
     *
     * @param id the ID of the role to delete
     * @return true if the role was successfully deleted
     */
    @IsAdministrator
    @MutationMapping
    fun deleteRole(
        @Argument id: Long
    ): Boolean {
        roleService.deleteRoleById(id)
        return true
    }
}
