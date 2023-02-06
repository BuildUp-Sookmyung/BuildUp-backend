package buildup.server.member;

import buildup.server.auth.domain.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void savePhone(String phone, AuthInfo info) {
        Member member = memberRepository.findByUsername(info.getMemberRefreshToken().getUsername())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (phoneRepository.findByMember(member).isPresent())
            throw new PhoneException(PhoneErrorCode.PHONE_DUPLICATED);
        phoneRepository.save(new Phone(member, phone));
    }
}
