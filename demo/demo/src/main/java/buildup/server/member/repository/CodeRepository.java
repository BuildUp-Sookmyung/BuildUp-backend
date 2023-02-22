package buildup.server.member.repository;

import buildup.server.member.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {

    Optional<Code> findByEmail(String email);
}
