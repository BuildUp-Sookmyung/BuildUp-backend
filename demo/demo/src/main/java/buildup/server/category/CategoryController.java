package buildup.server.category;

import buildup.server.common.response.StringResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping()
    public StringResponse createCategory(@RequestBody CategorySaveRequest request) {
        Long id = categoryService.createCategory(request).getId();
        return new StringResponse("카테고리를 생성했습니다. id: " + id);
    }
}
