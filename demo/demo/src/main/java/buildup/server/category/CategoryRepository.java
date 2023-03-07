package buildup.server.category;

import buildup.server.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByMember(Member member);
    Optional<Category> findByName(String name);
    List<Category> findAllByIdLessThan(Long categoryId);

}
