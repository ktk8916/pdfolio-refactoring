package com.playdata.pdfolio.domain.entity.common;


import com.playdata.pdfolio.global.exception.ColumnAlreadyDeletedException;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    private Boolean isDeleted=false;

    public void deleteColumn(){
        if(isDeleted){
            throw new ColumnAlreadyDeletedException();
        }
        this.deletedAt = LocalDateTime.now();
        this.isDeleted = true;
    }
}