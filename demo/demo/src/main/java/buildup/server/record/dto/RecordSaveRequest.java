package buildup.server.record.dto;

import buildup.server.activity.domain.Activity;
import buildup.server.record.domain.Record;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecordSaveRequest {

    private Long activityId;
    private String recordTitle;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String experienceName;

    private String conceptName;

    private String resultName;

    private String content;

    private String urlName;

    public Record toRecord(Activity activity) {
        return Record.builder()
                .title(recordTitle)
                .date(date)
                .experience(experienceName)
                .concept(conceptName)
                .result(resultName)
                .content(content)
                .url(urlName)
                .activity(activity)
                .build();
    }


}
