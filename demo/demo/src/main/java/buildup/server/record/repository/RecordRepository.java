package buildup.server.record.repository;

import buildup.server.activity.domain.Activity;
import buildup.server.record.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findAllByActivity(Activity activity);
}
