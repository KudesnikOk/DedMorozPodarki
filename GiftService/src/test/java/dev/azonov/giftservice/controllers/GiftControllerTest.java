package dev.azonov.giftservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.azonov.giftservice.model.GiftKind;
import dev.azonov.giftservice.service.GiftKindService;
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
    private GiftKindService giftKindServiceMock;

    // There is no Bean for ObjectMapper so getting Jackson converter like this
    @Autowired
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;

    private String toJson(List<GiftKind> giftKinds) throws JsonProcessingException {
        return springMvcJacksonConverter.getObjectMapper().writeValueAsString(giftKinds);
    }

    private List<GiftKind> getExpectedKinds() {
        List<GiftKind> expectedKinds = new ArrayList<>();

        var kind1 = new GiftKind();
        kind1.setName("car");
        var kind2 = new GiftKind();
        kind2.setName("doll");
        expectedKinds.add(kind1);
        expectedKinds.add(kind2);

        return expectedKinds;
    }

    @Test
    void findAllKindsShouldReturnOkStatus() throws Exception {
        mockMvc.perform(get("/gifts/kinds")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void findAllKindsShouldCallService() throws Exception {
        mockMvc.perform(get("/gifts/kinds")).andDo(print());

        verify(giftKindServiceMock, times(1)).findAll();
    }

    @Test
    void findAllKindsShouldReturnJson() throws Exception {
        List<GiftKind> expectedKinds = getExpectedKinds();
        when(giftKindServiceMock.findAll()).thenReturn(expectedKinds);

        MvcResult mvcResult = mockMvc.perform(get("/gifts/kinds")).andReturn();

        String expectedResponseBody = toJson(expectedKinds);
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
    }
}