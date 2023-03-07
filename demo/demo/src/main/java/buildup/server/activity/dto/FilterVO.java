package buildup.server.activity.dto;

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
public class FilterVO {

    private List<String> categories;

    @DateTimeFormat(pattern = "yyyy-MM")
    private String start;

    @DateTimeFormat(pattern = "yyyy-MM")
    private String end;
}
