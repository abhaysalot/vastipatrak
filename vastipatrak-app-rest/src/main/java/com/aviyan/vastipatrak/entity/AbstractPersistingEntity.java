package com.aviyan.vastipatrak.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractPersistingEntity {
    private String createdUser;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private String modifiedUser;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
}
