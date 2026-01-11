package com.errasoft.friendfinder.service.impl;

import com.errasoft.friendfinder.dto.ActivityDto;
import com.errasoft.friendfinder.model.Comment;
import com.errasoft.friendfinder.model.Post;
import com.errasoft.friendfinder.model.Reaction;
import com.errasoft.friendfinder.repo.CommentRepo;
import com.errasoft.friendfinder.repo.PostRepo;
import com.errasoft.friendfinder.repo.ReactionRepo;
import com.errasoft.friendfinder.service.ActivityService;
import com.errasoft.friendfinder.service.security.AuthService;
import com.errasoft.friendfinder.utils.ReactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private PostRepo postRepo;
    private CommentRepo commentRepo;
    private ReactionRepo reactionRepo;
    private AuthService authService;

    @Autowired
    public ActivityServiceImpl(PostRepo postRepo,
                               CommentRepo commentRepo,
                               ReactionRepo reactionRepo,
                               AuthService authService) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.reactionRepo = reactionRepo;
        this.authService = authService;
    }

    @Override
    public List<ActivityDto> getUserActivities(Long userId) {

        if (userId == null) {
            userId = authService.getCurrentUserId();
        }

        List<ActivityDto> activities = new ArrayList<>();

        //Posts
        List<Post> posts = postRepo.findTop4ByOwnerIdAndDeletedFalseOrderByCreatedDateDesc(userId);
        for (Post post : posts) {
            String mediaType = post.getMediaType() == null ? "TEXT" : post.getMediaType().name();
            String msg = switch (mediaType) {
                case "IMAGE" -> "Has posted a photo";
                case "VIDEO" -> "Has posted a video";
                default -> "Has posted a post";
            };

            activities.add(new ActivityDto(
                    "POST",
                    msg,
                    post.getCreatedDate(),
                    post.getId(),
                    mediaType
            ));
        }

        //Comments
        List<Comment> comments = commentRepo.findTop4ByOwnerIdAndDeletedFalseOrderByCreatedDateDesc(userId);
        for (Comment comment : comments) {
            activities.add(new ActivityDto(
                    "COMMENT",
                    "Commented on a post",
                    comment.getCreatedDate(),
                    comment.getPost().getId(),
                    null
            ));
        }

        List<Reaction> reactions = reactionRepo.findTop4ByOwnerIdAndDeletedFalseOrderByCreatedDateDesc(userId);
        for (Reaction reaction : reactions) {
            String msg = reaction.getType() == ReactionType.LIKE
                    ? "Liked a post"
                    : "Disliked a post";

            activities.add(new ActivityDto(
                    "REACTION",
                    msg,
                    reaction.getCreatedDate(),
                    reaction.getPost().getId(),
                    null
            ));
        }

        //sort & limit
        return activities.stream()
                .sorted(Comparator.comparing(ActivityDto::getDate).reversed())
                .limit(4)
                .toList();

    }
}
