package com.errasoft.friendfinder.service.impl;

import com.errasoft.friendfinder.controller.vm.FriendResponseVM;
import com.errasoft.friendfinder.dto.FriendshipDto;
import com.errasoft.friendfinder.mapper.FriendshipMapper;
import com.errasoft.friendfinder.model.Friendship;
import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.repo.FriendshipRepo;
import com.errasoft.friendfinder.repo.security.AccountRepo;
import com.errasoft.friendfinder.service.FriendshipService;
import com.errasoft.friendfinder.service.NotificationService;
import com.errasoft.friendfinder.service.security.AuthService;
import com.errasoft.friendfinder.utils.FriendshipStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    private FriendshipRepo friendshipRepo;
    private AccountRepo accountRepo;
    private FriendshipMapper friendshipMapper;
    private AuthService authService;
    private NotificationService notificationService;

    public FriendshipServiceImpl(FriendshipRepo friendshipRepo,
                                 FriendshipMapper friendshipMapper,
                                 AccountRepo accountRepo,
                                 AuthService authService,
                                 NotificationService notificationService) {
        this.friendshipRepo = friendshipRepo;
        this.friendshipMapper = friendshipMapper;
        this.accountRepo = accountRepo;
        this.authService = authService;
        this.notificationService = notificationService;
    }

    @Override
    public FriendshipDto sendRequest(Long receiverId) {

        Long requesterId = authService.getCurrentUserId();

        if (requesterId.equals(receiverId)) {
            throw new RuntimeException("cannot.send.request.to.yourself");
        }

        Account requester = accountRepo.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("account.not.found"));

        Account receiver = accountRepo.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("account.not.found"));

        boolean exists = friendshipRepo.existsActiveFriendship(requesterId, receiverId);

        if (exists) {
            throw new RuntimeException("friendship.already.exists.or.pending");
        }

        Friendship friendship = new Friendship();
        friendship.setRequester(requester);
        friendship.setReceiver(receiver);
        friendship.setStatus(FriendshipStatus.PENDING);

        notificationService.handleNotification(requester,receiver,null,"NEW_REQUEST",null);
        return friendshipMapper.toFriendshipDto(friendshipRepo.save(friendship));
    }

    @Override
    public void acceptRequest(Long friendshipId) {
        Friendship friendship = friendshipRepo.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("request.not.found"));

        Long currentUserId = authService.getCurrentUserId();
        if (!friendship.getReceiver().getId().equals(currentUserId)) {
            throw new RuntimeException("unauthorized.action");
        }
        validatePending(friendship);
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepo.save(friendship);
        notificationService.handleNotification(friendship.getRequester(),friendship.getReceiver(),null,"ACCEPT_REQUEST",null);
    }

    @Override
    public void rejectRequest(Long friendshipId) {
        Friendship friendship = friendshipRepo.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("request.not.found"));

        Long currentUserId = authService.getCurrentUserId();
        if (!friendship.getReceiver().getId().equals(currentUserId)) {
            throw new RuntimeException("unauthorized.action");
        }
        validatePending(friendship);
        friendship.setStatus(FriendshipStatus.REJECTED);
        friendshipRepo.save(friendship);
    }

    @Override
    public void cancelRequest(Long friendshipId) {
        Friendship friendship = friendshipRepo.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("request.not.found"));

        //Long currentUserId = authService.getCurrentUserId();
        //if (!friendship.getRequester().getId().equals(currentUserId)) {
        //    throw new RuntimeException("unauthorized.action");
        //}

       // validatePending(friendship);
        friendship.setStatus(FriendshipStatus.CANCELLED);
        friendshipRepo.save(friendship);
    }

    @Override
    public List<FriendResponseVM> getMyFriends() {
        Long currentId = authService.getCurrentUserId();
        List<Long> ids = friendshipRepo.findAcceptedFriendIds(currentId);

        List<Account> friends = accountRepo.findByIdInAndEnabledTrue(ids);

        return getFriendResponseVMS(currentId, friends);
    }

    @Override
    public List<FriendResponseVM> getUserFriends(Long userId) {

         accountRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("account.not.found"));

        List<Long> friendIds = friendshipRepo.findAcceptedFriendIds(userId);

        List<Account> friends = accountRepo.findByIdInAndEnabledTrue(friendIds);

        return getFriendResponseVMS(userId, friends);

    }

    private List<FriendResponseVM> getFriendResponseVMS(Long currentId, List<Account> friends) {
        return friends.stream()
                .map(a -> {
                    Friendship existingRequest = friendshipRepo
                            .findActiveFriendship(currentId, a.getId())
                            .orElse(friendshipRepo.findActiveFriendship(a.getId(), currentId)
                                    .orElse(null));

                    Long friendshipId = existingRequest != null ? existingRequest.getId() : null;

                    return new FriendResponseVM(a.getId(), friendshipId, a.getUsername());
                }).toList();
    }

    @Override
    public List<FriendResponseVM> getPendingReceivedRequests() {

        Long currentUserId = authService.getCurrentUserId();
        Account currentUser = accountRepo.findById(currentUserId).get();
        List<Friendship> requests =
                friendshipRepo.findByReceiverAndStatus(currentUser, FriendshipStatus.PENDING);

        return requests.stream()
                .map(f -> new FriendResponseVM(
                        f.getRequester().getId(),
                        f.getId(),
                        f.getRequester().getUsername()
                ))
                .toList();
    }

    @Override
    public List<FriendResponseVM> getPendingSentRequests() {
        Long currentUserId = authService.getCurrentUserId();
        Account currentUser = accountRepo.findById(currentUserId).get();
        List<Friendship> requests =
                friendshipRepo.findByRequesterAndStatus(currentUser, FriendshipStatus.PENDING);

        return requests.stream()
                .map(f -> {
                    Account receiver = f.getReceiver();
                    return new FriendResponseVM(
                            receiver.getId(),
                            f.getId(),
                            receiver.getUsername()
                    );
                })
                .toList();
    }

    @Override
    public List<FriendResponseVM> getSuggestions() {

        Long currentId = authService.getCurrentUserId();

        List<Account> suggestions = friendshipRepo.findSuggestedUsers(currentId);


        return getFriendResponseVMS(currentId, suggestions);
    }

    private void validatePending(Friendship friendship) {
        if (!friendship.getStatus().equals(FriendshipStatus.PENDING)) {
            throw new RuntimeException("friendship.not.pending");
        }
    }

}
