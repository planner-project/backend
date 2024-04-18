package com.planner.travel.domain.user.controller;

import com.planner.travel.domain.user.dto.response.UserInfoResponse;
import com.planner.travel.domain.user.service.UserInfoService;
import com.planner.travel.global.jwt.token.SubjectExtractor;
import com.planner.travel.global.jwt.token.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth/users")
@RequiredArgsConstructor
public class UserInfoController {
    private TokenExtractor tokenExtractor;
    private SubjectExtractor subjectExtractor;
    private UserInfoService userInfoService;


//    public ResponseEntity<UserInfoResponse> getUserInfo(HttpServletRequest request){
//        String token = tokenExtractor.getAccessTokenFromHeader(request);
//        Long userId = subjectExtractor.getUesrIdFromToken(token);
//        // 반환받은 userId 를 필요한 메서드에 넣어주세요.
//    }

}
