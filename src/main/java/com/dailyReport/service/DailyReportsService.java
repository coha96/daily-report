package com.dailyReport.service;


import com.dailyReport.dto.DailyReportsDto;
import com.dailyReport.dto.DailyReportsFormDto;
import com.dailyReport.entity.DailyReports;
import com.dailyReport.repository.DailyReportsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DailyReportsService {

    // 데이터 접근을 위한 Repository
    private final DailyReportsRepository dailyReportsRepository;
    private  final ModelMapper modelMapper; // 객체 매핑

    // ★ 보고서 저장
    // DailyReportsFormDto 를 받아 DailyReports Entity 객체로 변환 후 저장
    public DailyReports saveDailyReports(DailyReportsFormDto dailyReportsFormDto) throws Exception
    {
        DailyReports dailyReports = dailyReportsFormDto.createDdailyReports();
        return dailyReportsRepository.save(dailyReports);
    }

    // ★ 보고서 목록 조회
    @Transactional(readOnly = true)
    // Pageable 객체 사용 -> 페이징 처리된 보고서 목록 조회
    public Page<DailyReportsDto> listDailyReports(Pageable pageable)
    {
        Long totalCount = dailyReportsRepository.count();
        List<DailyReports> dailyReports = dailyReportsRepository.findAllCustom(pageable.getOffset(), pageable.getPageSize());

        List<DailyReportsDto> dailyReportsDtoList = dailyReports.stream().map((dailreport) -> modelMapper.map(dailreport, DailyReportsDto.class)).collect(Collectors.toList());

        log.info("############=> Long totalCount  {} ", totalCount);

        log.info("############=> Long dailyReportsDtoList  {} ", dailyReportsDtoList.size());

        // 페이지 구현 객체를 생성하여 반환
        return new PageImpl<>(dailyReportsDtoList, pageable, totalCount);
    }

    /**
     * 보고서 정보 가져오기
     * @param reportId
     */
    @Transactional(readOnly = true)
    // 특정 reportId를 사용하여 해당 보고서 상세 정보 조회
    public DailyReportsFormDto getReportDtl(Long reportId)
    {
        DailyReports dailyReports = dailyReportsRepository.findById(reportId).orElseThrow(EntityNotFoundException::new);
        return DailyReportsFormDto.of(dailyReports);
    }

    /**
     * 보고서 업데이트 처리
     * @param dailyReportsFormDto
     * @throws Exception
     */

    public void updateDailyReports(DailyReportsFormDto dailyReportsFormDto) throws Exception
    {
        DailyReports dailyReports = dailyReportsRepository.findById(dailyReportsFormDto.getReportId()).orElseThrow(EntityNotFoundException::new);

        // Dirty Checking(더티 체킹)
        // 엔티티 상태 변경 자동 감지 / 데이터베이스 반영
        dailyReports.setCategory(dailyReportsFormDto.getCategory());
        dailyReports.setDescription(dailyReportsFormDto.getDescription());
        dailyReports.setProgramSystem(dailyReportsFormDto.getProgramSystem());
        dailyReports.setSourcePath(dailyReportsFormDto.getSourcePath());
        dailyReports.setReportDate(new SimpleDateFormat("yyyy-MM-dd").parse(dailyReportsFormDto.getReportDate()));
    }

    // ★ 최신 보고서 ID 조회 - 현재 저장된 보고서 중 가장 큰 reportid 값 조회
    public Long getLastReportId()
    {
        return dailyReportsRepository.getMaxReportId();
    }

    // 특정 reportId 사용하여 해당 보고서 삭제
    public void deleteReport(Long reportId)
    {
        DailyReports dailyReports = dailyReportsRepository.findById(reportId).orElseThrow(EntityNotFoundException::new);
        dailyReportsRepository.delete(dailyReports);
    }
}