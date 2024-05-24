package com.planner.travel.global.util.search;

import com.planner.travel.global.util.search.dto.UserSearchResponse;
import com.planner.travel.global.util.search.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserSearchController {
    private final UserSearchService userSearchService;

    @GetMapping("")
    public ResponseEntity<UserSearchResponse> findUser(@RequestParam("email") String email) {
        UserSearchResponse userSearchResponse = userSearchService.findUserByEmail(email);
        return ResponseEntity.ok(userSearchResponse);
    }
}
