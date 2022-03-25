package dev.azonov.giftservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.azonov.giftservice.model.MailRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GiftsController.class)
class GiftsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // There is no Bean for ObjectMapper so getting Jackson converter like this
    @Autowired
    private MappingJackson2HttpMessageConverter springMvcJacksonConverter;

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
                post("/gifts/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("InvalidInputs")
    public void shouldReturnBadRequestForInvalidInput(MailRequest request) throws Exception {
        mockMvc.perform(
                        post("/gifts/request")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(request)))
                .andDo(print())
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
                createRequest("FirstName", "B", "MiddleName", "Gift"),
                createRequest("FirstName", "SecondName", "MiddleName", "C"));
    }
}