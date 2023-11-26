package com.playdata.pdfolio.gather.controller;


import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
import com.playdata.pdfolio.gather.domain.request.GatherEditRequest;
import com.playdata.pdfolio.gather.domain.request.GatherWriteRequest;
import com.playdata.pdfolio.gather.domain.response.GatherCommentResponse;
import com.playdata.pdfolio.gather.domain.response.GatherDetailResponse;
import com.playdata.pdfolio.gather.domain.response.GatherSearchResponse;
import com.playdata.pdfolio.gather.service.GatherService;
import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gather")
public class GatherController {

    private final GatherService gatherService;

    @GetMapping
    public GatherSearchResponse getGathersByCondition(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) GatherCategory category,
            @RequestParam(required = false) List<SkillType> skills,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "16") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return gatherService.getGathersByCondition(keyword, category, skills, pageRequest);
    }

    @GetMapping("/{gatherId}/comments")
    public List<GatherCommentResponse> getCommentsByGatherId(@PathVariable Long gatherId){
        return gatherService.getCommentsByGatherId(gatherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void writeGather(
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody GatherWriteRequest request
    ){
        gatherService.writeGather(tokenInfo.getId(), request);
    }

    @PutMapping("/{gatherId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editGather(
            @PathVariable Long gatherId,
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody GatherEditRequest request
    ){
        gatherService.editGather(gatherId, tokenInfo.getId(), request);
    }

    @DeleteMapping("/{gatherId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGather(
            @PathVariable Long gatherId,
            @AuthenticationPrincipal TokenInfo tokenInfo
    ){
        gatherService.deleteGather(gatherId, tokenInfo.getId());
    }

    @GetMapping("/{gatherId}")
    public GatherDetailResponse getGatherById(@PathVariable Long gatherId){
       return gatherService.getGatherById(gatherId);
    }
}
