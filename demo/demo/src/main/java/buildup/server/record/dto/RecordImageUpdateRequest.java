package buildup.server.record.dto;

import buildup.server.record.domain.RecordImg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecordImageUpdateRequest {

    private Long recordid;

    private List<RecordImgRequest> storeUrl;
}
