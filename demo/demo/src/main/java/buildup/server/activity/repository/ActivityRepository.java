package buildup.server.activity.repository;

import buildup.server.activity.domain.Activity;
import buildup.server.category.Category;
import buildup.server.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findAllByMember(Member member);
    List<Activity> findAllByMemberAndCategory(Member member, Category category);
    List<Activity> findAllByNameContaining(String keyword);
}
