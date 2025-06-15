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

package br.tech.desiderati.sample.graphql.graphql.resolver

import br.tech.desiderati.sample.graphql.domain.TestObject
import br.tech.desiderati.sample.graphql.service.TestService
import dev.springbloom.core.exception.ApplicationException
import dev.springbloom.web.configuration.MessageSourceContextHolder
import dev.springbloom.web.configuration.async.launchWithContextPropagation
import dev.springbloom.web.configuration.async.supplyAsyncWithContext
import dev.springbloom.web.configuration.async.withContextPropagation
import dev.springbloom.web.security.IsAdministrator
import dev.springbloom.web.security.AuthenticatedUsername
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.scheduling.annotation.Async
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.reflect.UndeclaredThrowableException
import java.util.concurrent.CompletableFuture

@Controller
class TestResolver(
    private val testService: TestService
) {

    @QueryMapping
    @IsAdministrator
    fun isAdministrator(): Boolean = true

    @QueryMapping
    fun getAuthenticatedUsernameIfPresent(
        @AuthenticatedUsername username: String?,
        authentication: Authentication
    ): String {
        println("Authentication = $authentication")
        println("Authentication Name = $username")
        return authentication.name
    }

    @QueryMapping
    fun getInternationalizedMessage(
        @Argument messageId: String
    ): String? {
        println("Testando uma chamada bloqueante!")
        return getMessage(messageId)
    }

    /**
     * Quando a função é marcada com `suspend`, o Spring GraphQL irá executar a função numa corrotina.
     * Precisamos assim informar que desejamos que o contexto seja propagado.
     */
    @QueryMapping
    suspend fun getSimpleSuspendInternationalizedMessage(
        @Argument messageId: String
    ): String? =
        withContextPropagation {
            delay(2500)
            println("Testando uma chamada suspendida!")
            getMessage(messageId)
        }

    @QueryMapping
    suspend fun getNestedSuspendInternationalizedMessage(
        @Argument messageId: String
    ): String? =
        coroutineScope {
            launchWithContextPropagation {
                delay(7500)
                println("Testando uma chamada suspendida dentro de uma função suspendida!")
                getMessage(messageId)
            }

            println("Testando uma chamada suspendida!")
            getMessage(messageId)
        }

    @Async
    @QueryMapping
    fun getAsyncInternationalizedMessage(
        @Argument messageId: String
    ): CompletableFuture<String?> {
        Thread.sleep(5000)
        println("Testando uma chamada assíncrona!")
        getRequestAttributes()

        // Caso não estejamos usando spring.mvc.async.context-propagation-mode=NON_INHERITABLE,
        // precisamos forçar a propagação do contexto. Não usar 'supplyAsync {..}' diretamente,
        // pois a mesma não propaga o contexto.
        return supplyAsyncWithContext {
            println("Testando uma chamada assíncrona, dentro do método `supply{..}`!")
            getMessage(messageId)
        }
    }

    private fun getRequestAttributes(): ServletRequestAttributes? {
        val locale = LocaleContextHolder.getLocale()
        println("Locale = $locale")

        val securityContext = SecurityContextHolder.getContext()
        println("SecurityContext = $securityContext")

        val attributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?
        println("RequestAttributes = $attributes")

//        val request = attributes?.request
//        val attributeNames = request?.attributeNames
//        while (attributeNames?.hasMoreElements() == true) {
//            val attributeName = attributeNames.nextElement()
//            val attributeValue = request.getAttribute(attributeName)
//            println("Attribute Name: $attributeName, Value: $attributeValue")
//        }
        return attributes
    }

    private fun getMessage(messageId: String): String? {
        // Garantir que está sendo retornado, quando executado dentro de um CompletableFuture.
        getRequestAttributes()
        return MessageSourceContextHolder.getMessage(messageId)
    }

    @QueryMapping
    fun getPaginatedResponse(): Page<String> =
        PageImpl(listOf("Teste 1", "Teste 2", "Teste 3"), PageRequest.of(0, 3), 3)

    @QueryMapping
    fun throwException(): Unit = throw Exception("Testando o disparo de Exception!")

    @QueryMapping
    fun throwThrowable(): Unit = throw Throwable("Testando o disparo de Throwable!")

    @QueryMapping
    fun throwUndeclaredThrowableException(): Unit =
        throw UndeclaredThrowableException(Throwable("Testando o disparo de UndeclaredThrowableException!"))

    @QueryMapping
    fun throwApplicationException(): Unit = throw ApplicationException("Testando o disparo de ApplicationException!")

    @MutationMapping
    fun createTestObjectWithoutValidation(
        @Argument testObject: TestObject
    ): TestObject = testService.createTestObjectWithoutValidation(testObject)

    @MutationMapping
    fun validateInputTestObject(
        @Argument testObject: TestObject
    ) {
        testService.validateInputTestObject(testObject)
    }

    @MutationMapping
    fun validateInputAndReturnTestObject(
        @Argument testObject: TestObject
    ) {
        testService.validateInputAndReturnTestObject(testObject)
    }
}
