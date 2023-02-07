package buildup.server.member.service;

import buildup.server.auth.domain.AuthInfo;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Phone;
import buildup.server.member.exception.MemberErrorCode;
import buildup.server.member.exception.MemberException;
import buildup.server.member.exception.PhoneErrorCode;
import buildup.server.member.exception.PhoneException;
import buildup.server.member.repository.MemberRepository;
import buildup.server.member.repository.PhoneRepository;
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
