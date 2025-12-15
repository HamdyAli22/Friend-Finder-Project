package com.errasoft.friendfinder.service.impl;

import com.errasoft.friendfinder.model.Post;
import com.errasoft.friendfinder.model.Reaction;
import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.repo.PostRepo;
import com.errasoft.friendfinder.repo.ReactionRepo;
import com.errasoft.friendfinder.service.ReactionService;
import com.errasoft.friendfinder.service.security.AuthService;
import com.errasoft.friendfinder.utils.ReactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReactionServiceImpl implements ReactionService {

    private ReactionRepo reactionRepo;
    private PostRepo postRepo;
    private AuthService authService;

    @Autowired
    public ReactionServiceImpl(ReactionRepo reactionRepo,
                               PostRepo postRepo,
                               AuthService authService) {
        this.reactionRepo = reactionRepo;
        this.postRepo = postRepo;
        this.authService = authService;
    }

    @Override
    public void react(Long postId, ReactionType type) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("post.not.found"));

        Long userId = authService.getCurrentUserId();

        Reaction existing = reactionRepo.findByPostIdAndOwnerIdAndDeletedFalse(postId, userId);

        if (existing == null) {
            Reaction reaction = new Reaction();
            reaction.setType(type);
            Account owner = new Account();
            owner.setId(userId);
            reaction.setOwner(owner);
            reaction.setPost(post);
            reaction.setDeleted(false);
            reactionRepo.save(reaction);
            if(type == ReactionType.LIKE){
                post.setLikesCount(post.getLikesCount() + 1);
            }else{
                post.setDislikesCount(post.getDislikesCount() + 1);
            }
            postRepo.save(post);
            return;
        }

        // If same type -> remove (toggle remove)
        if (existing.getType() == type){
            existing.setDeleted(true);
            reactionRepo.save(existing);
            if (existing.getType() == ReactionType.LIKE) {
                post.setLikesCount(Math.max(0L, post.getLikesCount() - 1));
            } else {
                post.setDislikesCount(Math.max(0L, post.getDislikesCount() - 1));
            }
            postRepo.save(post);
            return;
        }

        // if different type -> switch
        if (existing.getType() != type){
            // decrement old
            if (existing.getType() == ReactionType.LIKE) {
                post.setLikesCount(Math.max(0L, post.getLikesCount() - 1));
            } else {
                post.setDislikesCount(Math.max(0L, post.getDislikesCount() - 1));
            }
            // set new
            existing.setType(type);
            reactionRepo.save(existing);
            if (type == ReactionType.LIKE) {
                post.setLikesCount(post.getLikesCount() + 1);
            } else {
                post.setDislikesCount(post.getDislikesCount() + 1);
            }
            postRepo.save(post);
        }

    }

    @Override
    public ReactionType getMyReaction(Long postId) {
        Long userId = authService.getCurrentUserId();
        Reaction existing = reactionRepo.findByPostIdAndOwnerIdAndDeletedFalse(postId, userId);
        return existing != null ? existing.getType() : null;
    }

    @Override
    public long countLikes(Long postId) {
        return reactionRepo.countByPostIdAndTypeAndDeletedFalse(postId, ReactionType.LIKE);
    }

    @Override
    public long countDislikes(Long postId) {
        return reactionRepo.countByPostIdAndTypeAndDeletedFalse(postId, ReactionType.DISLIKE);
    }
}
