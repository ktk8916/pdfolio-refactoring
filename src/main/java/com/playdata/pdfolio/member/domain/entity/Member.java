package com.playdata.pdfolio.member.domain.entity;

import com.playdata.pdfolio.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String provider;
    private String providerId;
    private String imageUrl;
    @OneToMany(mappedBy = "member")
    @Cascade(CascadeType.ALL)
    private List<MemberSkill> skills = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    public void signup(String nickname, String imageUrl, List<MemberSkill> skills){
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.skills.addAll(skills);
        this.status = MemberStatus.MEMBER;
    }

    public void update(String nickname, String imageUrl, List<MemberSkill> skills){
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.skills.clear();
        this.skills.addAll(skills);
    }

    public static Member fromId(Long id){
        Member member = new Member();
        member.id = id;
        return member;
    }

    public static Member fromOauth2(String provider, String providerId){
        return Member.builder()
                .provider(provider)
                .providerId(providerId)
                .status(MemberStatus.UNAFFILIATED)
                .build();
    }

    @Builder
    public Member(String nickname, String provider, String providerId, String imageUrl, List<MemberSkill> skills, MemberStatus status) {
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        this.imageUrl = imageUrl;
        this.skills = skills;
        this.status = status;
    }
}
