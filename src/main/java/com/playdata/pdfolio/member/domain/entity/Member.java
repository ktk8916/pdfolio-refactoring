package com.playdata.pdfolio.member.domain.entity;

import com.playdata.pdfolio.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nickname;
    private String providerId;
    private String providerName;
    private String imageUrl;
    @OneToMany(mappedBy = "member")
    private List<MemberSkill> skills = new ArrayList<>();

    public void update(String nickname, String imageUrl){
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
