package dev.azonov.giftservice.controllers;

import dev.azonov.giftservice.service.IDeliveryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDeliveryService deliveryServiceMock;

    @MockBean
    private RestTemplate restTemplateMock;

    @Test
    void findAllShouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/deliveries")).andExpect(status().isOk());
    }

    @Test
    void findAllShouldCallDeliveryService() throws Exception {
        mockMvc.perform(get("/deliveries"));

        Mockito.verify(deliveryServiceMock, Mockito.times(1)).findAll();
    }
}