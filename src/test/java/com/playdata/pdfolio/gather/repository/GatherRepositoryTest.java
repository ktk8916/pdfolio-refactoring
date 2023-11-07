package com.playdata.pdfolio.gather.repository;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class GatherRepositoryTest {

    @Autowired
    private GatherRepository gatherRepository;

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

    @DisplayName("삭제 처리된 모집글은 조회되지 않는다.")
    @Test
    void findDeleteGatherByIdMemberFetch(){
        // given
        Gather gather = Gather.builder().build();
        Gather savedGather = gatherRepository.save(gather);
        gather.delete();

        // when
        Optional<Gather> findGather = gatherRepository.findByIdMemberFetch(savedGather.getId());

        // then
        assertThat(findGather).isEmpty();
    }
}