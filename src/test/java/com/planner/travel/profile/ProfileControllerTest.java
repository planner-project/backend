package com.planner.travel.profile;

import com.planner.travel.domain.profile.service.UserInfoUpdateService;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import com.planner.travel.global.ApiDocumentUtil;
import com.planner.travel.global.jwt.token.SubjectExtractor;
import com.planner.travel.global.jwt.token.TokenExtractor;
import com.planner.travel.global.jwt.token.TokenGenerator;
import com.planner.travel.global.jwt.token.TokenType;
import com.planner.travel.global.util.image.service.ImageDeleteService;
import com.planner.travel.global.util.image.service.ImageUpdateService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInfoUpdateService userInfoUpdateService;

    @Autowired
    private ImageDeleteService imageDeleteService;

    @Autowired
    private ImageUpdateService imageUpdateService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    private Long userId;

    private String validAccessToken;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = User.builder()
                .email("wldsmtldsm65@gmail.com")
                .password("123qwe!#QWE")
                .nickname("시은")
                .isWithdrawal(false)
                .birthday(LocalDate.parse("1996-11-20"))
                .signupDate(LocalDateTime.now())
                .userTag(1234L)
                .build();

        userRepository.save(user);
        userId = user.getId();

        validAccessToken = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(userId));
    }

    @Test
    @DisplayName("유저 회원 탈퇴")
    public void userWithdrawalTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/users/withdrawal")
                        .header("Authorization", validAccessToken))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("userWithdrawal",
                        ApiDocumentUtil.getDocumentRequest(),
                        ApiDocumentUtil.getDocumentResponse()
                ));

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        assert user.isWithdrawal();
    }
}
