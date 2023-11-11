package com.playdata.pdfolio.gather.service;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.gather.domain.entity.GatherReply;
import com.playdata.pdfolio.gather.domain.request.GatherCommentEditRequest;
import com.playdata.pdfolio.gather.domain.request.GatherCommentWriteRequest;
import com.playdata.pdfolio.gather.domain.request.GatherReplyWriteRequest;
import com.playdata.pdfolio.gather.exception.DeletedGatherCommentException;
import com.playdata.pdfolio.gather.exception.GatherCommentNotFoundException;
import com.playdata.pdfolio.gather.exception.InvalidGatherCommentWriterException;
import com.playdata.pdfolio.gather.repository.GatherCommentRepository;
import com.playdata.pdfolio.gather.repository.GatherReplyRepository;
import com.playdata.pdfolio.gather.repository.GatherRepository;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GatherCommentServiceTest {

    @Autowired
    private GatherCommentService gatherCommentService;
    @Autowired
    private GatherCommentRepository gatherCommentRepository;
    @Autowired
    private GatherReplyRepository gatherReplyRepository;

    @DisplayName("모집글에 댓글을 작성한다.")
    @Test
    void writeGatherComment(){
        // given
        Gather gather = createTestGather();

        Member member = createTestMember();
        GatherCommentWriteRequest request = new GatherCommentWriteRequest("댓글입니다.");

        // when
        gatherCommentService.writeGatherComment(gather.getId(), member.getId() , request);
        GatherComment comment = gatherCommentRepository.findAll().get(0);

        // then
        assertThat(comment.getContent()).isEqualTo("댓글입니다.");
    }

    @DisplayName("모집글 댓글을 수정한다.")
    @Test
    void editGatherComment(){
        // given
        Gather gather = createTestGather();

        Member member = createTestMember();
        GatherComment gatherComment = GatherComment.builder()
                .gather(gather)
                .member(member)
                .content("댓글입니다.")
                .build();

        GatherComment savedGatherComment = gatherCommentRepository.save(gatherComment);
        GatherCommentEditRequest request = new GatherCommentEditRequest("수정된 댓글입니다.");

        // when
        gatherCommentService.editGatherComment(savedGatherComment.getId(), member.getId(), request);

        // then
        assertThat(savedGatherComment.getContent()).isEqualTo("수정된 댓글입니다.");
    }

    @DisplayName("삭제된 모집글 댓글 수정 시 예외가 발생한다.")
    @Test
    void editDeletedGatherComment(){
        // given
        Gather gather = createTestGather();

        Member member = createTestMember();
        GatherComment gatherComment = GatherComment.builder()
                .gather(gather)
                .member(member)
                .content("댓글입니다.")
                .build();

        GatherComment savedGatherComment = gatherCommentRepository.save(gatherComment);
        savedGatherComment.delete();
        GatherCommentEditRequest request = new GatherCommentEditRequest("수정된 댓글입니다.");

        // when, then
        Assertions.assertThatThrownBy(()->gatherCommentService.editGatherComment(savedGatherComment.getId(), member.getId(), request))
                .isInstanceOf(DeletedGatherCommentException.class);
    }

    @DisplayName("모집글 댓글 수정 시, 작성자가 아니면 예외가 발생한다.")
    @Test
    void editGatherCommentInvalidWriter(){
        // given
        Gather gather = createTestGather();

        Member member = createTestMember();
        GatherComment gatherComment = GatherComment.builder()
                .gather(gather)
                .member(member)
                .content("댓글입니다.")
                .build();

        GatherComment savedGatherComment = gatherCommentRepository.save(gatherComment);
        GatherCommentEditRequest request = new GatherCommentEditRequest("수정된 댓글입니다.");


        Member anotherMember = createTestMember();

        // when, then
        Assertions.assertThatThrownBy(()->gatherCommentService.editGatherComment(savedGatherComment.getId(), anotherMember.getId(), request))
                .isInstanceOf(InvalidGatherCommentWriterException.class);
    }

    @DisplayName("모집글 댓글을 삭제한다.")
    @Test
    void deleteGatherComment(){
        // given
        Gather gather = createTestGather();

        Member member = createTestMember();
        GatherComment gatherComment = GatherComment.builder()
                .gather(gather)
                .member(member)
                .content("댓글입니다.")
                .build();

        GatherComment savedGatherComment = gatherCommentRepository.save(gatherComment);

        // when
        gatherCommentService.deleteGatherComment(gatherComment.getId(), member.getId());

        // then
        assertThat(gatherComment.isDeleted()).isTrue();
    }

    @DisplayName("모집글 댓글 삭제 시, 작성자가 아니면 예외가 발생한다.")
    @Test
    void deleteGatherCommentInvalidWriter(){
        // given
        Gather gather = createTestGather();

        Member member = createTestMember();
        GatherComment gatherComment = GatherComment.builder()
                .gather(gather)
                .member(member)
                .content("댓글입니다.")
                .build();

        GatherComment savedGatherComment = gatherCommentRepository.save(gatherComment);

        Member anotherMember = createTestMember();

        // when, then
        Assertions.assertThatThrownBy(()-> gatherCommentService.deleteGatherComment(gatherComment.getId(), anotherMember.getId()))
                .isInstanceOf(InvalidGatherCommentWriterException.class);
    }

    @DisplayName("모집글 답글을 작성한다.")
    @Test
    void writeGatherReply(){
        // given
        Gather gather = createTestGather();
        GatherComment comment = createTestGatherComment();

        Member member = createTestMember();
        GatherReplyWriteRequest request = new GatherReplyWriteRequest("답글입니다.");

        // when
        gatherCommentService.writeGatherReply(comment.getId(), member.getId(), request);
        GatherReply reply = gatherReplyRepository.findAll().get(0);

        // then
        assertThat(reply.getContent()).isEqualTo("답글입니다.");
    }

    @DisplayName("존재하지 않는 모집글 댓글에 답글을 작성 시, 예외가 발생한다.")
    @Test
    void writeGatherReplyDeletedComment(){
        // given
        Long notExistCommentId = 999999L;

        Member member = createTestMember();
        GatherReplyWriteRequest request = new GatherReplyWriteRequest("답글입니다.");

        // when, then
        assertThatThrownBy(()-> gatherCommentService.writeGatherReply(notExistCommentId, member.getId(), request))
                .isInstanceOf(GatherCommentNotFoundException.class);
    }

    @DisplayName("삭제된 모집글 댓글에 답글을 작성 시, 예외가 발생한다.")
    @Test
    void writeGatherReplyNotExistComment(){
        // given
        Gather gather = createTestGather();
        GatherComment comment = createTestGatherComment();
        comment.delete();

        Member member = createTestMember();
        GatherReplyWriteRequest request = new GatherReplyWriteRequest("답글입니다.");

        // when, then
        assertThatThrownBy(()-> gatherCommentService.writeGatherReply(comment.getId(), member.getId(), request))
                .isInstanceOf(DeletedGatherCommentException.class);
    }


    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GatherRepository gatherRepository;
    private Member createTestMember() {
        Member member = Member.builder()
                .build();
        return memberRepository.save(member);
    }

    private Gather createTestGather() {
        Member member = createTestMember();

        Gather gather = Gather.builder()
                .member(member)
                .build();
        return gatherRepository.save(gather);
    }

    private GatherComment createTestGatherComment(){
        Member member = createTestMember();
        Gather gather = createTestGather();

        GatherComment gatherComment = GatherComment.builder()
                .member(member)
                .gather(gather)
                .build();

        return gatherCommentRepository.save(gatherComment);
    }
}