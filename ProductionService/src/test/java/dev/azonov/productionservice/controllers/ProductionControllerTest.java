package dev.azonov.productionservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.azonov.productionservice.model.ProduceRequest;
import dev.azonov.productionservice.services.ProductionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductionController.class)
class ProductionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductionService productionServiceMock;

    @MockBean
    private RestTemplate restTemplateMock;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void produceGiftShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                post("/produce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createRequest("car"))))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("InvalidInputs")
    public void produceGiftShouldReturnBadRequestForInvalidInput(ProduceRequest request) throws Exception {
        mockMvc.perform(
                        post("/produce")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void produceGiftShouldStartProduction() throws Exception {
        String kind = "car";

        mockMvc.perform(
                        post("/produce")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(createRequest(kind))))
                .andExpect(status().isOk());

        verify(productionServiceMock, times(1)).startProduction(kind);
    }

    private static Stream<ProduceRequest> InvalidInputs() {
        return Stream.of(
                createRequest("veryveryveryveryveryveryveryveryveryveryverylongkind"),
                createRequest(null),
                createRequest(""));
    }

    private String toJson(ProduceRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }

    private static ProduceRequest createRequest(String kind) {
        ProduceRequest request = new ProduceRequest();
        request.setKind(kind);
        return request;
    }
}