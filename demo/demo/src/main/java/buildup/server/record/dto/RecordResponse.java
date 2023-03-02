package buildup.server.record.dto;

import buildup.server.record.domain.Record;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordResponse {

    private Long recordId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String title;
    private String experience;
    private String concept;
    private String result;
    private String content;

    private List<String> imgUrls;

    private String url;

    public RecordResponse(Long recordId, Record record, List<String> imgUrls) {
        this.recordId = recordId;
        this.date = record.getDate();
        this.title = record.getTitle();
        this.experience = record.getExperience();
        this.concept = record.getConcept();
        this.result = record.getResult();
        this.content = record.getContent();
        this.imgUrls = imgUrls;
        this.url = record.getUrl();

    }


}
