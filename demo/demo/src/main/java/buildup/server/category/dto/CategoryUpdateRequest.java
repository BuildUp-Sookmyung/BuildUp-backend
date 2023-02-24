package buildup.server.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateRequest {

    private Long id;
    private String categoryName;
    private Long iconId;
}
