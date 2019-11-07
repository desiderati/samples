/*
 * Copyright (c) 2019 - Felipe Desiderati
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
