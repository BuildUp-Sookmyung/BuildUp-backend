package buildup.server.record.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecordUpdateRequest {

    private Long id;
    private String recordTitle;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String experienceName;

    private String conceptName;

    private String resultName;

    private String content;

    private String urlName;



}
