package com.playdata.pdfolio.member.domain.entity;

import com.playdata.pdfolio.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nickname;
    private String provider;
    private String providerId;
    private String imageUrl;
    @OneToMany(mappedBy = "member")
    private List<MemberSkill> skills = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    public void update(String nickname, String imageUrl){
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public static Member fromOauth2(String provider, String providerId){
        return Member.builder()
                .provider(provider)
                .providerId(providerId)
                .build();
    }

    @Builder
    public Member(String name, String nickname, String provider, String providerId, String imageUrl, List<MemberSkill> skills, MemberStatus status) {
        this.name = name;
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        this.imageUrl = imageUrl;
        this.skills = skills;
        this.status = status;
    }
}
