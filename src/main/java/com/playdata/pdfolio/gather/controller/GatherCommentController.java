package com.playdata.pdfolio.gather.controller;

import com.playdata.pdfolio.gather.domain.request.GatherCommentWriteRequest;
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
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @PathVariable("gatherId") Long gatherId,
            @RequestBody GatherCommentWriteRequest request
    ){
        gatherCommentService.writeGatherComment(tokenInfo.getId(), gatherId, request);
    }
}
