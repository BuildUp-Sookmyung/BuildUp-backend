package buildup.server.record.domain;

import buildup.server.record.domain.Record;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class RecordImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String storeUrl;
    private String originalName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Record record;

    private Long index;

    public RecordImg(String storeUrl, Record record) {
        this.storeUrl = storeUrl;
        this.record = record;
    }

    public void RecordImgUpdate(String storeUrl){
        this.storeUrl = storeUrl;
    }
}
