package com.sid.gl.elections;

import com.sid.gl.commons.BaseEntity;
import com.sid.gl.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
//@Table(name = "tb_bulletin")
@Table(name = "tr_bulletin",
        indexes = {@Index(name = "idx_candidat_election", columnList="candidate_id, election_id", unique = true)})
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Bulletin extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;
    String partyName;
    String parcours;

    //private Long electionId;

}
