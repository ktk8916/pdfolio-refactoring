package com.playdata.pdfolio.gather.repository;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.gather.domain.entity.GatherReply;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class GatherRepositoryTest {

    @Autowired
    private GatherRepository gatherRepository;
    @Autowired
    private GatherCommentRepository gatherCommentRepository;
    @Autowired
    private GatherReplyRepository gatherReplyRepository;
    @Autowired
    private EntityManager entityManager;

    @DisplayName("id로 모집글을 조회한다.")
    @Test
    void findByIdMemberFetch(){
        // given
        Gather gather = Gather.builder().build();
        Gather savedGather = gatherRepository.save(gather);

        // when
        Optional<Gather> findGather = gatherRepository.findByIdMemberFetch(savedGather.getId());

        // then
        assertThat(findGather).isPresent();
    }

    @DisplayName("id로 모집글의 댓글을 조회한다.")
    @Test
    void findCommentsByGatherId(){
        // given
        Gather gather = Gather.builder().build();
        Gather savedGather = gatherRepository.save(gather);

        for (int i = 0; i <10; i++) {
            GatherComment comment = GatherComment.builder().gather(savedGather).build();
            gatherCommentRepository.save(comment);
        }

        // when
        List<GatherComment> comments = gatherRepository.findCommentsByGatherId(savedGather.getId());

        // then
        assertThat(comments).hasSize(10);
    }

    @DisplayName("삭제된 댓글은 조회되지 않는다.")
    @Test
    void findCommentsByGatherIdNotContainsDeletedComment(){
        // given
        Gather gather = Gather.builder().build();
        Gather savedGather = gatherRepository.save(gather);

        for (int i = 0; i <10; i++) {
            GatherComment comment = GatherComment.builder().gather(savedGather).build();
            if(i % 2 == 0){
                comment.delete();
            }
            gatherCommentRepository.save(comment);
        }

        // when
        List<GatherComment> comments = gatherRepository.findCommentsByGatherId(savedGather.getId());

        // then
        assertThat(comments).hasSize(5);
    }

    @DisplayName("조회된 모집글 댓글은 답글을 포함한다.")
    @Test
    void findCommentsByGatherIdContainsReplies(){
        // given
        Gather gather = Gather.builder().build();
        Gather savedGather = gatherRepository.save(gather);
        GatherComment comment = GatherComment.builder().gather(savedGather).build();
        GatherComment savedComment = gatherCommentRepository.save(comment);

        for (int i = 0; i <10; i++) {
            GatherReply reply = GatherReply.builder().comment(savedComment).build();
            gatherReplyRepository.save(reply);
        }

        contextClear();

        // when
        List<GatherComment> comments = gatherRepository.findCommentsByGatherId(savedGather.getId());
        List<GatherReply> replies = comments.get(0).getReplies();

        // then
        assertThat(replies).hasSize(10);
    }

    @DisplayName("삭제된 모집글 답글은 조회되지 않는다.")
    @Test
    void findCommentsByGatherIdNotContainsDeletedReply(){
        // given
        Gather gather = Gather.builder().build();
        Gather savedGather = gatherRepository.save(gather);
        GatherComment comment = GatherComment.builder().gather(savedGather).build();
        GatherComment savedComment = gatherCommentRepository.save(comment);

        for (int i = 0; i <10; i++) {
            GatherReply reply = GatherReply.builder().comment(savedComment).build();
            if(i % 2 == 0){
                reply.delete();
            }
            gatherReplyRepository.save(reply);
        }

        contextClear();

        // when
        List<GatherComment> comments = gatherRepository.findCommentsByGatherId(savedGather.getId());
        List<GatherReply> replies = comments.get(0).getReplies();

        // then
        assertThat(replies).hasSize(5);
    }

    private void contextClear() {
        entityManager.flush();
        entityManager.clear();
    }
}