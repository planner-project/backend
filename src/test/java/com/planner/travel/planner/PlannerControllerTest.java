package com.planner.travel.planner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.planner.travel.domain.group.dto.request.GroupMemberAddRequest;
import com.planner.travel.domain.group.repository.GroupMemberRepository;
import com.planner.travel.domain.group.service.GroupMemberService;
import com.planner.travel.domain.planner.dto.request.PlannerCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlannerDeleteRequest;
import com.planner.travel.domain.planner.dto.request.PlannerUpdateRequest;
import com.planner.travel.domain.planner.dto.response.PlannerListResponse;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private GroupMemberService groupMemberService;

    private Long userId1;

    private Long userId2;

    private String validAccessToken1;

    private String validAccessToken2;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        groupMemberRepository.deleteAll();
        plannerRepository.deleteAll();
        userRepository.deleteAll();
        profileRepository.deleteAll();
        imageRepository.deleteAll();

        Image image1 = Image.builder()
                .imageUrl("")
                .isDeleted(false)
                .isThumb(false)
                .category(Category.PROFILE)
                .createdAt(LocalDateTime.now())
                .build();

        imageRepository.save(image1);

        Profile profile1 = Profile.builder()
                .image(image1)
                .build();

        profileRepository.save(profile1);

        User user1 = User.builder()
                .email("wldsmtldsm65@gmail.com")
                .password("123qwe!#QWE")
                .nickname("시은")
                .isWithdrawal(false)
                .birthday(LocalDate.parse("1996-11-20"))
                .signupDate(LocalDateTime.now())
                .userTag(1234L)
                .provider("basic")
                .profile(profile1)
                .build();

        Image image2 = Image.builder()
                .imageUrl("")
                .isDeleted(false)
                .isThumb(false)
                .category(Category.PROFILE)
                .createdAt(LocalDateTime.now())
                .build();

        imageRepository.save(image2);

        Profile profile2 = Profile.builder()
                .image(image2)
                .build();

        profileRepository.save(profile2);

        User user2 = User.builder()
                .email("jieunnnn@gmail.com")
                .password("123qwe!#QWE")
                .nickname("지은")
                .isWithdrawal(false)
                .birthday(LocalDate.parse("1998-04-06"))
                .signupDate(LocalDateTime.now())
                .userTag(1234L)
                .provider("basic")
                .profile(profile2)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        userId1 = user1.getId();
        userId2 = user2.getId();

        validAccessToken1 = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(userId1));
        validAccessToken2 = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(userId2));
    }

    private void createTestPlanners() {
        PlannerCreateRequest request1 = new PlannerCreateRequest(
                "테스트 플래너1",
                false
        );

        PlannerCreateRequest request2 = new PlannerCreateRequest(
                "테스트 플래너2",
                true
        );

        PlannerCreateRequest request3 = new PlannerCreateRequest(
                "테스트 플래너3",
                false
        );

        plannerListService.create(request1, userId1);
        plannerListService.create(request2, userId1);
        plannerListService.create(request3, userId1);
    }

    @Test
    @DisplayName("플래너 리스트 반환")
    public void getAllPlanner() throws Exception {
        createTestPlanners();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{userId}/planners", userId1)
                        .header("Authorization", validAccessToken1)
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
    @DisplayName("플래너 리스트 반환 - 다른 유저")
    public void getAllPlannersOfOtherUser() throws Exception {
        createTestPlanners();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{userId}/planners", userId1)
                        .header("Authorization", validAccessToken2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("getAllPlannersOfOtherUser",
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

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", validAccessToken2);

        System.out.println("============================================================================");
        System.out.println("Planner size must be 2: " + plannerListService.getAllPlanners(userId1, true).size());
        System.out.println("============================================================================");
    }

    @Test
    @DisplayName("플래너 생성")
    public void createPlanner() throws Exception {
        PlannerCreateRequest request = new PlannerCreateRequest(
                "테스트 플래너1",
                false
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/{userId}/planners", userId1)
                        .header("Authorization", validAccessToken1)
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

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", validAccessToken1);

        System.out.println("============================================================================");
        System.out.println("before update: " + plannerListService.getAllPlanners(userId1, true).get(2));
        System.out.println("============================================================================");

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/users/{userId}/planners/{plannerId}", userId1, 4L)
                        .header("Authorization", validAccessToken1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("updatePlanner",
                        ApiDocumentUtil.getDocumentRequest(),
                        ApiDocumentUtil.getDocumentResponse()
                ));

        System.out.println("============================================================================");
        System.out.println("After update: " + plannerListService.getAllPlanners(userId1, true).get(2));
        System.out.println("============================================================================");
    }

    @Test
    @DisplayName("플래너 삭제")
    public void deletePlanner() throws Exception {
        createTestPlanners();

        PlannerDeleteRequest request = new PlannerDeleteRequest(userId1);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", validAccessToken1);

        List<PlannerListResponse> user1Planners = plannerListService.getAllPlanners(userId1, true);
        GroupMemberAddRequest groupMemberAddRequest = new GroupMemberAddRequest(userId2);

        for(PlannerListResponse planner : user1Planners) {
//            System.out.println("============================================================================");
//            System.out.println("Planner id: " + planner.plannerId());
//            System.out.println("============================================================================");

            groupMemberService.addGroupMembers(planner.plannerId(), groupMemberAddRequest);
        }

        System.out.println("============================================================================");
        System.out.println("UserId1 planner size before update: " + plannerListService.getAllPlanners(userId1, true).size());
        System.out.println("UserId2 planner size before update(login): " + plannerListService.getAllPlanners(userId2, true).size());
        System.out.println("UserId2 planner size before update(notLogin): " + plannerListService.getAllPlanners(userId2, false).size());
        System.out.println("============================================================================");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/users/{userId}/planners/{plannerId}", userId1, 9L)
                        .header("Authorization", validAccessToken1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("deletePlanner",
                        ApiDocumentUtil.getDocumentRequest(),
                        ApiDocumentUtil.getDocumentResponse(),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("userId").description("유저 인덱스")
                        )
                ));

        System.out.println("============================================================================");
        System.out.println("UserId1 planner size after update: " + plannerListService.getAllPlanners(userId1, true).size());
        System.out.println("UserId2 planner size before update(login): " + plannerListService.getAllPlanners(userId2, true).size());
        System.out.println("UserId2 planner size before update(notLogin): " + plannerListService.getAllPlanners(userId2, false).size());
        System.out.println("============================================================================");
    }
}
