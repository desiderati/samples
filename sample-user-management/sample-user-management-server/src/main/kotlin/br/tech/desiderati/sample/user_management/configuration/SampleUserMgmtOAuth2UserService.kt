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

package br.tech.desiderati.sample.user_management.configuration

import lombok.extern.slf4j.Slf4j
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Slf4j
@Component
class SampleUserMgmtOAuth2UserService : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Suppress("unchecked_cast")
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        // Just to ensure that we are using the authorities correctly.
        return DefaultOAuth2UserService().loadUser(userRequest).let {
            val userNameAttributeName =
                userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

            val authorities = it.attributes["authorities"]
            if (authorities == null || authorities !is List<*> || authorities.isEmpty()) {
                it
            } else {
                DefaultOAuth2User(
                    (authorities as List<String>)
                        .stream()
                        .map { role: String -> SimpleGrantedAuthority(role) }
                        .toList(),
                    it.attributes,
                    userNameAttributeName
                )
            }
        }
    }
}
