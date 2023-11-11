package com.playdata.pdfolio.gather.repository;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GatherRepository extends JpaRepository<Gather, Long>, GatherSearchRepository {

    @Query("select g from Gather g " +
            "left join fetch g.member " +
            "where g.id = :id ")
    Optional<Gather> findByIdMemberFetch(@Param("id") Long id);
}
