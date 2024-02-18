package com.dailyReport.dto;

import com.dailyReport.entity.DailyReports;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.text.SimpleDateFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DailyReportsFormDto {

    private Long  reportId;

    // @NotBlank : 데이터 유효성 검증(즉, 필수 입력 값)
    @NotBlank(message = "CATEGORY 를 입력 해주세요.")
    private String category;

    @NotBlank(message = "PROGRAM/SYSTEM 를 입력 해주세요.")
    private String programSystem;

    @NotBlank(message = "DESCRIPTION 를 입력 해주세요.")
    private String description;

    @NotBlank(message = "SOURCEPATH 를 입력 해주세요.")
    private String sourcePath;

    @NotBlank(message = "Date 를 입력 해주세요.")
    private String reportDate;

    private static ModelMapper modelMapper = new ModelMapper();

    // 사용자가 입력한 데이터는 DTO 형태로 서버에 전달됨
    // 데이터베이스에 이 데이터를 저장 or 업데이트 -> JPA 가 이해할 수 있는 Entity 객체 형태로 변환해야 함
    // DTO 데이터를 기반으로 엔티티 객체(DailyReports) 생성
    public DailyReports createDdailyReports() {
        return modelMapper.map(this, DailyReports.class);
    }

    public static DailyReportsFormDto of(DailyReports dailyReports) {
        DailyReportsFormDto dailyReportsFormDto= modelMapper.map(dailyReports, DailyReportsFormDto.class);
        // reportDate 필드 날짜 형식 문자열 변환
        dailyReportsFormDto.setReportDate(new SimpleDateFormat("yyyy-MM-dd").format(dailyReports.getReportDate()));
        return dailyReportsFormDto; // 변환 객체 반환
    }
}