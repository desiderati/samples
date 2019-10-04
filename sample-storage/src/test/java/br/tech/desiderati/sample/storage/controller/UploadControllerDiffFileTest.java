/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage.controller;

import br.tech.desiderati.sample.storage.domain.Diff;
import br.tech.desiderati.sample.storage.domain.Offset;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Tests.
 */
@Slf4j
class UploadControllerDiffFileTest extends AbstractUploadControllerTest {

    @Test
    void diffFileShouldReturnSameFileEvenIfEmptyContent() throws Exception {
        MvcResult leftResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/1/left")
                    .contentType("application/txt")
                    .content(""))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult rightResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/1/right")
                    .contentType("application/txt")
                    .content(""))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult diffResult =
            mockMvc.perform(
                get(API_BASE_PATH + "/v1/diff/1"))
                .andExpect(status().isOk())
                .andReturn();

        Diff diff =
            mapper.readValue(diffResult.getResponse().getContentAsString(), Diff.class);
        assertTrue(diff.getSameFile());
    }

    @Test
    void diffFileShouldReturnSameFile() throws Exception {
        MvcResult leftResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/2/left")
                    .contentType("application/txt")
                    .content("123456"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult rightResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/2/right")
                    .contentType("application/txt")
                    .content("123456"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult diffResult =
            mockMvc.perform(
                get(API_BASE_PATH + "/v1/diff/2"))
                .andExpect(status().isOk())
                .andReturn();

        Diff diff =
            mapper.readValue(diffResult.getResponse().getContentAsString(), Diff.class);
        assertTrue(diff.getSameFile());
    }

    @Test
    void diffFileShouldReturnDiffSizeEvenIfLeftEmptyContent() throws Exception {
        MvcResult leftResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/3/left")
                    .contentType("application/txt")
                    .content(""))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult rightResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/3/right")
                    .contentType("application/txt")
                    .content("123456"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult diffResult =
            mockMvc.perform(
                get(API_BASE_PATH + "/v1/diff/3"))
                .andExpect(status().isOk())
                .andReturn();

        Diff diff =
            mapper.readValue(diffResult.getResponse().getContentAsString(), Diff.class);
        assertTrue(diff.getDiffSize());
    }

    @Test
    void diffFileShouldReturnDiffSizeEvenIfRigthEmptyContent() throws Exception {
        MvcResult leftResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/4/left")
                    .contentType("application/txt")
                    .content("123456"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult rightResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/4/right")
                    .contentType("application/txt")
                    .content(""))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult diffResult =
            mockMvc.perform(
                get(API_BASE_PATH + "/v1/diff/4"))
                .andExpect(status().isOk())
                .andReturn();

        Diff diff =
            mapper.readValue(diffResult.getResponse().getContentAsString(), Diff.class);
        assertTrue(diff.getDiffSize());
    }

    @Test
    void diffFileShouldReturnDiffSize() throws Exception {
        MvcResult leftResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/5/left")
                    .contentType("application/txt")
                    .content("123456"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult rightResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/5/right")
                    .contentType("application/txt")
                    .content("1234567"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult diffResult =
            mockMvc.perform(
                get(API_BASE_PATH + "/v1/diff/5"))
                .andExpect(status().isOk())
                .andReturn();

        Diff diff =
            mapper.readValue(diffResult.getResponse().getContentAsString(), Diff.class);
        assertTrue(diff.getDiffSize());
    }

    @Test
    void diffFileShouldReturnDifference() throws Exception {
        MvcResult leftResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/6/left")
                    .contentType("application/txt")
                    .content("123456789"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult rightResult =
            mockMvc.perform(
                post(API_BASE_PATH + "/v1/diff/6/right")
                    .contentType("application/txt")
                    .content("12ab56cde"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult diffResult =
            mockMvc.perform(
                get(API_BASE_PATH + "/v1/diff/6"))
                .andExpect(status().isOk())
                .andReturn();

        Diff diff =
            mapper.readValue(diffResult.getResponse().getContentAsString(), Diff.class);
        Offset offset1 = diff.getOffsets().get(0);
        assertEquals(2, offset1.getPosition());
        assertEquals(2, offset1.getLength());

        Offset offset2 = diff.getOffsets().get(1);
        assertEquals(6, offset2.getPosition());
        assertEquals(3, offset2.getLength());
    }
}
