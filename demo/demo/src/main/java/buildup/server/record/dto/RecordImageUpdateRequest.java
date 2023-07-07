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

    // TODO: 수정
    private Long recordid;

    private String[] deleteUrlList;
    private List<RecordImgRequest> storeUrl;
}
