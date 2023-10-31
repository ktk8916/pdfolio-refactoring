package com.playdata.pdfolio.gather.repository;

import com.playdata.pdfolio.gather.domain.entity.Gather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GatherRepository extends JpaRepository <Gather,Long> , GatherSearchRepository {
    @Query("select g from Gather g " +
            "left join fetch g.skills s " +
//            "join fetch s.skill " +
            "where g.id = :id and g.isDeleted = false")
    Optional<Gather> findByGather(@Param("id") Long id);


    //    @Query("select g from Gather g " +
    //            "where (g.title like %:keyword% or g.content like %:keyword%)" +
    //            " and g.category = :category" +
    //            " and g.isDeleted = false")
    //    Page<Gather> findSearchKeyword(Pageable request,
    //                                   @Param("keyword") String keyword,
    //                                   @Param("category") GatherCategory category);

}
