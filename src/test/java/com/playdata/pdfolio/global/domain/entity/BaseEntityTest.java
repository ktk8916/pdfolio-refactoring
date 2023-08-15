package com.playdata.pdfolio.global.domain.entity;

import com.playdata.pdfolio.global.exception.CommonException;
import com.playdata.pdfolio.global.exception.CommonExceptionMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseEntityTest {

    @Test
    void 컬럼_삭제_성공() {
        //given
        BaseEntity baseEntity = new BaseEntity() {};
        //when
        baseEntity.deleteColumn();
        //then
        assertThat(baseEntity.getIsDeleted()).isTrue();
        assertThat(baseEntity.getDeletedAt()).isNotNull();
    }

    @Test
    void 삭제된_컬럼_삭제시_예외() {
        //given
        BaseEntity baseEntity = new BaseEntity() {};
        baseEntity.deleteColumn();

        //when, then
        assertThrows(
                CommonException.class,
                baseEntity::deleteColumn,
                CommonExceptionMessage.COLUMN_ALREADY_DELETED.name());
    }
}