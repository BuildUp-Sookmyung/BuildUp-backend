package buildup.server.category;

import buildup.server.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByMember(Member member);
    List<Category> findAllByIdLessThan(Long categoryId);

}
