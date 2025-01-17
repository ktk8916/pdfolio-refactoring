package com.playdata.pdfolio.gather.controller;

import com.playdata.pdfolio.gather.domain.request.GatherCommentEditRequest;
import com.playdata.pdfolio.gather.domain.request.GatherCommentWriteRequest;
import com.playdata.pdfolio.gather.domain.request.GatherReplyEditRequest;
import com.playdata.pdfolio.gather.domain.request.GatherReplyWriteRequest;
import com.playdata.pdfolio.gather.service.GatherCommentService;
import com.playdata.pdfolio.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gather")
public class GatherCommentController {

    private final GatherCommentService gatherCommentService;

    @PostMapping("/{gatherId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void writeGatherComment(
            @PathVariable Long gatherId,
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody GatherCommentWriteRequest request)
    {
        gatherCommentService.writeGatherComment(gatherId, tokenInfo.getId(), request);
    }

    @PutMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void editGatherComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody GatherCommentEditRequest request)
    {
        gatherCommentService.editGatherComment(commentId, tokenInfo.getId(), request);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void GatherCommentDelete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal TokenInfo tokenInfo)
    {
        gatherCommentService.deleteGatherComment(commentId, tokenInfo.getId());
    }

    @PostMapping("/comments/{commentId}/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public void writeGatherReply(
            @PathVariable Long commentId,
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody GatherReplyWriteRequest request)
    {
        gatherCommentService.writeGatherReply(commentId, tokenInfo.getId(), request);
    }

    @PutMapping("/replies/{replyId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void editGatherReply(
            @PathVariable Long replyId,
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody GatherReplyEditRequest request)
    {
        gatherCommentService.editGatherReply(replyId, tokenInfo.getId(), request);
    }

    @DeleteMapping("/replies/{replyId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void deleteGatherReply(
            @PathVariable Long replyId,
            @AuthenticationPrincipal TokenInfo tokenInfo)
    {
        gatherCommentService.deleteGatherReply(replyId, tokenInfo.getId());
    }
}
