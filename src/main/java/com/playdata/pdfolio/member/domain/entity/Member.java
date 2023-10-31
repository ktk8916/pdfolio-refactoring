package com.playdata.pdfolio.member.domain.entity;

import com.playdata.pdfolio.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    private Set<MemberSkill> skills = new HashSet<>();

    public void update(String nickname, String imageUrl){
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
