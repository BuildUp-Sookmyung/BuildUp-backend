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
    @Column(name = "record_img_id")
    private Long id;

    @Setter
    @Column(name="store_url")
    private String storeUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Record record;

    public RecordImg(String storeUrl, Record record) {
        this.storeUrl = storeUrl;
        this.record = record;
    }

    public void RecordImgUpdate(String storeUrl){
        this.storeUrl = storeUrl;
    }
}
