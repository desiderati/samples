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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import dev.springbloom.core.validation.constraints.DefaultEmail
import dev.springbloom.data.jpa.AbstractIdentity
import dev.springbloom.web.security.UserData
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "sampl_usr_mgmt_usr")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
class User(

    @Id
    @SequenceGenerator(
        name = "sampl_usr_mgmt_usr_id",
        sequenceName = "sampl_usr_mgmt_usr_id",
        allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sampl_usr_mgmt_usr_id")
    private val id: Long? = null,

    @Column(nullable = false, unique = true)
    @field:NotBlank("{user.login.notBlank}")
    val login: String,

    @Column(nullable = false)
    @field:NotBlank("{user.name.notBlank}")
    val name: String,

    @Column(nullable = false, unique = true)
    @field:NotBlank("{user.email.notBlank}")
    @field:DefaultEmail("{user.email.invalid}")
    val email: String,

    @Column(nullable = false)
    val systemAdmin: Boolean = false

) : AbstractIdentity<Long>(),
    UserData {

    @ManyToMany
    @JoinTable(
        name = "sampl_usr_mgmt_assign",
        joinColumns = [JoinColumn(name = "id_user", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "id_allocation", referencedColumnName = "id")]
    )
    val allocations: MutableSet<Allocation> = mutableSetOf()

    @Override
    override fun getId(): Long? = id

    fun groups(): MutableSet<Group> = allocations.map { it.group }.toMutableSet()
}
