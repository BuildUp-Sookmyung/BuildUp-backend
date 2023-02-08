package buildup.server.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "record")
    private List<RecordImg> images = new ArrayList<>();

    @Builder
    public Record(String title, String experience, String concept, String result, String content, LocalDate date, Activity activity) {
        this.title = title;
        this.experience = experience;
        this.concept = concept;
        this.result = result;
        this.content = content;
        this.date = date;
        this.activity = activity;
    }
}
