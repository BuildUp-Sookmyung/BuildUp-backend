package buildup.server.record.dto;

import buildup.server.record.domain.Record;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordListResponse {

    private Long recordId;

    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;


    public RecordListResponse(Long recordId, String title, LocalDate date) {
        this.recordId = recordId;
        this.title = title;
        this.date = date;
    }
    public static List<RecordListResponse> toDtoList(List<Record> entities) {
        List<RecordListResponse> dtos = new ArrayList<>();

        for (Record entity : entities)
            dtos.add(new RecordListResponse(entity.getId(),entity.getTitle(), entity.getDate()));

        return dtos;
    }
}
