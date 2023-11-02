package com.playdata.pdfolio.project.repository;

import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.project.domain.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectSearchRepository {

    // TODO: 나중에 동적 쿼리 적용
    @Query(value = "select distinct p from Project p " +
            "join fetch p.skills ps " +
            "join p.member m " +
            "where ps.skillType in :skillCategory " +
            "order by p.createdAt desc")
    Page<Project> searchByConditionOrderByCreatedAt(List<SkillType> skillTypeCategory, Pageable pageable);

    @Query(value = "select distinct p from Project p " +
            "join fetch p.skills ps " +
            "join p.member m " +
            "where ps.skillType in :skillCategory " +
            "order by p.viewCount desc")
    Page<Project> searchByConditionOrderByViewCount(List<SkillType> skillTypeCategory, Pageable pageable);

    @Query(value = "select distinct p from Project p " +
            "join fetch p.skills ps " +
            "join p.member m " +
            "where ps.skillType in :skillCategory " +
            "order by p.heartCount desc")
    Page<Project> searchByConditionOrderByHeartCount(List<SkillType> skillTypeCategory, Pageable pageable);

}
