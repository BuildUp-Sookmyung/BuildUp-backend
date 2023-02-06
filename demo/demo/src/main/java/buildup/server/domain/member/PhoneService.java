package buildup.server.domain.member;

import buildup.server.auth.domain.AuthInfo;
import buildup.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void savePhone(AuthInfo info) {
        Member member = memberRepository.findByUsername(info.getMemberRefreshToken().getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Optional<Phone> optionalPhone = phoneRepository.findByMember(member);
        if (optionalPhone.isPresent())
            throw new RuntimeException("이미 가입된 번호입니다.");
        phoneRepository.save(new Phone(member, optionalPhone.get().getNumber()));
    }
}
