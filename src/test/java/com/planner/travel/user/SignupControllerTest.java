package com.planner.travel.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.planner.travel.domain.user.controller.UserController;
import com.planner.travel.domain.user.dto.response.SignupRequest;
import com.planner.travel.domain.user.service.LoginService;
import com.planner.travel.domain.user.service.SignupService;
import com.planner.travel.global.ApiDocumentUtil;
import com.planner.travel.global.util.CookieUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser
@AutoConfigureRestDocs
public class SignupControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignupService signupService;
    @MockBean
    private LoginService loginService;
    @MockBean
    private CookieUtil cookieUtil;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @DisplayName("회원 가입")
    @Test
    void signup() throws Exception {
        SignupRequest request = new SignupRequest(
                "wldsmtldsm65@gmail.com",
                "123qwe!@#QWE",
                "시니",
                LocalDate.parse("1996-11-20")
        );

        doNothing()
                .when(signupService)
                .signup(any(SignupRequest.class));

        mockMvc
                .perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("signup",
                        ApiDocumentUtil.getDocumentRequest(),
                        ApiDocumentUtil.getDocumentResponse()));

        verify(signupService, times(1)).signup(any(SignupRequest.class));
    }

}