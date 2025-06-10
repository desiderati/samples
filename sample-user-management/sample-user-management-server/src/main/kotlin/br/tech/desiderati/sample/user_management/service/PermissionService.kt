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
import br.tech.desiderati.sample.user_management.domain.Permission
import br.tech.desiderati.sample.user_management.repository.PermissionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PermissionService(
    private val permissionRepository: PermissionRepository
) {
    /**
     * Creates a new permission.
     *
     * @param permission the permission to create
     * @return the created permission
     */
    @Transactional
    fun createPermission(permission: Permission): Permission = permissionRepository.save(permission)

    /**
     * Finds all permissions with pagination.
     *
     * @param pageable the pagination information
     * @return a page of permissions
     */
    fun findAllPermissions(pageable: Pageable): Page<Permission> = permissionRepository.findAll(pageable)

    /**
     * Updates an existing permission.
     *
     * @param permission the permission to update
     * @return the updated permission
     * @throws SampleUserMgmtException if the permission does not exist
     */
    @Transactional
    fun updatePermission(permission: Permission): Permission {
        permission.getId()?.let {
            permissionRepository.findById(it).orElseThrow {
                SampleUserMgmtException("permission.notFoundById", it)
            }
        } ?: throw SampleUserMgmtException("permission.withNullId")
        return permissionRepository.save(permission)
    }

    /**
     * Deletes a permission by ID.
     *
     * @param id the ID of the permission to delete
     * @throws SampleUserMgmtException if the permission does not exist
     */
    @Transactional
    fun deletePermissionById(id: Long) {
        val permission =
            permissionRepository.findById(id).orElseThrow {
                SampleUserMgmtException("permission.notFoundById", id)
            }
        permissionRepository.delete(permission)
    }
}
