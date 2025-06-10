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
import au.com.console.kassava.kotlinToString
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import dev.springbloom.data.jpa.AbstractIdentity
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.annotations.DiscriminatorOptions

@Entity
@Table(name = "sampl_usr_mgmt_grp")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorOptions(force = true)
@DiscriminatorColumn(name = "type")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)

// If the class is not declared as abstract, there is no need to define such annotations.
// Left here for documentation purposes only.
// @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "_type")
// @JsonSubTypes(
//    JsonSubTypes.Type(value = Division::class, name = "Division"),
//    JsonSubTypes.Type(value = Workgroup::class, name = "Workgroup")
// )

// XXX Felipe Desiderati: Lombok is not working yet, so we kept its annotations and
//  the [equals] and [hashCode] methods using Kassava.
//  Left here for documentation purposes only.
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class Group(

    @Id
    @SequenceGenerator(
        name = "sampl_usr_mgmt_grp_id",
        sequenceName = "sampl_usr_mgmt_grp_id",
        allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sampl_usr_mgmt_grp_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private val id: Long? = null,

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    @ToString.Include
    @field:NotBlank("{group.name.notBlank}")
    val name: String

) : AbstractIdentity<Long>() {

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(Group::id, Group::name)
        private val toStringProperties = arrayOf(Group::id, Group::name)
    }

    @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL], orphanRemoval = true)
    val allocations: MutableSet<Allocation> = mutableSetOf()

    @Override
    override fun getId(): Long? = id

    /**
     * @return All the ancestrals for the current [Division].
     * For a [Workgroup] it will always be an empty collection.
     */
    fun ancestrals(): MutableSet<out Group> = mutableSetOf()

    override fun equals(other: Any?) = kotlinEquals(other, equalsAndHashCodeProperties)

    override fun hashCode() = kotlinHashCode(equalsAndHashCodeProperties)

    override fun toString() = kotlinToString(toStringProperties)
}
