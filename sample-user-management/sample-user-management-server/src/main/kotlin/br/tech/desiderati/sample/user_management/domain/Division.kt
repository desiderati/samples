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
import jakarta.persistence.*

@Entity
@DiscriminatorValue("D")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
class Division(
    divisionId: Long? = null,
    name: String
) : Group(divisionId, name) {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parent")
    val parent: Division? = null

    @OneToMany
    @JoinColumn(name = "id_parent")
    @OrderColumn(name = "order_as_division")
    val children: MutableSet<Division> = mutableSetOf()

    fun isRoot(): Boolean = parent == null || parent?.id == null

    override fun ancestrals(): MutableSet<Division> = ancestrals(this)

    private fun ancestrals(
        division: Division,
        ancestrals: MutableSet<Division> = mutableSetOf()
    ): MutableSet<Division> {
        if (division.parent != null && !division.parent!!.isRoot()) {
            ancestrals.add(division.parent!!)
            ancestrals(division.parent!!, ancestrals)
        }
        return ancestrals
    }
}
