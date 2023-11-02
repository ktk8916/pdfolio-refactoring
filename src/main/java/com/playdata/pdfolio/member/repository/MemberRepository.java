package com.playdata.pdfolio.member.repository;

import com.playdata.pdfolio.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByProviderAndProviderId(String provider, String providerId);
    @Query("select m from Member m " +
            "join fetch m.skills " +
            "where m.id = :id")
    Optional<Member> findByIdFetchMemberSkill(@Param("id") Long id);
}
