package com.playdata.pdfolio.gather.service;

import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.gather.domain.entity.GatherReply;
import com.playdata.pdfolio.gather.domain.request.GatherCommentEditRequest;
import com.playdata.pdfolio.gather.domain.request.GatherCommentWriteRequest;
import com.playdata.pdfolio.gather.domain.request.GatherReplyEditRequest;
import com.playdata.pdfolio.gather.domain.request.GatherReplyWriteRequest;
import com.playdata.pdfolio.gather.domain.response.GatherCommentResponse;
import com.playdata.pdfolio.gather.repository.GatherCommentRepository;
import com.playdata.pdfolio.gather.repository.GatherReplyRepository;
import com.playdata.pdfolio.global.exception.ErrorCode;
import com.playdata.pdfolio.global.exception.ForbiddenException;
import com.playdata.pdfolio.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            throw new ForbiddenException(ErrorCode.INVALID_AUTHOR, GatherComment.class, commentId, memberId);
        }

        gatherComment.edit(request.content());
    }

    @Transactional
    public void deleteGatherComment(Long commentId, Long memberId){
        GatherComment gatherComment = findGatherCommentById(commentId);

        if(!isValidGatherCommentWriter(gatherComment, memberId)){
            throw new ForbiddenException(ErrorCode.INVALID_AUTHOR, GatherComment.class, commentId, memberId);
        }

        gatherComment.delete();
    }

    @Transactional
    public void writeGatherReply(Long commentId, Long memberId, GatherReplyWriteRequest request) {
        findGatherCommentById(commentId);

        GatherReply gatherReply = request.toEntity(commentId, memberId);
        gatherReplyRepository.save(gatherReply);
    }

    @Transactional
    public void editGatherReply(Long replyId, Long memberId, GatherReplyEditRequest request) {
        GatherReply reply = findGatherReplyById(replyId);

        if(!isValidGatherReplyWriter(reply, memberId)){
            throw new ForbiddenException(ErrorCode.INVALID_AUTHOR, GatherReply.class, replyId, memberId);
        }

        reply.edit(request.content());
    }

    private GatherReply findGatherReplyById(Long commentId) {
        GatherReply reply = gatherReplyRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CONTENT, GatherReply.class, commentId));

        if(reply.isDeleted()){
            throw new NotFoundException(ErrorCode.DELETED_CONTENT, GatherReply.class, commentId);
        }

        return reply;
    }

    @Transactional
    public void deleteGatherReply(Long replyId, Long memberId) {
        GatherReply reply = findGatherReplyById(replyId);

        if(!isValidGatherReplyWriter(reply, memberId)){
            throw new ForbiddenException(ErrorCode.INVALID_AUTHOR, GatherReply.class, replyId, memberId);
        }

        reply.delete();
    }

    private GatherComment findGatherCommentById(Long commentId) {
        GatherComment gatherComment = gatherCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CONTENT, GatherComment.class, commentId));

        if(gatherComment.isDeleted()){
            throw new NotFoundException(ErrorCode.DELETED_CONTENT, GatherComment.class, commentId);
        }

        return gatherComment;
    }

    private boolean isValidGatherCommentWriter(GatherComment gatherComment, Long memberId){
        return gatherComment.getMember().getId().equals(memberId);
    }

    private boolean isValidGatherReplyWriter(GatherReply gatherReply, Long memberId){
        return gatherReply.getMember().getId().equals(memberId);
    }
}
