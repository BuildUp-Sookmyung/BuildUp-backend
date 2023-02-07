package buildup.server.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "record_id")
    private Long id;

    private String title;

    private String experience;
    private String concept;
    private String result;
    private String content;
    private LocalDate date;

    @ManyToOne
    @JoinColumn
    private Activity activity;
}
