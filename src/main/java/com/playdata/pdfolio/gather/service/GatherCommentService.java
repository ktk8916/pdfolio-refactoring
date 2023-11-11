package com.playdata.pdfolio.gather.service;

import com.playdata.pdfolio.gather.domain.request.GatherCommentWriteRequest;
import com.playdata.pdfolio.gather.repository.GatherCommentRepository;
import com.playdata.pdfolio.gather.repository.GatherReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GatherCommentService {

    private final GatherCommentRepository gatherCommentRepository;
    private final GatherReplyRepository gatherReplyRepository;

    public void writeGatherComment(Long memberId, Long gatherId, GatherCommentWriteRequest request){
        gatherCommentRepository.save(request.toEntity(memberId, gatherId));
    }
}
