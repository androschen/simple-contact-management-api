package com.restapi.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import java.util.Date;

public class BaseEntity {
    //TODO insert to table
    public static final String COLUMN_MARK_FOR_DELETE = "MARK_FOR_DELETE";
    public static final String COLUMN_CREATED_BY = "CREATED_BY";
    public static final String COLUMN_CREATED_DATE = "CREATED_DATE";
    public static final String COLUMN_UPDATED_BY = "UPDATED_BY";
    public static final String COLUMN_UPDATED_DATE = "UPDATED_DATE";

    @Column(name = BaseEntity.COLUMN_MARK_FOR_DELETE)
    private boolean markForDelete;

    @CreatedBy
    @Column(name = BaseEntity.COLUMN_CREATED_BY)
    private String createdBy;

    @Column(name = BaseEntity.COLUMN_CREATED_DATE)
    private Date createdDate;

    @LastModifiedBy
    @Column(name = BaseEntity.COLUMN_UPDATED_BY)
    private String updateBy;

    @Column(name = BaseEntity.COLUMN_UPDATED_DATE)
    private Date updatedDate;
}
