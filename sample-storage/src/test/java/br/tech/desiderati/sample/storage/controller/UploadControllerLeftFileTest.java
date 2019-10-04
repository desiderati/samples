/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage.controller;

import br.tech.desiderati.common.exception.ValidationResponseExceptionDTO;
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
