package com.planner.travel.profile;

import com.planner.travel.domain.profile.entity.Profile;
import com.planner.travel.domain.profile.repository.ProfileRepository;
import com.planner.travel.domain.profile.service.ProfileImageService;
import com.planner.travel.domain.profile.service.UserInfoUpdateService;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import com.planner.travel.global.ApiDocumentUtil;
import com.planner.travel.global.jwt.token.TokenGenerator;
import com.planner.travel.global.jwt.token.TokenType;
import com.planner.travel.global.util.image.entity.Category;
import com.planner.travel.global.util.image.entity.Image;
import com.planner.travel.global.util.image.repository.ImageRepository;
import com.planner.travel.global.util.image.service.ImageDeleteService;
import com.planner.travel.global.util.image.service.ImageUpdateService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInfoUpdateService userInfoUpdateService;

    @MockBean
    private ProfileImageService profileImageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    private Long userId;

    private String validAccessToken;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        profileRepository.deleteAll();
        imageRepository.deleteAll();

        Image image = Image.builder()
                .imageUrl("")
                .isDeleted(false)
                .isThumb(false)
                .category(Category.PROFILE)
                .createdAt(LocalDateTime.now())
                .build();

        imageRepository.save(image);

        Profile profile = Profile.builder()
                .image(image)
                .build();

        profileRepository.save(profile);

        User user = User.builder()
                .email("wldsmtldsm65@gmail.com")
                .password("123qwe!#QWE")
                .nickname("시은")
                .isWithdrawal(false)
                .birthday(LocalDate.parse("1996-11-20"))
                .signupDate(LocalDateTime.now())
                .userTag(1234L)
                .profile(profile)
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

    @Test
    @DisplayName("유저 정보 수정 - 이미지")
    public void updateUserProfileImageTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "profileImage",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/v1/users/{userId}/image", userId)
                        .file(file)
                        .header("Authorization", validAccessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("updateProfileImage",
                        ApiDocumentUtil.getDocumentRequest(),
                        ApiDocumentUtil.getDocumentResponse()
                ));

        verify(profileImageService).update(eq(userId), any(MultipartFile.class));
    }

    @Test
    @DisplayName("유저 정보 수정 - 이미지 없는 경우")
    public void updateUserProfileImageToBasicTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/users/{userId}/image", userId)
                        .header("Authorization", validAccessToken))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("updateProfileImageToBasic",
                        ApiDocumentUtil.getDocumentRequest(),
                        ApiDocumentUtil.getDocumentResponse()
                ));

        verify(profileImageService).delete(userId);
    }
}
