package dev.pjatk.recipeapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pjatk.recipeapp.SpringIntegrationTest;
import dev.pjatk.recipeapp.dto.request.RegisterDTO;
import dev.pjatk.recipeapp.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static dev.pjatk.recipeapp.controller.RegistrationController.REGISTER;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringIntegrationTest
@AutoConfigureMockMvc
//@WebMvcTest(RegisterController.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class RegistrationControllerIT {

    private static final String REGISTER_PATH = REGISTER;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private IUserService userService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    void shouldNotBeSecured() throws Exception {
        // given
        var body = new RegisterDTO("def@gmail.com", "password123!@#");

        mockMvc.perform(
                post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(body))
                        .with(csrf())
        )
                .andExpect(status().isCreated())
                .andDo(document("register-example",
                                requestFields(
                                        fieldWithPath("email").description("User email"),
                                        fieldWithPath("password").description("User password")
                                )));
    }

}