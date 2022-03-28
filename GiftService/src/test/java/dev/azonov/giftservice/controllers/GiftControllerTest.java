package dev.azonov.giftservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.azonov.giftservice.model.GiftModel;
import dev.azonov.giftservice.model.PopulationRequest;
import dev.azonov.giftservice.service.GiftService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GiftController.class)
class GiftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GiftService giftServiceMock;

    // There is no Bean for ObjectMapper so getting Jackson converter like this
    @Autowired
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;

    private String toJson(List<GiftModel> gifts) throws JsonProcessingException {
        return springMvcJacksonConverter.getObjectMapper().writeValueAsString(gifts);
    }

    private String toJson(PopulationRequest request) throws JsonProcessingException {
        return springMvcJacksonConverter.getObjectMapper().writeValueAsString(request);
    }

    private List<GiftModel> getExpectedKinds() {
        List<GiftModel> expectedKinds = new ArrayList<>();

        var kind1 = new GiftModel();
        kind1.setKind("car");
        var kind2 = new GiftModel();
        kind2.setKind("doll");
        expectedKinds.add(kind1);
        expectedKinds.add(kind2);

        return expectedKinds;
    }

    @Test
    void findAllKindsShouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/gifts")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void findAllKindsShouldCallService() throws Exception {
        mockMvc.perform(get("/gifts")).andDo(print());

        verify(giftServiceMock, times(1)).findAll();
    }

    @Test
    void findAllKindsShouldReturnJson() throws Exception {
        List<GiftModel> expectedKinds = getExpectedKinds();
        when(giftServiceMock.findAll()).thenReturn(expectedKinds);

        MvcResult mvcResult = mockMvc.perform(get("/gifts")).andReturn();

        String expectedResponseBody = toJson(expectedKinds);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    @Test
    void getByKindShouldReturnOkStatus() throws Exception {
        String kind = "car";
        when(giftServiceMock.get(kind)).thenReturn(new GiftModel());

        mockMvc.perform(get("/gifts/" + kind)).andExpect(status().isOk());
    }

    @Test
    void getByKindShouldReturnNotFoundForUnknownKind() throws Exception {
        mockMvc.perform(get("/gifts/car")).andExpect(status().isNotFound());
    }

    @Test
    void getByKindShouldReturnBadRequestForTooLongKind() throws Exception {
        String longKindName = "veryveryveryveryveryveryveryveryveryveryveryveryverylongkind";
        mockMvc.perform(get("/gifts/" + longKindName)).andExpect(status().isBadRequest());
    }

    @Test
    void increaseQuantityReturnOkStatus() throws Exception {
        PopulationRequest request = new PopulationRequest(1);

        mockMvc.perform(
                post("/gifts/car/populate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isOk());
    }

    @Test
    void increaseQuantityUsesGiftService() throws Exception {
        int increment = 4;
        PopulationRequest request = new PopulationRequest(increment);

        mockMvc.perform(
                        post("/gifts/car/populate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(request)));

        verify(giftServiceMock, times(1)).increaseQuantity("car", increment);
    }

    @ParameterizedTest
    @MethodSource("InvalidInputs")
    public void increaseQualityInvalidInput(PopulationRequest request) throws Exception {
        mockMvc.perform(
                post("/gifts/car/populate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    public static Stream<PopulationRequest> InvalidInputs() {
        return Stream.of(
                new PopulationRequest(0),
                new PopulationRequest(51),
                new PopulationRequest(-10),
                new PopulationRequest(100));
    }
}