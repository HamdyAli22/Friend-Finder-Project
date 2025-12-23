package com.errasoft.friendfinder.service;

import com.errasoft.friendfinder.controller.vm.FriendResponseVM;
import com.errasoft.friendfinder.dto.FriendshipDto;
import com.errasoft.friendfinder.model.security.Account;

import java.util.List;

public interface FriendshipService {

    FriendshipDto sendRequest(Long receiverId);

    void acceptRequest(Long friendshipId);

    void rejectRequest(Long friendshipId);

    void cancelRequest(Long friendshipId);

    List<FriendResponseVM> getMyFriends();

    List<FriendResponseVM> getUserFriends(Long userId);

    List<FriendResponseVM> getPendingReceivedRequests();

    List<FriendResponseVM> getPendingSentRequests();

    List<FriendResponseVM> getSuggestions();

}
