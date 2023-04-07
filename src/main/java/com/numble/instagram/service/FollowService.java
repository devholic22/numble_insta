package com.numble.instagram.service;

import com.numble.instagram.entity.Follow;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.*;
import com.numble.instagram.repository.FollowRepository;
import com.numble.instagram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public void addFollow(Long userId, User sender) {

        if (sender == null) {
            throw new NotLoggedInException("로그인되지 않았습니다.");
        }

        if (!sender.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        if (sender.getId().equals(userId)) {
            throw new SelfFollowAPIException("자기 자신과는 팔로우 API가 이뤄질 수 없습니다.");
        }

        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new NotSearchedTargetException("사용자가 없습니다."));

        if (!receiver.isActivated()) {
            throw new ExitedTargetUserException("탈퇴한 유저라 불가능합니다.");
        }

        if (followRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiver.getId()) != null) {
            throw new AlreadyFollowException("이미 팔로우가 맺어져 있습니다.");
        }

        Follow newFollow = Follow.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        followRepository.save(newFollow);
    }

    public void cancelFollow(Long userId, User sender) {

        if (sender == null) {
            throw new NotLoggedInException("로그인되지 않았습니다.");
        }

        if (!sender.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        if (sender.getId().equals(userId)) {
            throw new SelfFollowAPIException("자기 자신과는 팔로우 API가 이뤄질 수 없습니다.");
        }

        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new NotSearchedTargetException("사용자가 없습니다."));

        Follow isExistFollow = followRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiver.getId());

        if (isExistFollow == null) {
            throw new NotFollowException("팔로우 관계가 맺어지지 않은 관계입니다.");
        }

        followRepository.deleteBySender_IdAndReceiver_Id(sender.getId(), receiver.getId());
    }
}
