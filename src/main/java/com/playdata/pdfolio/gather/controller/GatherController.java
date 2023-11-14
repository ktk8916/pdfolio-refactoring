package com.playdata.pdfolio.gather.controller;


import com.playdata.pdfolio.gather.domain.request.GatherEditRequest;
import com.playdata.pdfolio.jwt.TokenInfo;
import com.playdata.pdfolio.gather.domain.dto.SearchDto;
import com.playdata.pdfolio.gather.domain.request.GatherReplyWriteRequest;
import com.playdata.pdfolio.gather.domain.request.GatherWriteRequest;
import com.playdata.pdfolio.gather.domain.response.GatherDetailResponse;
import com.playdata.pdfolio.gather.domain.response.GatherResponse;
import com.playdata.pdfolio.gather.service.GatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gather")
public class GatherController {

    private final GatherService gatherService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void writeGather(
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody GatherWriteRequest request
    ){
        gatherService.writeGather(tokenInfo.getId(), request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editGather(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal TokenInfo tokenInfo,
            @RequestBody GatherEditRequest request)
    {
        gatherService.editGather(id, tokenInfo.getId(), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGather(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal TokenInfo tokenInfo
    ){
        gatherService.deleteGather(id, tokenInfo.getId());
    }

    @GetMapping("/{id}")
    public GatherDetailResponse getGatherById(@PathVariable("id") Long id){
       return gatherService.getGatherById(id);
    }

    // 모집글 전체 보기 / 모집글 제목 , 글 내용 , 카테고리 , 스킬 검색
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<GatherResponse> allGather(
            @RequestParam(required = false,defaultValue = "0",name = "page")
            Integer page,
            @RequestParam(required = false,defaultValue = "8",name = "size")
            Integer size,
//            @RequestParam(required = false,defaultValue = "",name = "keyword")
//            String keyword,
            SearchDto searchDto
            ) {
        PageRequest request = PageRequest.of(page, size);
//        return gatherService.allGather(request, keyword, category);
        return gatherService.allGather(request, searchDto);
    }

}
