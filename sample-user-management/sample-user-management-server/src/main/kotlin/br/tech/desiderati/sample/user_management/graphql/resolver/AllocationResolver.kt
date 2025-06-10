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

import br.tech.desiderati.sample.user_management.domain.Allocation
import br.tech.desiderati.sample.user_management.domain.credentials.IsAdministrator
import br.tech.desiderati.sample.user_management.service.AllocationService
import br.tech.desiderati.sample.user_management.service.GroupService
import br.tech.desiderati.sample.user_management.service.RoleService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class AllocationResolver(
    private val allocationService: AllocationService,
    private val groupService: GroupService,
    private val roleService: RoleService
) {
    @IsAdministrator
    @MutationMapping
    fun allocateRoleOnGroup(
        @Argument groupId: Long,
        @Argument roleId: Long
    ): Allocation {
        val group = groupService.findGroupById(groupId)
        val role = roleService.findRoleById(roleId)
        return allocationService.allocateRoleOnGroup(Allocation(group = group, role = role))
    }
}
