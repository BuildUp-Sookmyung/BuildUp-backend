package buildup.server.record.domain;

import buildup.server.activity.domain.Activity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;
    private String title;
    private String experience;
    private String concept;
    private String result;
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String url;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;
    @Transient
    @OneToMany(mappedBy = "record")
    private List<RecordImg> images = new ArrayList<>();

    @Builder
    public Record(String title, String experience, String concept, String result, String content, LocalDate date, String url, Activity activity) {
        this.title = title;
        this.experience = experience;
        this.concept = concept;
        this.result = result;
        this.content = content;
        this.date = date;
        this.url = url;
        this.activity = activity;
    }

    public void updateRecord(String title, String experience, String concept, String result, String content, LocalDate date, String url) {
        this.title = title;
        this.experience = experience;
        this.concept = concept;
        this.result = result;
        this.content = content;
        this.date = date;
        this.url = url;
    }

    public void updateRecordImage(List<RecordImg> images){
        this.images = images;
        for (RecordImg img : images) {
            img.setRecord(this);
        }
    }
}