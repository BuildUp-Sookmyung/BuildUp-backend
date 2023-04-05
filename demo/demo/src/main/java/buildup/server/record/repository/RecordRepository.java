package buildup.server.record.repository;

import buildup.server.activity.domain.Activity;
import buildup.server.member.domain.Member;
import buildup.server.record.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findAllByActivity(Activity activity);

    Optional<Record> findByUrl(String deleteUrl);
}
