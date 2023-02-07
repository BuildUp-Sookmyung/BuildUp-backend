package buildup.server.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private String field;
    //TODO: enum 고민해보기

}
