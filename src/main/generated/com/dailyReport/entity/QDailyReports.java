package com.dailyReport.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDailyReports is a Querydsl query type for DailyReports
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDailyReports extends EntityPathBase<DailyReports> {

    private static final long serialVersionUID = -1127907709L;

    public static final QDailyReports dailyReports = new QDailyReports("dailyReports");

    public final StringPath category = createString("category");

    public final StringPath description = createString("description");

    public final StringPath programSystem = createString("programSystem");

    public final DateTimePath<java.util.Date> reportDate = createDateTime("reportDate", java.util.Date.class);

    public final NumberPath<Long> reportId = createNumber("reportId", Long.class);

    public final StringPath sourcePath = createString("sourcePath");

    public QDailyReports(String variable) {
        super(DailyReports.class, forVariable(variable));
    }

    public QDailyReports(Path<? extends DailyReports> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDailyReports(PathMetadata metadata) {
        super(DailyReports.class, metadata);
    }

}

