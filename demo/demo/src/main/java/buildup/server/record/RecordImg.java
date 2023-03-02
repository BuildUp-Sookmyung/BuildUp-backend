package buildup.server.record;

import buildup.server.record.Record;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class RecordImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeUrl;
//    private String originalName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Record record;

    private Long index;

    public RecordImg(String storeUrl, Record record) {
        this.storeUrl = storeUrl;
        this.record = record;
    }
}
