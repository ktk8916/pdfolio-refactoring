package com.playdata.pdfolio.gather.service;


import com.playdata.pdfolio.gather.domain.dto.SearchDto;
import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherReply;
import com.playdata.pdfolio.gather.domain.request.GatherEditRequest;
import com.playdata.pdfolio.gather.domain.request.GatherReplyWriteRequest;
import com.playdata.pdfolio.gather.domain.request.GatherWriteRequest;
import com.playdata.pdfolio.gather.domain.response.GatherDetailResponse;
import com.playdata.pdfolio.gather.domain.response.GatherResponse;
import com.playdata.pdfolio.gather.repository.GatherReplyRepository;
import com.playdata.pdfolio.gather.repository.GatherRepository;
import com.playdata.pdfolio.global.exception.BadRequestException;
import com.playdata.pdfolio.global.exception.ErrorCode;
import com.playdata.pdfolio.global.exception.ForbiddenException;
import com.playdata.pdfolio.global.exception.NotFoundException;
import com.playdata.pdfolio.global.type.SkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GatherService {
    private final GatherRepository gatherRepository;
    private final GatherReplyRepository gatherReplyRepository;

    public void writeGather(Long memberId, GatherWriteRequest request){
        if(!isValidDuration(request.startDate(), request.closeDate())){
            throw new BadRequestException(ErrorCode.INVALID_DURATION);
        }

        Gather gather = gatherRepository.save(request.toEntity(memberId));

        List<SkillType> skillTypes = SkillType.convertList(request.skills());

        gather.replaceGatherSkills(skillTypes);
    }

    @Transactional
    public void editGather(Long gatherId, Long memberId, GatherEditRequest request){
        Gather gather = findGatherById(gatherId);

        if(!isValidGatherWriter(gather, memberId)){
            throw new ForbiddenException(ErrorCode.INVALID_AUTHOR, Gather.class, gatherId, memberId);
        }

        if(!isValidDuration(request.startDate(), request.closeDate())){
            throw new BadRequestException(ErrorCode.INVALID_DURATION);
        }

        gather.edit(
                request.title(),
                request.content(),
                request.startDate(),
                request.closeDate(),
                request.teamSize(),
                request.category(),
                request.contact(),
                SkillType.convertList(request.skills())
        );
    }

    @Transactional
    public void deleteGather(Long gatherId, Long memberId){
        Gather gather = findGatherById(gatherId);

        if(!isValidGatherWriter(gather, memberId)){
            throw new ForbiddenException(ErrorCode.INVALID_AUTHOR, Gather.class, gatherId, memberId);
        }

        gather.delete();
    }

    @Transactional
    public GatherDetailResponse getGatherById(Long gatherId){
        Gather gather = findGatherById(gatherId);
        gather.increaseViewCount();
        return GatherDetailResponse.fromEntity(gather);
    }

    // 모집글 전체 보기 / 모집글 제목 , 글 내용 , 카테고리 검색
    public Page<GatherResponse> allGather(PageRequest request, SearchDto searchDto){
        Page<GatherResponse> all = gatherRepository.findAllByCondition(request,searchDto);
        return all;
    }


    // 리플라이 수정
    public void modifyGatherReply(GatherReplyWriteRequest gatherReplyWriteRequest, Long id){
        Optional<GatherReply> optionalGatherReply = gatherReplyRepository.findById(id);
        if (optionalGatherReply.isPresent()) { //  있는지 확인하고 실행
            GatherReply existiongGatherReply = optionalGatherReply.get();
//            existiongGatherReply.setContent(writeReplyRequest.content());
        } else {
            // Gather 엔터티를 찾지 못한 경우 예외 처리 또는 메시지 출력
            // 예: throw new NotFoundException("Gather not found with id: " + writeRequest.id());
        }
    }
    // 리플라이 삭제
    public void deleteGatherReply(Long id){
        GatherReply gatherReply = gatherReplyRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        gatherReply.delete();
    }

    private Gather findGatherById(Long gatherId) {
        Gather gather = gatherRepository.findByIdMemberFetch(gatherId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CONTENT, Gather.class, gatherId));

        if(gather.isDeleted()){
            throw new NotFoundException(ErrorCode.DELETED_CONTENT, Gather.class, gatherId);
        }
        return gather;
    }

    private boolean isValidGatherWriter(Gather gather, Long memberId){
        return gather.getMember().getId().equals(memberId);
    }

    private boolean isValidDuration(LocalDate startDate, LocalDate closeDate){
        return startDate.isBefore(closeDate);
    }
}
