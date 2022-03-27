package dev.azonov.giftservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.azonov.giftservice.exceptions.GiftNotFoundException;
import dev.azonov.giftservice.exceptions.GiftOutOfStockException;
import dev.azonov.giftservice.model.MailRequest;
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

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MailController.class)
class MailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // There is no Bean for ObjectMapper so getting Jackson converter like this
    @Autowired
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;

    @MockBean
    private GiftService giftServiceMock;

    private String toJson(MailRequest request) throws JsonProcessingException {
        return springMvcJacksonConverter.getObjectMapper().writeValueAsString(request);
    }

    private static MailRequest createRequest(String firstName, String secondName, String middleName, String giftKind) {
        MailRequest request = new MailRequest();
        request.setFirstName(firstName);
        request.setSecondName(secondName);
        request.setMiddleName(middleName);
        request.setGiftKind(giftKind);

        return request;
    }

    @Test
    public void shouldReturnOkStatus() throws Exception {
        MailRequest request = createRequest("Michael", "Black", null, "constructor");

        mockMvc.perform(
                post("/mails/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("InvalidInputs")
    public void shouldReturnBadRequestForInvalidInput(MailRequest request) throws Exception {
        mockMvc.perform(
                        post("/mails/process")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void processMailShouldReturnBadRequestForUnknownKind() throws Exception {
        MailRequest request = createRequest("Michael", "Black", null, "constructor");

        doThrow(new GiftNotFoundException("constructor")).when(giftServiceMock).sendGift(any());

        mockMvc.perform(
                        post("/mails/process")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void processMailShouldReturnBadRequestForOutOfStockKind() throws Exception {
        MailRequest request = createRequest("Michael", "Black", null, "constructor");

        doThrow(new GiftOutOfStockException("constructor")).when(giftServiceMock).sendGift(any());

        mockMvc.perform(
                        post("/mails/process")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    public static Stream<MailRequest> InvalidInputs() {
        return Stream.of(
                createRequest(null, null, null, null),
                createRequest(null, "SecondName", "MiddleName", "Gift"),
                createRequest("FirstName", null, "MiddleName", "Gift"),
                createRequest("FirstName", "SecondName", "MiddleName", null),
                createRequest("VeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongName", "SecondName", "MiddleName", "Gift"),
                createRequest("FirstName", "VeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongName", "MiddleName", "Gift"),
                createRequest("FirstName", "SecondName", "VeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongName", "Gift"),
                createRequest("FirstName", "SecondName", "MiddleName", "VeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongGiftName"),
                createRequest("A", "SecondName", "MiddleName", "Gift"),
                createRequest("FirstName", "B", "MiddleName", "Gift"));
    }
}