package com.playdata.pdfolio.gather.service;

import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.gather.domain.request.GatherCommentEditRequest;
import com.playdata.pdfolio.gather.domain.request.GatherCommentWriteRequest;
import com.playdata.pdfolio.gather.exception.DeletedGatherCommentException;
import com.playdata.pdfolio.gather.exception.GatherCommentNotFoundException;
import com.playdata.pdfolio.gather.exception.InvalidGatherCommentWriterException;
import com.playdata.pdfolio.gather.repository.GatherCommentRepository;
import com.playdata.pdfolio.gather.repository.GatherReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GatherCommentService {

    private final GatherCommentRepository gatherCommentRepository;
    private final GatherReplyRepository gatherReplyRepository;

    public void writeGatherComment(Long gatherId, Long memberId, GatherCommentWriteRequest request){
        gatherCommentRepository.save(request.toEntity(gatherId, memberId));
    }

    @Transactional
    public void editGatherComment(Long commentId, Long memberId, GatherCommentEditRequest request){
        GatherComment gatherComment = findGatherCommentById(commentId);

        if(!isValidGatherCommentWriter(gatherComment, memberId)){
            throw new InvalidGatherCommentWriterException();
        }

        gatherComment.edit(request.content());
    }

    @Transactional
    public void deleteGatherComment(Long commentId, Long memberId){
        GatherComment gatherComment = findGatherCommentById(commentId);

        if(!isValidGatherCommentWriter(gatherComment, memberId)){
            throw new InvalidGatherCommentWriterException();
        }

        gatherComment.delete();
    }

    private GatherComment findGatherCommentById(Long commentId) {
        GatherComment gatherComment = gatherCommentRepository.findById(commentId)
                .orElseThrow(GatherCommentNotFoundException::new);

        if(gatherComment.isDeleted()){
            throw new DeletedGatherCommentException();
        }

        return gatherComment;
    }

    private boolean isValidGatherCommentWriter(GatherComment gatherComment, Long memberId){
        return gatherComment.getMember().getId().equals(memberId);
    }
}
