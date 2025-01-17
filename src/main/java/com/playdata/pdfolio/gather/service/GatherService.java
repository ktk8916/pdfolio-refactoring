package com.playdata.pdfolio.gather.service;


import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherCategory;
import com.playdata.pdfolio.gather.domain.request.GatherEditRequest;
import com.playdata.pdfolio.gather.domain.request.GatherWriteRequest;
import com.playdata.pdfolio.gather.domain.response.GatherCommentResponse;
import com.playdata.pdfolio.gather.domain.response.GatherDetailResponse;
import com.playdata.pdfolio.gather.domain.response.GatherSearchResponse;
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

@Service
@RequiredArgsConstructor
public class GatherService {
    private final GatherRepository gatherRepository;

    public List<GatherCommentResponse> getCommentsByGatherId(Long gatherId) {
        return gatherRepository.findCommentsByGatherId(gatherId).stream()
                .map(GatherCommentResponse::fromEntity)
                .toList();
    }

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

    public GatherSearchResponse getGathersByCondition(String keyword, GatherCategory category, List<SkillType> skills, PageRequest pageRequest){
        Page<Gather> result = gatherRepository.findGathersByCondition(keyword, category, skills, pageRequest);
        return GatherSearchResponse.of(result.getContent(), result.getTotalPages());
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
