package buildup.server.record;

import buildup.server.activity.domain.Activity;
import buildup.server.category.Category;
import buildup.server.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findAllByActivity(Activity activity);
}
