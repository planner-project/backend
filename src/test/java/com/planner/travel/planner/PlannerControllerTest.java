package com.planner.travel.planner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.planner.travel.domain.planner.dto.request.PlannerCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlannerUpdateRequest;
import com.planner.travel.domain.planner.repository.PlannerRepository;
import com.planner.travel.domain.planner.service.PlannerListService;
import com.planner.travel.domain.profile.entity.Profile;
import com.planner.travel.domain.profile.repository.ProfileRepository;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import com.planner.travel.global.ApiDocumentUtil;
import com.planner.travel.global.jwt.token.TokenGenerator;
import com.planner.travel.global.jwt.token.TokenType;
import com.planner.travel.global.util.image.entity.Category;
import com.planner.travel.global.util.image.entity.Image;
import com.planner.travel.global.util.image.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class PlannerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlannerListService plannerListService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PlannerRepository plannerRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    private Long userId;

    private String validAccessToken;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @BeforeEach
    void setUp() {
        plannerRepository.deleteAll();
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

    private void createTestPlanners() {
        PlannerCreateRequest request1 = new PlannerCreateRequest(
                "테스트 플래너1",
                false
        );

        PlannerCreateRequest request2 = new PlannerCreateRequest(
                "테스트 플래너2",
                false
        );

        PlannerCreateRequest request3 = new PlannerCreateRequest(
                "테스트 플래너3",
                false
        );

        plannerListService.create(request1, userId);
        plannerListService.create(request2, userId);
        plannerListService.create(request3, userId);
    }

    @Test
    @DisplayName("플래너 리스트 반환")
    public void getAllPlanner() throws Exception {
        createTestPlanners();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/user/{userId}/planners", userId)
                        .header("Authorization", validAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("getAllPlanners",
                        ApiDocumentUtil.getDocumentRequest(),
                        ApiDocumentUtil.getDocumentResponse(),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("[].plannerId").description("플래너 인덱스"),
                                PayloadDocumentation.fieldWithPath("[].title").description("플래너 제목"),
                                PayloadDocumentation.fieldWithPath("[].startDate").description("여행 시작 날짜"),
                                PayloadDocumentation.fieldWithPath("[].endDate").description("여행 끝 날짜"),
                                PayloadDocumentation.fieldWithPath("[].isPrivate").description("플래너 공개 여부")
                        )
                ));
    }

    @Test
    @DisplayName("플래너 생성")
    public void createPlanner() throws Exception {
        PlannerCreateRequest request = new PlannerCreateRequest(
                "테스트 플래너1",
                false
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/user/{userId}/planners", userId)
                        .header("Authorization", validAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("createPlanner",
                        ApiDocumentUtil.getDocumentRequest(),
                        ApiDocumentUtil.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("title").description("플래너 제목"),
                                PayloadDocumentation.fieldWithPath("isPrivate").description("플래너 공개 여부")
                        )
                ));
    }

    @Test
    @DisplayName("플래너 수정")
    public void updatePlanner() throws Exception {
        createTestPlanners();

        PlannerUpdateRequest request = new PlannerUpdateRequest(
                "테스트 플래너99",
                false
        );

        System.out.println("============================================================================");
        System.out.println("before update: " + plannerListService.getAllPlanners(userId).get(2));
        System.out.println("============================================================================");

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/user/{userId}/planners/{plannerId}", userId, 1L)
                        .header("Authorization", validAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("updatePlanner",
                        ApiDocumentUtil.getDocumentRequest(),
                        ApiDocumentUtil.getDocumentResponse()
                ));

        System.out.println("============================================================================");
        System.out.println("After update: " + plannerListService.getAllPlanners(userId).get(2));
        System.out.println("============================================================================");
    }

    @Test
    @DisplayName("플래너 삭제")
    public void deletePlanner() throws Exception {
        createTestPlanners();

        System.out.println("============================================================================");
        System.out.println("Planner size before update: " + plannerListService.getAllPlanners(userId).size());
        System.out.println("============================================================================");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/user/{userId}/planners/{plannerId}", userId, 6L)
                        .header("Authorization", validAccessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("deletePlanner",
                        ApiDocumentUtil.getDocumentRequest(),
                        ApiDocumentUtil.getDocumentResponse()
                ));

        System.out.println("============================================================================");
        System.out.println("Planner size after update: " + plannerListService.getAllPlanners(userId).size());
        System.out.println("============================================================================");
    }
}
