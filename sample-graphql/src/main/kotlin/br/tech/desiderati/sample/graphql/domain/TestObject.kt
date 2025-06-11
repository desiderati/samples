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

package br.tech.desiderati.sample.graphql.domain

import arrow.core.*
import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import dev.springbloom.core.validation.*

data class TestObject(
    var name: String,
    val cellphone: String
) : TypedValidationEntity {

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(TestObject::name)
        private val toStringProperties = arrayOf(TestObject::name)
    }

    override fun isValid(): Either<TypedValidationException, Unit> =
        ensure {
            all(
                { isValid(name == "Test") { "TestObject.invalidName" } },
                { isValid(name.length == 4) { "TestObject.invalidNameLength" } }
            )
        }

    override fun equals(other: Any?) = kotlinEquals(other, equalsAndHashCodeProperties)

    override fun hashCode() = kotlinHashCode(equalsAndHashCodeProperties)

    override fun toString() = kotlinToString(toStringProperties)
}
