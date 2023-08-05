package buildup.server.activity.domain;

import buildup.server.category.Category;
import buildup.server.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Setter
    @Column(name = "activity_img")
    private String activityImg;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Activity(String name,
                    String host,
                    String role,
                    String url,
                    String activityImg,
                    LocalDate startDate,
                    LocalDate endDate,
                    Category category,
                    Member member) {
        this.name = name;
        this.host = host;
        this.role = role;
        this.url = url;
        this.activityImg = activityImg;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.member = member;
    }

    public void updateActivity(Category category, String name, String host, String role, LocalDate startDate, LocalDate endDate,String url) {
        this.category = category;
        this.name = name;
        this.host = host;
        this.role = role;
        this.startDate = startDate;
        this.endDate = endDate;
        this.url = url;
    }

}
