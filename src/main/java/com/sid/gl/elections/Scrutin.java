package com.sid.gl.elections;

import com.sid.gl.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tr_scrutin",
        indexes = {@Index(name = "idx_bulletin_electeur", columnList="bulletin_id,electeur_id", unique = true)})
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Scrutin extends BaseEntity {
    private Long bulletinId;
    private Long electeurId;
}
