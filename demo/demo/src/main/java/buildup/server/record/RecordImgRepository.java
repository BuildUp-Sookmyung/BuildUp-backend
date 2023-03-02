package buildup.server.record;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordImgRepository extends JpaRepository<RecordImg, Long> {

    List<RecordImg> findAllByRecord(Record record);
}
