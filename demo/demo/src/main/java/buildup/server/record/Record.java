package buildup.server.record;

import buildup.server.activity.domain.Activity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @JoinColumn
    private Activity activity;

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
}