package com.planner.travel.domain.profile.controller;

import com.planner.travel.domain.profile.dto.request.UserInfoUpdateRequest;
import com.planner.travel.domain.profile.service.UserInfoUpdateService;
import com.planner.travel.global.util.image.entity.Category;
import com.planner.travel.global.util.image.service.ImageDeleteService;
import com.planner.travel.global.util.image.service.ImageUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/users/")
@RequiredArgsConstructor
public class ProfileController {
    private final UserInfoUpdateService userInfoUpdateService;
    private final ImageDeleteService imageDeleteService;
    private final ImageUpdateService imageUpdateService;

    @GetMapping(value = "/{userId}")
    public void getProfile(@PathVariable("userId") Long userId) {

    }

    @PatchMapping(value = "/{userId}/image")
    public void updateProfileImage(@PathVariable("userId") Long userId, MultipartFile multipartFile) throws Exception {
        Category category = Category.valueOf("PROFILE");
        imageDeleteService.deleteImage(userId, category);
        imageUpdateService.saveImage(userId, multipartFile, category);
    }

    @PatchMapping(value = "/{userId}/info")
    public void updateUserInfo(@PathVariable("userId") Long userId, @RequestBody UserInfoUpdateRequest request) {
        userInfoUpdateService.update(userId, request);
    }

    @PatchMapping(value = "/{userId}/withdrawal")
    public void withdrawal(@PathVariable("userId") Long userId) {
        userInfoUpdateService.withdrawal(userId);
    }
}
