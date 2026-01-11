package com.errasoft.friendfinder.service.impl;

import com.errasoft.friendfinder.controller.vm.ReactionUsersVm;
import com.errasoft.friendfinder.model.Post;
import com.errasoft.friendfinder.model.Reaction;
import com.errasoft.friendfinder.model.security.Account;
import com.errasoft.friendfinder.repo.PostRepo;
import com.errasoft.friendfinder.repo.ReactionRepo;
import com.errasoft.friendfinder.repo.security.AccountRepo;
import com.errasoft.friendfinder.service.NotificationService;
import com.errasoft.friendfinder.service.ReactionService;
import com.errasoft.friendfinder.service.security.AuthService;
import com.errasoft.friendfinder.utils.ReactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReactionServiceImpl implements ReactionService {

    private ReactionRepo reactionRepo;
    private PostRepo postRepo;
    private AuthService authService;
    private NotificationService notificationService;
    private AccountRepo accountRepo;

    @Autowired
    public ReactionServiceImpl(ReactionRepo reactionRepo,
                               PostRepo postRepo,
                               AuthService authService,
                               NotificationService notificationService,
                               AccountRepo accountRepo) {
        this.reactionRepo = reactionRepo;
        this.postRepo = postRepo;
        this.authService = authService;
        this.notificationService = notificationService;
        this.accountRepo = accountRepo;
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
            Account owner = accountRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("account.not.found"));
            reaction.setOwner(owner);
            reaction.setPost(post);
            reaction.setDeleted(false);
            Reaction savedReaction = reactionRepo.save(reaction);
            if(type == ReactionType.LIKE){
                post.setLikesCount(post.getLikesCount() + 1);
            }else{
                post.setDislikesCount(post.getDislikesCount() + 1);
            }
            postRepo.save(post);
            if (!savedReaction.getOwner().getId().equals(post.getOwner().getId())){
                notificationService.handleNotification(savedReaction.getOwner(),post.getOwner(),null,"REACT",postId);
            }
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
            Reaction savedReaction =  reactionRepo.save(existing);
            if (type == ReactionType.LIKE) {
                post.setLikesCount(post.getLikesCount() + 1);
            } else {
                post.setDislikesCount(post.getDislikesCount() + 1);
            }
            postRepo.save(post);
            if (!savedReaction.getOwner().getId().equals(post.getOwner().getId())){
                notificationService.handleNotification(savedReaction.getOwner(),post.getOwner(),null,"REACT",postId);
            }

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

    @Override
    public ReactionUsersVm getPostReactionsUsers(Long postId) {

        postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("post.not.found"));

        List<String> likes = reactionRepo
                .findByPostIdAndTypeAndDeletedFalseOrderByCreatedDateDesc(
                        postId, ReactionType.LIKE
                )
                .stream()
                .map(r -> r.getOwner().getUsername())
                .toList();

        List<String> dislikes = reactionRepo
                .findByPostIdAndTypeAndDeletedFalseOrderByCreatedDateDesc(
                        postId, ReactionType.DISLIKE
                )
                .stream()
                .map(r -> r.getOwner().getUsername())
                .toList();

        return new ReactionUsersVm(likes, dislikes);
    }
}
