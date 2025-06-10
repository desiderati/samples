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

import br.tech.desiderati.sample.user_management.domain.Permission
import br.tech.desiderati.sample.user_management.domain.credentials.IsAdministrator
import br.tech.desiderati.sample.user_management.graphql.input.PermissionInput
import br.tech.desiderati.sample.user_management.graphql.mapper.PermissionMapper
import br.tech.desiderati.sample.user_management.service.AllocationService
import br.tech.desiderati.sample.user_management.service.PermissionService
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
class PermissionResolver(
    private val permissionService: PermissionService,
    private val allocationService: AllocationService

) {
    /**
     * Creates a new permission.
     *
     * @param permissionInput the permission input data
     * @return the created permission
     */
    @IsAdministrator
    @MutationMapping
    fun createPermission(
        @Argument @Valid permissionInput: PermissionInput
    ): Permission {
        val allocation = allocationService.findAllocationById(permissionInput.allocationId)
        val permission = PermissionMapper.INSTANCE.toPermission(permissionInput, allocation)
        return permissionService.createPermission(permission)
    }

    /**
     * Finds all permissions with pagination.
     *
     * @param pageable the pagination information
     * @return a page of permissions
     */
    @QueryMapping
    fun findAllPermissions(pageable: Pageable): Page<Permission> = permissionService.findAllPermissions(pageable)

    /**
     * Updates an existing permission.
     *
     * @param permissionInput the permission input data
     * @return the updated permission
     */
    @IsAdministrator
    @MutationMapping
    fun updatePermission(
        @Argument @Valid permissionInput: PermissionInput
    ): Permission {
        val allocation = allocationService.findAllocationById(permissionInput.allocationId)
        val permission = PermissionMapper.INSTANCE.toPermission(permissionInput, allocation)
        return permissionService.updatePermission(permission)
    }

    /**
     * Deletes a permission by ID.
     *
     * @param id the ID of the permission to delete
     * @return true if the permission was successfully deleted
     */
    @IsAdministrator
    @MutationMapping
    fun deletePermission(
        @Argument id: Long
    ): Boolean {
        permissionService.deletePermissionById(id)
        return true
    }
}
