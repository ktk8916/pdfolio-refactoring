package com.playdata.pdfolio.gather.service;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.gather.domain.request.GatherCommentEditRequest;
import com.playdata.pdfolio.gather.domain.request.GatherCommentWriteRequest;
import com.playdata.pdfolio.gather.exception.InvalidGatherCommentWriterException;
import com.playdata.pdfolio.gather.repository.GatherCommentRepository;
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

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GatherCommentServiceTest {

    @Autowired
    private GatherCommentService gatherCommentService;
    @Autowired
    private GatherCommentRepository gatherCommentRepository;

    @DisplayName("모집글에 댓글을 작성한다.")
    @Test
    void writeGatherComment(){
        // given
        Member member = createTestMember();
        Gather gather = createTestGather();

        GatherCommentWriteRequest request = new GatherCommentWriteRequest("댓글입니다.");

        // when
        gatherCommentService.writeGatherComment(gather.getId(), member.getId() , request);
        GatherComment comment = gatherCommentRepository.findAll().get(0);

        // then
        Assertions.assertThat(comment.getContent()).isEqualTo("댓글입니다.");
    }

    @DisplayName("모집글 댓글을 수정한다.")
    @Test
    void editGatherComment(){
        // given
        Member member = createTestMember();
        Gather gather = createTestGather();

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
        Assertions.assertThat(savedGatherComment.getContent()).isEqualTo("수정된 댓글입니다.");
    }

    @DisplayName("모집글 댓글 수정 시, 작성자가 아니면 예외가 발생한다.")
    @Test
    void editGatherCommentInvalidWriter(){
        // given
        Member member = createTestMember();
        Gather gather = createTestGather();

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
        Member member = createTestMember();
        Gather gather = createTestGather();

        GatherComment gatherComment = GatherComment.builder()
                .gather(gather)
                .member(member)
                .content("댓글입니다.")
                .build();

        GatherComment savedGatherComment = gatherCommentRepository.save(gatherComment);

        // when
        gatherCommentService.deleteGatherComment(gatherComment.getId(), member.getId());

        // then
        Assertions.assertThat(gatherComment.isDeleted()).isTrue();
    }

    @DisplayName("모집글 댓글 삭제 시, 작성자가 아니면 예외가 발생한다.")
    @Test
    void deleteGatherCommentInvalidWriter(){
        // given
        Member member = createTestMember();
        Gather gather = createTestGather();

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
}