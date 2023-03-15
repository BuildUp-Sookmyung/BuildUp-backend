package buildup.server.member.repository;

import buildup.server.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

//    Optional<Member> findByEmail(String email);

    List<Member> findAllByEmail(String email);

    Optional<Member> findByPassword(String password);
}
