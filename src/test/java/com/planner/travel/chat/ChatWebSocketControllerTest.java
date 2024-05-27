package com.planner.travel.chat;

import com.planner.travel.domain.chat.dto.ChatDto;
import com.planner.travel.domain.group.entity.GroupMember;
import com.planner.travel.domain.group.repository.GroupMemberRepository;
import com.planner.travel.domain.group.service.GroupMemberService;
import com.planner.travel.domain.planner.entity.Planner;
import com.planner.travel.domain.planner.repository.PlannerRepository;
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
public class ChatWebSocketControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebSocketStompClient webSocketStompClient;

    private StompSession stompSession1;
    private StompSession stompSession2;

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

        GroupMember groupMember2 = GroupMember.builder()
                .user(user2)
                .planner(planner)
                .isLeaved(false)
                .isHost(true)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        plannerRepository.save(planner);
        groupMemberRepository.save(groupMember1);
        groupMemberRepository.save(groupMember2);

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

        WebSocketHttpHeaders handshakeHeaders1 = new WebSocketHttpHeaders();
        handshakeHeaders1.add("Authorization", validAccessToken1);

        StompHeaders connectHeaders1 = new StompHeaders();
        connectHeaders1.add("Authorization", validAccessToken1);

        WebSocketHttpHeaders handshakeHeaders2 = new WebSocketHttpHeaders();
        handshakeHeaders2.add("Authorization", validAccessToken2);

        StompHeaders connectHeaders2 = new StompHeaders();
        connectHeaders2.add("Authorization", validAccessToken2);

        System.out.println("============================================================================");
        System.out.println("userId: " + userId1);
        System.out.println("validAccessToken1: " + validAccessToken1);
        System.out.println("Connecting to WebSocket with headers: " + connectHeaders1);
        System.out.println("============================================================================");
        System.out.println("userId: " + userId2);
        System.out.println("validAccessToken1: " + validAccessToken2);
        System.out.println("Connecting to WebSocket with headers: " + connectHeaders2);
        System.out.println("============================================================================");

        stompSession1 = webSocketStompClient
                .connect(url, handshakeHeaders1, connectHeaders1, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        stompSession2 = webSocketStompClient
                .connect(url, handshakeHeaders2, connectHeaders2, new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        stompSession1.subscribe("/sub/planner/" + plannerId, new StompFrameHandler() {
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

        stompSession2.subscribe("/sub/planner/" + plannerId, new StompFrameHandler() {
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
    @DisplayName("실시간 채팅")
    public void testChatting() throws Exception {
        User user1 = userRepository.findById(userId1).get();
        User user2 = userRepository.findById(userId2).get();

        // 유저 1 이 보냄
        ChatDto chatDto1 = new ChatDto(user1.getId(), user1.getNickname(), "", "안녕 난 유저 1 이야.");

        StompHeaders headers1 = new StompHeaders();
        headers1.setDestination("/pub/planner/" + plannerId + "/chat/send");
        headers1.add("Authorization", validAccessToken1);

        stompSession1.send(headers1, chatDto1);

        Map<String, Object> response1 = blockingQueue.poll(5, TimeUnit.SECONDS);
        assertNotNull(response1);
        assertEquals("chat", response1.get("type"));

        // 유저 2 가 보냄
        ChatDto chatDto2 = new ChatDto(user2.getId(), user2.getNickname(), "", "반가웡! 난 유저 2 야.");

        StompHeaders headers2 = new StompHeaders();
        headers2.setDestination("/pub/planner/" + plannerId + "/chat/send");
        headers2.add("Authorization", validAccessToken2);

        stompSession2.send(headers2, chatDto2);

        Map<String, Object> response2 = blockingQueue.poll(5, TimeUnit.SECONDS);
        assertNotNull(response2);
        assertEquals("chat", response2.get("type"));

    }
}
