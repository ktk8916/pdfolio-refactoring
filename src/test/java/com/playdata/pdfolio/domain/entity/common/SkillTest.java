package com.playdata.pdfolio.domain.entity.common;

import com.playdata.pdfolio.global.exception.NoSuchSkillException;
import com.playdata.pdfolio.global.exception.PdFolioException;
import com.playdata.pdfolio.global.type.Skill;
import com.playdata.pdfolio.global.type.SkillCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SkillTest {

    @ParameterizedTest
    @CsvSource({
            "JAVA, KOTLIN, JAVASCRIPT, LANGUAGE",
            "SPRING, NEST, EXPRESS, FRAMEWORK"
    })
    void findSkillsBySkillNames(String skill1, String skill2, String skill3, String categoryName) {
        // given
        List<String> skillNames = Arrays.asList(skill1, skill2, skill3);

        // when
        List<Skill> skills = Skill.of(skillNames);

        // then
        // Skill이 잘 생성되었는지 확인
        assertThat(skills).hasSize(skillNames.size());

        // 만들어진 Skill들의 skillName 확인 테스트
        assertThat(skills)
                .extracting(Skill::getSkillName)
                .containsExactlyElementsOf(skillNames);

        // 만들어진 Skill들의 category 확인 테스트
        assertThat(skills)
                .extracting(Skill::getCategory)
                .extracting(SkillCategory::getSkillCategoryName)
                .containsOnly(categoryName);
    }

    @Test
    void testNoSuchSkillException() {
        List<String> nonexistentSkills = Arrays.asList("FULLTER", "GOLANG");

        // 처리된 예외가 잘 던져지는지 테스트
        assertThatThrownBy(() -> Skill.of(nonexistentSkills))
                .isInstanceOf(NoSuchSkillException.class);

        assertThatThrownBy(() -> Skill.of(nonexistentSkills))
                .isInstanceOf(PdFolioException.class);
    }

}