package com.planner.travel.groupMember;

import com.planner.travel.domain.group.dto.request.GroupMemberAddRequest;
import com.planner.travel.domain.group.dto.request.GroupMemberDeleteRequest;
import com.planner.travel.domain.group.entity.GroupMember;
import com.planner.travel.domain.group.repository.GroupMemberRepository;
import com.planner.travel.domain.group.service.GroupMemberService;
import com.planner.travel.domain.planner.dto.request.PlannerCreateRequest;
import com.planner.travel.domain.planner.entity.Planner;
import com.planner.travel.domain.planner.repository.PlannerRepository;
import com.planner.travel.domain.planner.service.PlannerListService;
import com.planner.travel.domain.profile.entity.Profile;
import com.planner.travel.domain.profile.repository.ProfileRepository;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import com.planner.travel.global.jwt.token.TokenAuthenticator;
import com.planner.travel.global.jwt.token.TokenGenerator;
import com.planner.travel.global.jwt.token.TokenType;
import com.planner.travel.global.util.image.entity.Category;
import com.planner.travel.global.util.image.entity.Image;
import com.planner.travel.global.util.image.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupMemberWebSocketControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebSocketStompClient webSocketStompClient;

    private StompSession stompSession;

    private BlockingQueue<Map<String, Object>> blockingQueue;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

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
    private TokenAuthenticator tokenAuthenticator;

    @Autowired
    private GroupMemberService groupMemberService;

    @Autowired
    private PlannerListService plannerListService;

    private Long userId1;

    private Long userId2;

    private Long plannerId;

    private String validAccessToken1;

    private String validAccessToken2;

    void setUsers() {
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
                .profile(profile2)
                .build();

        Planner planner = Planner.builder()
                .title("테스트 플래너1")
                .isPrivate(false)
                .isDeleted(false)
                .build();

        GroupMember groupMember1 = GroupMember.builder()
                .user(user1)
                .planner(planner)
                .isLeaved(false)
                .isHost(true)
                .build();


        userRepository.save(user1);
        userRepository.save(user2);
        plannerRepository.save(planner);
        groupMemberRepository.save(groupMember1);

        userId1 = user1.getId();
        userId2 = user2.getId();
        plannerId = planner.getId();

        validAccessToken1 = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(userId1));
        validAccessToken2 = "Bearer " + tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(userId2));
    }

    @BeforeEach
    public void webSocketSetup() throws Exception {
        setUsers();

        blockingQueue = new LinkedBlockingDeque<>();
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = "ws://localhost:" + port + "/ws";

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        handshakeHeaders.add("Authorization", validAccessToken1);

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", validAccessToken1);

        System.out.println("============================================================================");
        System.out.println("userId: " + userId1);
        System.out.println("validAccessToken1: " + validAccessToken1);
        System.out.println("Connecting to WebSocket with headers: " + connectHeaders);
        System.out.println("============================================================================");

        stompSession = webSocketStompClient
                .connect(url, handshakeHeaders, connectHeaders, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        stompSession.subscribe("/sub/planner/" + plannerId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Map.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("============================================================================");
                blockingQueue.add((Map<String, Object>) payload);
                System.out.println(blockingQueue.toString());
                System.out.println("============================================================================");
            }
        });
    }

    @Test
    @DisplayName("그룹 멤버 추가")
    public void testAddGroupMember() throws Exception {
        GroupMemberAddRequest addRequest = new GroupMemberAddRequest(userId2);

        StompHeaders headers = new StompHeaders();
        headers.setDestination("/pub/planner/" + plannerId + "/members/create");
        headers.add("Authorization", validAccessToken1);

        stompSession.send(headers, addRequest);

        Map<String, Object> response = blockingQueue.poll(5, TimeUnit.SECONDS);
        assertNotNull(response);
        assertEquals("add-group-member", response.get("type"));
    }

    @Test
    @DisplayName("그룹 멤버 삭제")
    public void testDeleteGroupMember() throws Exception {
        GroupMember groupMember = GroupMember.builder()
                .isHost(false)
                .isLeaved(false)
                .planner(plannerRepository.findById(plannerId).get())
                .build();

        groupMemberRepository.save(groupMember);

        GroupMemberDeleteRequest deleteRequest = new GroupMemberDeleteRequest(groupMember.getId());

        StompHeaders headers = new StompHeaders();
        headers.setDestination("/pub/planner/" + plannerId + "/members/" + groupMember.getId() + "/delete");
        headers.add("Authorization", validAccessToken1);

        stompSession.send(headers, deleteRequest);

        Map<String, Object> response = blockingQueue.poll(5, TimeUnit.SECONDS);
        assertNotNull(response);
        assertEquals("delete-group-member", response.get("type"));
    }
}
