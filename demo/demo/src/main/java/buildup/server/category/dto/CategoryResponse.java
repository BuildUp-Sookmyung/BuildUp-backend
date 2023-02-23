package buildup.server.category.dto;

import buildup.server.category.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryResponse {

    private Long categoryId;
    private String categoryName;

    public static List<CategoryResponse> toDtoList(List<Category> entities) {
        List<CategoryResponse> dtos = new ArrayList<>();

        for (Category entity : entities)
            dtos.add(new CategoryResponse(entity.getId(), entity.getName()));

        return dtos;
    }
}
