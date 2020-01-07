/*
 * Copyright (c) 2020 - Felipe Desiderati
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

import io.herd.common.exception.ValidationResponseExceptionDTO;
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
class UploadControllerLeftFileTest extends AbstractUploadControllerTest {

    @Test
    void uploadLeftFileShouldReturnInternalServerErrorBecauseInvalidFileId() throws Exception {
        MvcResult result =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/abc/left")
                    .contentType("application/txt")
                    .content(""))
                .andExpect(status().isInternalServerError())
                .andReturn();

        ValidationResponseExceptionDTO responseExceptionDTO =
            mapper.readValue(result.getResponse().getContentAsString(), ValidationResponseExceptionDTO.class);
        assertEquals("http_error_500", responseExceptionDTO.getErrorCode());
    }

    @Test
    void uploadLeftFileShouldReturnBadRequestBecauseNullFileId() throws Exception {
        MvcResult result =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/ /left")
                    .contentType("application/txt")
                    .content(""))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationResponseExceptionDTO responseExceptionDTO =
            mapper.readValue(result.getResponse().getContentAsString(), ValidationResponseExceptionDTO.class);
        assertEquals("validation_error_msg", responseExceptionDTO.getErrorCode());

        String[] validationMessages = responseExceptionDTO.getValidationMessages();
        assertEquals(1, validationMessages.length);
        assertEquals("left.fileId:must not be null", validationMessages[0]);
    }

    @Test
    void uploadLeftFileShouldReturnBadRequestBecauseNegativeFileId() throws Exception {
        MvcResult result =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/-1/left")
                    .contentType("application/txt")
                    .content(""))
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationResponseExceptionDTO responseExceptionDTO =
            mapper.readValue(result.getResponse().getContentAsString(), ValidationResponseExceptionDTO.class);
        assertEquals("validation_error_msg", responseExceptionDTO.getErrorCode());

        String[] validationMessages = responseExceptionDTO.getValidationMessages();
        assertEquals(1, validationMessages.length);
        assertEquals("left.fileId:must be greater than 0", validationMessages[0]);
    }

    @Test
    void uploadLeftFileShouldReturnBadRequestBecauseFileAlreadyUploaded() throws Exception {
        MvcResult result =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/1/left")
                    .contentType("application/txt")
                    .content("123456"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult secondResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/1/left")
                    .contentType("application/txt")
                    .content("123456"))
                .andExpect(status().isNotAcceptable())
                .andReturn();

        ValidationResponseExceptionDTO responseExceptionDTO =
            mapper.readValue(secondResult.getResponse().getContentAsString(), ValidationResponseExceptionDTO.class);
        assertEquals("file_already_uploaded_excpetion", responseExceptionDTO.getErrorCode());
    }

    @Test
    void uploadLeftFileShouldReturnSuccessEvenIfEmptyContent() throws Exception {
        MvcResult result =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/2/left")
                    .contentType("application/txt")
                    .content(""))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void uploadLeftFileShouldReturnSuccess() throws Exception {
        MvcResult result =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/3/left")
                    .contentType("application/txt")
                    .content("123456"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
