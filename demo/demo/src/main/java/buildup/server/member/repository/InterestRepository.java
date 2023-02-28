package buildup.server.member.repository;

import buildup.server.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findAllByFieldContaining(String keyword);
}
