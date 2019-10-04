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
class UploadControllerRightFileTest extends AbstractUploadControllerTest {

    @Test
    void uploadRightFileShouldReturnInternalServerErrorBecauseInvalidFileId() throws Exception {
        MvcResult result =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/abc/right")
                    .contentType("application/txt")
                    .content(""))
                .andExpect(status().isInternalServerError())
                .andReturn();

        ValidationResponseExceptionDTO responseExceptionDTO =
            mapper.readValue(result.getResponse().getContentAsString(), ValidationResponseExceptionDTO.class);
        assertEquals("http_error_500", responseExceptionDTO.getErrorCode());
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

        ValidationResponseExceptionDTO responseExceptionDTO =
            mapper.readValue(result.getResponse().getContentAsString(), ValidationResponseExceptionDTO.class);
        assertEquals("validation_error_msg", responseExceptionDTO.getErrorCode());

        String[] validationMessages = responseExceptionDTO.getValidationMessages();
        assertEquals(1, validationMessages.length);
        assertEquals("right.fileId:must not be null", validationMessages[0]);
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
        assertEquals("validation_error_msg", responseExceptionDTO.getErrorCode());

        String[] validationMessages = responseExceptionDTO.getValidationMessages();
        assertEquals(1, validationMessages.length);
        assertEquals("right.fileId:must be greater than 0", validationMessages[0]);
    }

    @Test
    void uploadRightFileShouldReturnBadRequestBecauseFileAlreadyUploaded() throws Exception {
        MvcResult result =
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
        assertEquals("file_already_uploaded_excpetion", responseExceptionDTO.getErrorCode());
    }

    @Test
    void uploadRightFileShouldReturnSuccessEvenIfEmptyContent() throws Exception {
        MvcResult result =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/2/right")
                    .contentType("application/txt")
                    .content(""))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void uploadRightFileShouldReturnSuccess() throws Exception {
        MvcResult result =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/3/right")
                    .contentType("application/txt")
                    .content("123456"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
