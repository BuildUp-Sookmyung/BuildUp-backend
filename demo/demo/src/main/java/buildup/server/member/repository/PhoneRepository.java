package buildup.server.member.repository;

import buildup.server.member.domain.Member;
import buildup.server.member.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Optional<Phone> findByMember(Member member);
}
