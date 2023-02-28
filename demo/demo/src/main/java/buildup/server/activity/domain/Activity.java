package buildup.server.activity.domain;

import buildup.server.category.Category;
import buildup.server.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Activity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @Column(name = "activity_name")
    private String name;

    private String host;

    private String role;
    private String url;

    private String percentage;
    @Setter
    private String activityimg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;

    @Setter
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Activity(String name, String host, String role, String url, LocalDate startDate, LocalDate endDate,Category category, Member member) {
        this.name = name;
        this.host = host;
        this.role = role;
        this.url = url;
        this.startDate = startDate;
        this.endDate = endDate;
//        this.percentage = percentage;
        this.category = category;
        this.member = member;
    }

    public void updateActivity(String name, String host, String role, LocalDate startDate, LocalDate endDate,String url) {
        this.name = name;
        this.host = host;
        this.role = role;
        this.startDate = startDate;
        this.endDate = endDate;
        this.url = url;
    }

}
