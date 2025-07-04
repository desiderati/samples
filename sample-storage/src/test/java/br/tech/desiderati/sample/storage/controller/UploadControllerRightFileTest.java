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
package br.tech.desiderati.sample.storage.controller;

import dev.springbloom.web.rest.exception.ResponseExceptionDTO;
import dev.springbloom.web.rest.exception.ValidationResponseExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Tests.
 */
@Slf4j
class UploadControllerRightFileTest extends AbstractUploadControllerTest {

    @Test
    void uploadRightFileShouldReturnInternalServerErrorBecauseInvalidFileId() throws Exception {
        MvcResult result =
            mockMvc.perform(
                    post(API_BASE_PATH + "/v1/diff/abc/right")
                        .contentType("application/txt")
                        .content(""))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationResponseExceptionDTO responseExceptionDTO =
            mapper.readValue(result.getResponse().getContentAsString(), ValidationResponseExceptionDTO.class);
        assertEquals("http_error_400", responseExceptionDTO.getErrorCode());
    }

    @Test
    void uploadRightFileShouldReturnBadRequestBecauseNullFileId() throws Exception {
        MvcResult result =
            mockMvc.perform(
                    post(API_BASE_PATH + "/v1/diff/ /right")
                        .contentType("application/txt")
                        .content(""))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseExceptionDTO responseExceptionDTO =
            mapper.readValue(result.getResponse().getContentAsString(), ResponseExceptionDTO.class);
        assertEquals("http_error_400", responseExceptionDTO.getErrorCode());
    }

    @Test
    void uploadRightFileShouldReturnBadRequestBecauseNegativeFileId() throws Exception {
        MvcResult result =
            mockMvc.perform(
                    post(API_BASE_PATH + "/v1/diff/-1/right")
                        .contentType("application/txt")
                        .content(""))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationResponseExceptionDTO responseExceptionDTO =
            mapper.readValue(result.getResponse().getContentAsString(), ValidationResponseExceptionDTO.class);
        assertEquals("DefaultValidation.message", responseExceptionDTO.getErrorCode());

        String[] validationMessages = responseExceptionDTO.getValidationMessages();
        assertEquals(1, validationMessages.length);
        assertEquals("right.fileId:must be greater than 0", validationMessages[0]);
    }

    @Test
    void uploadRightFileShouldReturnBadRequestBecauseFileAlreadyUploaded() throws Exception {
        mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/1/right")
                    .contentType("application/txt")
                    .content("123456"))
            .andExpect(status().isOk())
            .andReturn();

        MvcResult secondResult =
            mockMvc.perform(
                    post(API_BASE_PATH + "/v1/diff/1/right")
                        .contentType("application/txt")
                        .content("123456"))
                .andExpect(status().isNotAcceptable())
                .andReturn();

        ValidationResponseExceptionDTO responseExceptionDTO =
            mapper.readValue(secondResult.getResponse().getContentAsString(), ValidationResponseExceptionDTO.class);
        assertEquals("file_already_uploaded_exception", responseExceptionDTO.getErrorCode());
    }

    @Test
    void uploadRightFileShouldReturnSuccessEvenIfEmptyContent() throws Exception {
        mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/2/right")
                    .contentType("application/txt")
                    .content(""))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    void uploadRightFileShouldReturnSuccess() throws Exception {
        mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/3/right")
                    .contentType("application/txt")
                    .content("123456"))
            .andExpect(status().isOk())
            .andReturn();
    }
}
