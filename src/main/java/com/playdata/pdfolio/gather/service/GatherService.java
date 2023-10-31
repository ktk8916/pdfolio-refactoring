package com.playdata.pdfolio.gather.service;


import com.playdata.pdfolio.gather.domain.dto.SearchDto;
import com.playdata.pdfolio.global.type.SkillType;
import com.playdata.pdfolio.gather.domain.entity.Gather;
import com.playdata.pdfolio.gather.domain.entity.GatherComment;
import com.playdata.pdfolio.gather.domain.entity.GatherReply;
import com.playdata.pdfolio.gather.domain.entity.GatherSkill;
import com.playdata.pdfolio.gather.domain.request.WriteCommentRequest;
import com.playdata.pdfolio.gather.domain.request.WriteReplyRequest;
import com.playdata.pdfolio.gather.domain.request.WriteRequest;
import com.playdata.pdfolio.gather.domain.response.GatherDetailResponse;
import com.playdata.pdfolio.gather.domain.response.GatherResponse;
import com.playdata.pdfolio.gather.repository.GatherCommentRepository;
import com.playdata.pdfolio.gather.repository.GatherReplyRepository;
import com.playdata.pdfolio.gather.repository.GatherRepository;
import com.playdata.pdfolio.gather.repository.GatherSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GatherService {
    private final GatherRepository gatherRepository;
    private final GatherCommentRepository gatherCommentRepository;
    private final GatherReplyRepository gatherReplyRepository;
    private final GatherSkillRepository gatherSkillRepository;


    
    // 모집글 작성
    public void writeGather(WriteRequest writeRequest,Long memberId){

        Gather gather = gatherRepository.save(writeRequest.toEntity(memberId));

        List<SkillType> skillTypes = SkillType.of(writeRequest.skills());
        List<GatherSkill> gatherSkills = createGatherSkills(gather, skillTypes);
        gatherSkillRepository.saveAll(gatherSkills);
    }

    private List<GatherSkill> createGatherSkills(Gather gather, List<SkillType> skillTypes) {
        return skillTypes.stream()
                .map(skill -> GatherSkill.builder()
                        .gather(gather)
                        .skillType(skill)
                        .build())
                .toList();
    }
    
    // 모집글 수정
    public void modifyGather(WriteRequest writeRequest,Long id,Long memberId){
        Optional<Gather> optionalGather = gatherRepository.findById(id);

        if (optionalGather.isPresent()) { //  있는지 확인하고 실행
            Gather existingGather = optionalGather.get(); // find로 찾은 Gather 할당

            existingGather.setTitle(writeRequest.title());
            existingGather.setContent(writeRequest.content());
            existingGather.setStartDate(writeRequest.startDate());
            existingGather.setCloseDate(writeRequest.closeDate());
            existingGather.setTeamSize(writeRequest.teamSize());
            existingGather.setCategory(writeRequest.category());
            existingGather.setContact(writeRequest.contact());
            
            // 수정된거 저장하는데 Transactional끝날때 쿼리 날려줘서 알아서 업데이트해서 아래 코드 필요x
//            Gather modifiedGather = gatherRepository.save(existingGather);
        } else {
            // Gather 엔터티를 찾지 못한 경우 예외 처리 또는 메시지 출력
            // 예: throw new NotFoundException("Gather not found with id: " + writeRequest.id());
        }
    }

    // 모집글 삭제
    public void deleteGather(Long id){
        Gather byId = gatherRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        byId.deleteColumn();  // deleteColumn으로 실제 삭제가 아닌 is_deleted 칼럼 boolean을 변경

        // gatherRepository.deleteById(id);
    }

    // 모집글 상세 보기
    public GatherDetailResponse detailGather(Long id){
        Optional<Gather> byId = gatherRepository.findByGather(id);
        Gather gather = byId.orElseThrow(() ->
                new RuntimeException("Not Found Gather" + id));
        gather.increaseViewCount();
        return new GatherDetailResponse(gather);

//        Gather gather = gatherRepository.findByIdIncludingUndeletedComments(id);
//        return new GatherDetailResponse(gather);
    }

    // 모집글 전체 보기 / 모집글 제목 , 글 내용 , 카테고리 검색
    public Page<GatherResponse> allGather(PageRequest request, SearchDto searchDto){
        Page<GatherResponse> all = gatherRepository.findAllByCondition(request,searchDto);
        return all;
    }



// -----------------------------------------------------------------------------
    // 코멘트 작성
    public void writeGatherComment(WriteCommentRequest writeCommentRequest,Long memberId){
        gatherCommentRepository.save(writeCommentRequest.toEntity(memberId));
    }

    // 코멘트 수정
    public void modifyGatherComment(WriteCommentRequest writeCommentRequest,Long id){
        Optional<GatherComment> optionalGatherComment = gatherCommentRepository.findById(id);

        if (optionalGatherComment.isPresent()) { //  있는지 확인하고 실행
            GatherComment existiongGatherComment = optionalGatherComment.get();
            existiongGatherComment.setContent(writeCommentRequest.content());
        } else {
            // Gather 엔터티를 찾지 못한 경우 예외 처리 또는 메시지 출력
            // 예: throw new NotFoundException("Gather not found with id: " + writeRequest.id());
        }
    }

    // 코멘트 삭제
    public void deleteGatherComment(Long id){
        GatherComment gatherComment = gatherCommentRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        gatherComment.deleteColumn();
    }
// -----------------------------------------------------------------------------
    // 리플라이 작성
    public void writeGatherReply(WriteReplyRequest writeReplyRequest,Long memberId){
        gatherReplyRepository.save(writeReplyRequest.toEntity(memberId));
    }
    // 리플라이 수정
    public void modifyGatherReply(WriteReplyRequest writeReplyRequest,Long id){
        Optional<GatherReply> optionalGatherReply = gatherReplyRepository.findById(id);
        if (optionalGatherReply.isPresent()) { //  있는지 확인하고 실행
            GatherReply existiongGatherReply = optionalGatherReply.get();
            existiongGatherReply.setContent(writeReplyRequest.content());
        } else {
            // Gather 엔터티를 찾지 못한 경우 예외 처리 또는 메시지 출력
            // 예: throw new NotFoundException("Gather not found with id: " + writeRequest.id());
        }
    }
    // 리플라이 삭제
    public void deleteGatherReply(Long id){
        GatherReply gatherReply = gatherReplyRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        gatherReply.deleteColumn();
    }
}
