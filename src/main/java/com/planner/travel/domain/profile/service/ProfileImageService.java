package com.planner.travel.domain.profile.service;

import com.planner.travel.domain.user.component.UserFinder;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import com.planner.travel.global.util.image.entity.Category;
import com.planner.travel.global.util.image.service.ImageUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ProfileImageService {
    private UserRepository userRepository;
    private ImageUpdateService updateService;
    private UserFinder userFinder;

    private void update(Long userId, MultipartFile multipartFile, Category category) throws Exception {
        Path path = updateService.saveImage(userId, multipartFile, category);
        User user = userFinder.find(userId);
        user.getProfile()
                .getImage()
                .updateImageUrl(path.toString());

        userRepository.save(user);
    }
}
