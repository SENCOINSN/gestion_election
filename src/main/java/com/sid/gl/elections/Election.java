package com.sid.gl.elections;


import com.sid.gl.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "tb_elections")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Election extends BaseEntity {
    @Column(unique = true, nullable = false) //index uniq not null
    private String name;
    private String description;
    @Temporal(TemporalType.DATE) // format :  yyyy-MM-dd
    private Date startDate;
    private LocalDateTime endDate; // format : yyyy-MM-ddTHH:mm:ss
    private boolean active=false;
}
