package com.dailyReport.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

//데이터베이스 테이블과 직접적인 연결이 있는, 데이터를 담는 컨테이너 역할
@Entity //JPA 엔티티
@Table(name = "daily_reports") //daily_reports 라는 데이터베이스 테이블에 매핑
//Lombok 라이브러리를 사용해 자동으로 getter 및 setter 메서드 생성
@Getter
@Setter
@NoArgsConstructor // Lombok 을 사용해 파라미터가 없는 기본 생성자 생성
public class DailyReports {

    @Id // 테이블 기본 키
    @Column(name = "report_id") // report_id 라는 이름의 컬럼에 매핑
    // 자바 클래스의 필드를 특정 데이터베이스 테이블의 컬럼에 명시적으로 매핑하기 위함
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    // 기본 키 값이 자동으로 생성되도록 설정, 여기서는 시퀀스를 사용
    @SequenceGenerator(name = "dailyReportSequence", sequenceName = "daily_reports_seq", allocationSize = 1)
    @Comment("보고서 고유 식별번호") // 데이터베이스 컬럼 설명 추가
    private Long reportId;

    @Column(length = 100)
    @Comment("학습 또는 업무 카테고리")
    private String category;

    @Column(length = 200)
    @Comment("관련 프로그램 또는 시스템")
    private String programSystem;

    @Column(length = 1000)
    @Comment("상세 설명")
    private String description;

    @Column(length = 500)
    @Comment("관련 자료 또는 문서의 위치")
    private String sourcePath;

    @CreatedDate // 엔티티가 생성될 때 현재 날짜와 시간을 자동으로 할당
    @Comment("작성날짜")
    private Date reportDate;
}