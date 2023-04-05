package buildup.server.record.repository;

import buildup.server.record.domain.Record;
import buildup.server.record.domain.RecordImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordImgRepository extends JpaRepository<RecordImg, Long> {

    List<RecordImg> findAllByRecord(Record record);

    List<RecordImg> findByRecordId(Long recordid);

    Optional<RecordImg> findByRecord(Long record_id);

    Optional<RecordImg> findByRecordUrl(Record record);



}
