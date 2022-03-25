package dev.azonov.giftservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.azonov.giftservice.model.Gift;
import dev.azonov.giftservice.service.GiftService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private String toJson(List<Gift> gifts) throws JsonProcessingException {
        return springMvcJacksonConverter.getObjectMapper().writeValueAsString(gifts);
    }

    private List<Gift> getExpectedKinds() {
        List<Gift> expectedKinds = new ArrayList<>();

        var kind1 = new Gift();
        kind1.setKind("car");
        var kind2 = new Gift();
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
        List<Gift> expectedKinds = getExpectedKinds();
        when(giftServiceMock.findAll()).thenReturn(expectedKinds);

        MvcResult mvcResult = mockMvc.perform(get("/gifts")).andReturn();

        String expectedResponseBody = toJson(expectedKinds);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }
}