package buildup.server.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileSaveRequest {

    private String nickname;
    private String email;
    private String school;
    private String major;
    private String grade;
    private String imgUrl;

}
