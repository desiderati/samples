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

import au.com.console.kassava.kotlinEquals
import br.tech.desiderati.sample.user_management.domain.Group
import br.tech.desiderati.sample.user_management.repository.UserRepository
import dev.springbloom.test.CleanupDatabase
import dev.springbloom.test.MockitoLoader
import dev.springbloom.test.annotation.ServiceJpaTest
import org.hibernate.Hibernate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.Objects

@Testcontainers
@ExtendWith(PostgreSQLContainerAware::class) // Necessary for testcontainers to work properly at Kotlin!

@ServiceJpaTest(
    properties = [
        "spring.liquibase.enabled=true",
        "spring.liquibase.change-log=classpath:db/changelog/test-initial-schema.xml"
    ]
)
@CleanupDatabase(mode = CleanupDatabase.Mode.AFTER_CLASS)

// Ensures that all objects (services dependencies) that are not defined here will be created via mock.
@ContextConfiguration(loader = MockitoLoader::class)
class GroupEqualsTest {

    @Autowired
    private val userRepository: UserRepository? = null

    private val testGroup = Group(4L, "Workgroup B")

    @Test
    fun `should be equals to test group`() {
        assertNotNull(userRepository)

        val user = userRepository!!.findByLogin("felipedesiderati")
        val userAllocations = user!!.allocations
        assertEquals(userAllocations.filter { it.group == testGroup }.size, 1)
    }

    @Test
    fun `should be equals to test group, forcing equals() function`() {
        assertNotNull(userRepository)

        val user = userRepository!!.findByLogin("felipedesiderati")
        val userAllocations = user!!.allocations
        assertEquals(
            userAllocations
                .filter {
                    @Suppress("ReplaceCallWithBinaryOperator")
                    it.group.equals(testGroup)
                }.size,
            1
        )
    }

    @Test
    fun `should be equals to test group, using hibernate unproxy`() {
        assertNotNull(userRepository)

        val user = userRepository!!.findByLogin("felipedesiderati")
        val userAllocations = user!!.allocations
        assertEquals(userAllocations.filter { Hibernate.unproxy(it.group) == testGroup }.size, 1)
    }

    @Test
    fun `should be equals to test group, using hibernate unproxy and forcing equals() function`() {
        assertNotNull(userRepository)

        val user = userRepository!!.findByLogin("felipedesiderati")
        val userAllocations = user!!.allocations
        assertEquals(
            userAllocations
                .filter {
                    @Suppress("ReplaceCallWithBinaryOperator")
                    Hibernate.unproxy(it.group).equals(testGroup)
                }.size,
            1
        )
    }

    @Test
    fun `should be equals to test group, using kotlin equals`() {
        assertNotNull(userRepository)

        val equalsAndHashCodeProperties = arrayOf(Group::name)
        val user = userRepository!!.findByLogin("felipedesiderati")
        val userAllocations = user!!.allocations
        assertEquals(
            userAllocations.filter { it.group.kotlinEquals(testGroup, equalsAndHashCodeProperties) }.size,
            1
        )
    }

    @Test
    fun `should be equals to test group, using object equals`() {
        assertNotNull(userRepository)

        val user = userRepository!!.findByLogin("felipedesiderati")
        val userAllocations = user!!.allocations
        assertEquals(userAllocations.filter { Objects.equals(it.group, testGroup) }.size, 1)
    }
}
