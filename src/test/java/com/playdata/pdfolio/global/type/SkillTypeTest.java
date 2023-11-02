package com.playdata.pdfolio.global.type;

import com.playdata.pdfolio.global.exception.NoMatchingSkillTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SkillTypeTest {

    @DisplayName("이름으로 SkillType을 생성한다.")
    @Test
    void fromName(){
        // given
        String java = "java";

        // when
        SkillType skillType = SkillType.fromName(java);

        // then
        assertThat(skillType).isEqualTo(SkillType.JAVA);
    }

    @DisplayName("유효하지 않은 이름인 경우 예외가 발생한다.")
    @Test
    void invalidName(){
        // given
        String invalidName = "invalidName";

        // when, then
        assertThatThrownBy(()->SkillType.fromName(invalidName))
                .isInstanceOf(NoMatchingSkillTypeException.class);
    }

    @DisplayName("skill list를 SkillType list로 변환한다.")
    @Test
    void convertList(){
        // given
        List<String> skills = List.of("java", "spring");

        // when
        List<SkillType> skillTypes = SkillType.convertList(skills);

        // then
        assertThat(skillTypes).hasSize(2)
                .containsExactlyInAnyOrder(
                        SkillType.JAVA,
                        SkillType.SPRING);
    }

    @DisplayName("중복된 스킬이 있을 시 중복을 제거하고 SkillType list로 변환한다.")
    @Test
    void convertListIncludesDuplicateSkills(){
        // given
        List<String> skills = List.of("java", "java", "spring");

        // when
        List<SkillType> skillTypes = SkillType.convertList(skills);

        // then
        assertThat(skillTypes).hasSize(2)
                .containsExactlyInAnyOrder(
                        SkillType.JAVA,
                        SkillType.SPRING);
    }

}