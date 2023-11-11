package com.playdata.pdfolio.gather.service;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
import com.playdata.pdfolio.gather.domain.request.GatherEditRequest;
import com.playdata.pdfolio.gather.domain.request.GatherWriteRequest;
import com.playdata.pdfolio.gather.domain.response.GatherDetailResponse;
import com.playdata.pdfolio.gather.exception.DeletedGatherException;
import com.playdata.pdfolio.gather.exception.GatherNotFoundException;
import com.playdata.pdfolio.gather.exception.InvalidGatherDurationException;
import com.playdata.pdfolio.gather.exception.InvalidGatherWriterException;
import com.playdata.pdfolio.gather.repository.GatherRepository;
import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GatherServiceTest {

    @Autowired
    private GatherService gatherService;
    @Autowired
    private GatherRepository gatherRepository;
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("모집글을 조회한다.")
    @Test
    void getGatherById(){
        // given
        Member member = createTestMember( "철수", "www.cdn.com");

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate closeDate = LocalDate.of(2023, 11, 5);

        Gather gather = createTestGather(member,
                "제목입니다.",
                "내용입니다.",
                startDate,
                closeDate,
                5,
                GatherCategory.PROJECT,
                "aaa@aaa.com",
                List.of("java", "spring"));

        Gather savedGather = gatherRepository.save(gather);

        // when
        GatherDetailResponse response = gatherService.getGatherById(savedGather.getId());

        // then
        assertThat(response.id()).isEqualTo(savedGather.getId());
        assertThat(response.title()).isEqualTo("제목입니다.");
        assertThat(response.content()).isEqualTo("내용입니다.");
        assertThat(response.startDate()).isEqualTo(startDate);
        assertThat(response.closeDate()).isEqualTo(closeDate);
        assertThat(response.teamSize()).isEqualTo(5);
        assertThat(response.category()).isEqualTo(GatherCategory.PROJECT);
        assertThat(response.contact()).isEqualTo("aaa@aaa.com");
        assertThat(response.likeCount()).isEqualTo(0);
        assertThat(response.viewCount()).isEqualTo(1);
        assertThat(response.member())
                .extracting("id", "nickname", "imageUrl")
                .containsExactlyInAnyOrder(member.getId(), "철수", "www.cdn.com");
        assertThat(response.skills()).hasSize(2)
                .containsExactlyInAnyOrder(SkillType.JAVA, SkillType.SPRING);
    }

    @DisplayName("모집글 조회 시, 조회 수가 증가한다.")
    @Test
    void increaseViewCount(){
        // given
        Member member = createTestMember( "철수", "www.cdn.com");

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate closeDate = LocalDate.of(2023, 11, 5);

        Gather gather = createTestGather(member,
                "제목입니다.",
                "내용입니다.",
                startDate,
                closeDate,
                5,
                GatherCategory.PROJECT,
                "aaa@aaa.com",
                List.of("java", "spring"));

        Gather savedGather = gatherRepository.save(gather);

        // when
        GatherDetailResponse response = gatherService.getGatherById(savedGather.getId());

        // then
        assertThat(response.viewCount()).isEqualTo(1);
    }

    @DisplayName("존재하지 않는 모집글 조회 시 예외가 발생한다.")
    @Test
    void getGatherByNotExistId(){
        // given
        Member member = createTestMember( "철수", "www.cdn.com");

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate closeDate = LocalDate.of(2023, 11, 5);

        Gather gather = createTestGather(member,
                "제목입니다.",
                "내용입니다.",
                startDate,
                closeDate,
                5,
                GatherCategory.PROJECT,
                "aaa@aaa.com",
                List.of("java", "spring"));

        Gather savedGather = gatherRepository.save(gather);

        Long notExistGatherId = 9999999L;

        // when, then
        assertThatThrownBy(()->gatherService.getGatherById(notExistGatherId))
                .isInstanceOf(GatherNotFoundException.class);
    }

    @DisplayName("삭제된 모집글 조회 시, 예외가 발생한다.")
    @Test
    void getDeletedGather(){
        // given
        Member member = createTestMember( "철수", "www.cdn.com");

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate closeDate = LocalDate.of(2023, 11, 5);

        Gather gather = createTestGather(member,
                "제목입니다.",
                "내용입니다.",
                startDate,
                closeDate,
                5,
                GatherCategory.PROJECT,
                "aaa@aaa.com",
                List.of("java", "spring"));

        Gather savedGather = gatherRepository.save(gather);
        savedGather.delete();

        // when, then
        Assertions.assertThatThrownBy(()->gatherService.getGatherById(savedGather.getId()))
                .isInstanceOf(DeletedGatherException.class);
    }

    @DisplayName("모집글을 작성한다.")
    @Test
    void writeGather(){
        // given
        Member member = createTestMember("철수", "www.cdn.com");
        Long memberId = member.getId();

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate closeDate = LocalDate.of(2023, 11, 5);

        GatherWriteRequest gatherWriteRequest = new GatherWriteRequest(
                "제목입니다.",
                "내용입니다.",
                startDate,
                closeDate,
                5,
                GatherCategory.PROJECT,
                "aaa@aaa.com",
                List.of("java", "spring")
        );

        // when
        gatherService.writeGather(memberId, gatherWriteRequest);

        // then
        Gather gather = gatherRepository.findAll().get(0);

        assertThat(gather.getTitle()).isEqualTo("제목입니다.");
        assertThat(gather.getContent()).isEqualTo("내용입니다.");
        assertThat(gather.getStartDate()).isEqualTo(startDate);
        assertThat(gather.getCloseDate()).isEqualTo(closeDate);
        assertThat(gather.getTeamSize()).isEqualTo(5);
        assertThat(gather.getCategory()).isEqualTo(GatherCategory.PROJECT);
        assertThat(gather.getContact()).isEqualTo("aaa@aaa.com");
        assertThat(gather.getSkills()).extracting("skillType").hasSize(2)
                .containsExactlyInAnyOrder(SkillType.JAVA, SkillType.SPRING);
    }

    @DisplayName("모집글 작성 시, 종료일이 시작일보다 빠르면 예외가 발생한다.")
    @Test
    void writeGatherInvalidDuration(){
        // given
        Member member = createTestMember("철수", "www.cdn.com");
        Long memberId = member.getId();

        LocalDate startDate = LocalDate.of(2023, 11, 5);
        LocalDate closeDate = LocalDate.of(2023, 10, 5);

        GatherWriteRequest gatherWriteRequest = new GatherWriteRequest(
                "제목입니다.",
                "내용입니다.",
                startDate,
                closeDate,
                5,
                GatherCategory.PROJECT,
                "aaa@aaa.com",
                List.of("java", "spring")
        );

        // when, then
        assertThatThrownBy(()->gatherService.writeGather(memberId, gatherWriteRequest))
                .isInstanceOf(InvalidGatherDurationException.class);
    }

    @DisplayName("모집글을 수정한다.")
    @Test
    void editGather(){
        // given
        Member member = createTestMember( "철수", "www.cdn.com");

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate closeDate = LocalDate.of(2023, 11, 5);

        Gather gather = createTestGather(member,
                "제목입니다.",
                "내용입니다.",
                startDate,
                closeDate,
                5,
                GatherCategory.PROJECT,
                "aaa@aaa.com",
                List.of("java", "spring"));

        Gather savedGather = gatherRepository.save(gather);

        LocalDate editStartDate = LocalDate.of(2024, 10, 5);
        LocalDate editCloseDate = LocalDate.of(2024, 11, 5);

        GatherEditRequest gatherEditRequest = new GatherEditRequest(
                "수정 제목입니다.",
                "수정 내용입니다.",
                editStartDate,
                editCloseDate,
                7,
                GatherCategory.STUDY,
                "bbb@bbb.com",
                List.of("php", "mysql", "git")
        );

        Gather editedgather = gatherRepository.findAll().get(0);

        // when
        gatherService.editGather(editedgather.getId(), member.getId(), gatherEditRequest);

        // then
        assertThat(editedgather.getTitle()).isEqualTo("수정 제목입니다.");
        assertThat(editedgather.getContent()).isEqualTo("수정 내용입니다.");
        assertThat(editedgather.getStartDate()).isEqualTo(editStartDate);
        assertThat(editedgather.getCloseDate()).isEqualTo(editCloseDate);
        assertThat(editedgather.getTeamSize()).isEqualTo(7);
        assertThat(editedgather.getCategory()).isEqualTo(GatherCategory.STUDY);
        assertThat(editedgather.getContact()).isEqualTo("bbb@bbb.com");
        assertThat(editedgather.getSkills()).extracting("skillType").hasSize(3)
                .containsExactlyInAnyOrder(SkillType.PHP, SkillType.MYSQL, SkillType.GIT);
    }

    @DisplayName("모집글 수정 시, 글 작성자가 아니면 예외가 발생한다.")
    @Test
    void editGatherInvalidMember(){
        // given
        Member member = createTestMember( "철수", "www.cdn.com");

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate closeDate = LocalDate.of(2023, 11, 5);

        Gather gather = createTestGather(member,
                "제목입니다.",
                "내용입니다.",
                startDate,
                closeDate,
                5,
                GatherCategory.PROJECT,
                "aaa@aaa.com",
                List.of("java", "spring"));

        Gather savedGather = gatherRepository.save(gather);

        LocalDate editStartDate = LocalDate.of(2024, 10, 5);
        LocalDate editCloseDate = LocalDate.of(2024, 11, 5);

        GatherEditRequest gatherEditRequest = new GatherEditRequest(
                "수정 제목입니다.",
                "수정 내용입니다.",
                editStartDate,
                editCloseDate,
                7,
                GatherCategory.STUDY,
                "bbb@bbb.com",
                List.of("php", "mysql", "git")
        );

        Long invalidMemberId = 99999999L;

        // when, then
        assertThatThrownBy(()->gatherService.editGather(gather.getId(), invalidMemberId, gatherEditRequest))
                .isInstanceOf(InvalidGatherWriterException.class);
    }

    @DisplayName("모집글 수정 시, 종료일이 시작일보다 빠르면 예외가 발생한다.")
    @Test
    void editGatherInvalidDuration(){
        // given
        Member member = createTestMember( "철수", "www.cdn.com");

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate closeDate = LocalDate.of(2023, 11, 5);

        Gather gather = createTestGather(member,
                "제목입니다.",
                "내용입니다.",
                startDate,
                closeDate,
                5,
                GatherCategory.PROJECT,
                "aaa@aaa.com",
                List.of("java", "spring"));

        Gather savedGather = gatherRepository.save(gather);

        LocalDate editStartDate = LocalDate.of(2024, 10, 5);
        LocalDate editCloseDate = LocalDate.of(2023, 11, 5);

        GatherEditRequest gatherEditRequest = new GatherEditRequest(
                "수정 제목입니다.",
                "수정 내용입니다.",
                editStartDate,
                editCloseDate,
                7,
                GatherCategory.STUDY,
                "bbb@bbb.com",
                List.of("php", "mysql", "git")
        );

        // when, then
        assertThatThrownBy(()->gatherService.editGather(gather.getId(), member.getId(), gatherEditRequest))
                .isInstanceOf(InvalidGatherDurationException.class);
    }

    @DisplayName("모집글을 삭제한다.")
    @Test
    void deleteGather(){
        // given
        Member member = createTestMember( "철수", "www.cdn.com");

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate closeDate = LocalDate.of(2023, 11, 5);

        Gather gather = createTestGather(member,
                "제목입니다.",
                "내용입니다.",
                startDate,
                closeDate,
                5,
                GatherCategory.PROJECT,
                "aaa@aaa.com",
                List.of("java", "spring"));

        Gather savedGather = gatherRepository.save(gather);

        // when
        gatherService.deleteGather(savedGather.getId(), member.getId());

        // then
        assertThat(savedGather.isDeleted()).isTrue();
    }

    @DisplayName("모집글 삭제 시 글 작성자가 아니면 예외가 발생한다.")
    @Test
    void deleteGatherInvalidMember(){
        // given
        Member member = createTestMember( "철수", "www.cdn.com");

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate closeDate = LocalDate.of(2023, 11, 5);

        Gather gather = createTestGather(member,
                "제목입니다.",
                "내용입니다.",
                startDate,
                closeDate,
                5,
                GatherCategory.PROJECT,
                "aaa@aaa.com",
                List.of("java", "spring"));

        Gather savedGather = gatherRepository.save(gather);

        Long invalidMemberId = 99999999L;

        // when, then
        assertThatThrownBy(()->gatherService.deleteGather(gather.getId(), invalidMemberId))
                .isInstanceOf(InvalidGatherWriterException.class);
    }

    private Member createTestMember(String nickname, String imageUrl) {
        Member member = Member.builder()
                .nickname(nickname)
                .imageUrl(imageUrl)
                .build();
        return memberRepository.save(member);
    }

    private Gather createTestGather(Member member, String title, String content, LocalDate startDate, LocalDate closeDate, int teamSize, GatherCategory category, String contact, List<String> skills) {
        Gather gather = Gather.builder()
                .title(title)
                .content(content)
                .startDate(startDate)
                .closeDate(closeDate)
                .teamSize(teamSize)
                .category(category)
                .contact(contact)
                .member(member)
                .skills(new ArrayList<>())
                .build();
        gather.replaceGatherSkills(SkillType.convertList(skills));
        return gather;
    }
}