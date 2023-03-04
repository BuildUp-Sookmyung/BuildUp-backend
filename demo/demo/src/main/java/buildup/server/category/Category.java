package buildup.server.category;

import buildup.server.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "icon_id")
    private Long iconId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Category(String name, Long iconId, Member member) {
        this.name = name;
        this.iconId = iconId;
        this.member = member;
    }

    public void updateCategory(String name, Long iconId) {
        this.name = name;
        this.iconId = iconId;
    }
}
