package buildup.server.category;

import buildup.server.category.dto.CategoryResponse;
import buildup.server.category.dto.CategorySaveRequest;
import buildup.server.category.dto.CategoryUpdateRequest;
import buildup.server.category.exception.CategoryErrorCode;
import buildup.server.category.exception.CategoryException;
import buildup.server.member.domain.Member;
import buildup.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberService memberService;

    @Transactional
    public Category createCategory(CategorySaveRequest request) {
        Member member = memberService.findCurrentMember();
        checkDuplicateCategory(member, request.getCategoryName());
        return categoryRepository.save(new Category(request.getCategoryName(), request.getIconId(), member));
    }

    @Transactional
    public void updateCategory(CategoryUpdateRequest request) {
        Member member = memberService.findCurrentMember();
        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        checkCategoryAuth(member, category);
        category.updateCategory(request.getCategoryName(), request.getIconId());
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        Member member = memberService.findCurrentMember();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
        checkCategoryAuth(member, category);
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> readCategories() {
        Member member = memberService.findCurrentMember();
        return CategoryResponse.toDtoList(categoryRepository.findAllByMember(member));
    }

    private void checkDuplicateCategory(Member member, String categoryName) {
        List<Category> categories = categoryRepository.findAllByMember(member);
        for (Category category : categories) {
            if (categoryName.equals(category.getName()))
                throw new CategoryException(CategoryErrorCode.CATEGORY_DUPLICATED);
        }
    }

    private void checkCategoryAuth(Member member, Category target) {
        List<Category> categories = categoryRepository.findAllByMember(member);
        for (Category category : categories) {
            if (category.equals(target))
                return;
        }
        throw new CategoryException(CategoryErrorCode.CATEGORY_NO_AUTH);
    }
}
