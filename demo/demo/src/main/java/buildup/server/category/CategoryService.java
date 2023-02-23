package buildup.server.category;

import buildup.server.member.domain.Member;
import buildup.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberService memberService;

    @Transactional
    public Category createCategory(CategorySaveRequest request) {
        Member member = memberService.findCurrentMember();
        // Todo: 해당 멤버가 만든 카테고리 중 같은 이름이 없는지 확인

        return categoryRepository.save(new Category(request.getCategoryName(), member));
    }
}
