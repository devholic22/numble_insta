package com.numble.instagram.service;

import com.numble.instagram.dto.FollowDto;
import com.numble.instagram.entity.Follow;
import com.numble.instagram.entity.User;
import com.numble.instagram.repository.FollowRepository;
import com.numble.instagram.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public void addFollow(FollowDto followDto) {
        User sender = getLoggedInUser();
        if (sender.getId() == followDto.getUser_id()) {
            throw new RuntimeException("자기 자신과는 팔로우 API가 이뤄질 수 없습니다.");
        }
        Optional<User> receiver = userRepository.findById(followDto.getUser_id());
        if (receiver.isEmpty()) {
            throw new RuntimeException("사용자가 없습니다.");
        }
        if (followRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiver.get().getId()) != null) {
            throw new RuntimeException("이미 팔로우가 맺어져 있습니다.");
        }
        Follow newFollow = Follow.builder()
                .sender(sender)
                .receiver(receiver.get())
                .build();
        followRepository.save(newFollow);
    }

    public void cancelFollow(FollowDto followDto) {
        User sender = getLoggedInUser();
        if (sender.getId() == followDto.getUser_id()) {
            throw new RuntimeException("자기 자신과는 팔로우 API가 이뤄질 수 없습니다.");
        }
        Optional<User> receiver = userRepository.findById(followDto.getUser_id());
        if (receiver.isEmpty()) {
            throw new RuntimeException("사용자가 없습니다.");
        }
        Follow isExistFollow = followRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiver.get().getId());
        if (isExistFollow == null) {
            throw new RuntimeException("팔로우 관계가 맺어지지 않은 관계입니다.");
        }
        followRepository.deleteBySender_IdAndReceiver_Id(sender.getId(), receiver.get().getId());
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();
        return userRepository.findByNickname(nickname);
    }
}
