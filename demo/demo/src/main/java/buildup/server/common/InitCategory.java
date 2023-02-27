package buildup.server.common;

import buildup.server.category.Category;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitCategory {

    private final InitCategoryService initCategoryService;

    @PostConstruct
    public void init() {
        initCategoryService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitCategoryService {

        private final EntityManager em;

        public void init() {
            Category category1 = createCategory("대외활동", 1L);
            Category category2 = createCategory("공모전", 1L);
            Category category3 = createCategory("동아리", 1L);
            Category category4 = createCategory("프로젝트", 1L);
            Category category5 = createCategory("자격증", 1L);
            Category category6 = createCategory("교내활동", 1L);
            em.persist(category1);
            em.persist(category2);
            em.persist(category3);
            em.persist(category4);
            em.persist(category5);
            em.persist(category6);
        }

        private Category createCategory(String name, Long iconId) {
            return new Category(name, iconId, null);
        }
    }

}
