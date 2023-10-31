package com.playdata.pdfolio.member.repository;

import com.playdata.pdfolio.member.domain.entity.Member;
import com.playdata.pdfolio.member.domain.entity.MemberSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberSkillRepository extends JpaRepository<MemberSkill, Long> {

    List<MemberSkill> findByMember(Member member);
    void deleteByMember(Member member);

}
