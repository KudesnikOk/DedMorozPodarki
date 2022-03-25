package dev.azonov.giftservice.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GiftsController.class)
class GiftsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnOkStatus() throws Exception {
        String requestJson = "{\"firstName\":\"Michael\", \"secondName\":\"Black\", \"giftKind\": \"constructor\"}";
        mockMvc.perform(
                post("/gifts/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

}