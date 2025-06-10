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

package br.tech.desiderati.sample.user_management.domain

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import dev.springbloom.data.jpa.AbstractIdentity
import jakarta.persistence.*
import jakarta.validation.Valid

@Entity
@Table(name = "sampl_usr_mgmt_alloc")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
class Allocation(

    @Id
    @SequenceGenerator(
        name = "sampl_usr_mgmt_alloc_id",
        sequenceName = "sampl_usr_mgmt_alloc_id",
        allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sampl_usr_mgmt_alloc_id")
    private val id: Long? = null,

    @Valid
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_group", nullable = false)
    val group: Group,

    @Valid
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_role", nullable = false)
    val role: Role

) : AbstractIdentity<Long>() {

    companion object {
        private val equalsAndHashCodeProperties =
            arrayOf(Allocation::id, Allocation::group, Allocation::role)
    }

    @ManyToMany
    @JoinTable(
        name = "sampl_usr_mgmt_assign",
        joinColumns = [JoinColumn(name = "id_allocation", nullable = false)],
        inverseJoinColumns = [JoinColumn(name = "id_user", nullable = false)]
    )
    val usersAllocated: MutableList<User> = mutableListOf()

    @OneToMany(mappedBy = "allocation", cascade = [CascadeType.ALL], orphanRemoval = true)
    val permissions: MutableList<Permission> = mutableListOf()

    @Override
    override fun getId(): Long? = id

    override fun equals(other: Any?) = kotlinEquals(other, equalsAndHashCodeProperties)

    override fun hashCode() = kotlinHashCode(equalsAndHashCodeProperties)
}
