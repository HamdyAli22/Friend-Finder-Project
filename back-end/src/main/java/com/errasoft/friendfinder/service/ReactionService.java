package com.errasoft.friendfinder.service;

import com.errasoft.friendfinder.utils.ReactionType;

public interface ReactionService {
    void react(Long postId, ReactionType type);
    public ReactionType getMyReaction(Long postId);
    public long countLikes(Long postId);
    public long countDislikes(Long postId);
}
