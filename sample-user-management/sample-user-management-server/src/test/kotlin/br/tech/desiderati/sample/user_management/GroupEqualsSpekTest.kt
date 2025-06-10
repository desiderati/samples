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

package br.tech.desiderati.sample.user_management

import br.tech.desiderati.sample.user_management.domain.Group
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.springbloom.core.not
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

@Suppress("unused")
class GroupEqualsSpekTest :
    Spek({

        val `group with null id` =
            Group(null, "Presidência")

        val `group with id` =
            Group(1L, "Presidência")

        val `group with same id, but different name` =
            Group(1L, "Presidência Interina")

        val `group with different id, but same name` =
            Group(2L, "Presidência")

        describe("a group with null id") {
            it("should not be equal to a group with an id, even if name is the same") {
                assertThat(`group with null id`, not(equalTo(`group with id`)))
            }
        }

        describe("a group") {
            it("should not be equal to null") {
                assertThat(`group with id`, not(absent()))
            }

            it("should not be equal to an object of another type") {
                assertThat(`group with id`, not(equalTo<Any>("other object")))
            }

            it("should not be equal to a group with a different id") {
                assertThat(`group with id`, not(equalTo(`group with different id, but same name`)))
            }

            it("should not be equal to a group with a different name") {
                assertThat(`group with id`, not(equalTo(`group with same id, but different name`)))
            }

            it("should be equal to the same object") {
                assertThat(`group with id`, equalTo(`group with id`))
            }
        }
    })
