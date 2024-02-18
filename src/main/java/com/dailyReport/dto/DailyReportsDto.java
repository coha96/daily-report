package com.dailyReport.dto;

import com.dailyReport.entity.DailyReports;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // 생성자만 사용할때 발생할 수 있는 문제 개선
@ToString // toString() 메서드 자동 생성
public class DailyReportsDto {

    // DailyReports Entity 각 필드와 동일한 이름과 타입의 필드들로 구성
    private Long  reportId;
    private String category;
    private String programSystem;
    private String description;
    private String sourcePath;
    private Date reportDate;

    private static ModelMapper modelMapper = new ModelMapper();

    // * ModelMapper : 객체 간 데이터를 매핑해주는 유틸리티(사용자의 편리성을 향상하는 유용한 소프트웨어)
    // 이를 사용하여 DailyReports 엔티티 인스턴스를 DailyReportsDto 객체로 변환
    public static DailyReportsDto of(DailyReports dailyReports) {
        return modelMapper.map(dailyReports, DailyReportsDto.class);
    }
}