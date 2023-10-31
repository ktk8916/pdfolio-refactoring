package com.playdata.pdfolio.gather.repository;

import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatherCommentRepository extends JpaRepository <GatherComment,Long> {
}
