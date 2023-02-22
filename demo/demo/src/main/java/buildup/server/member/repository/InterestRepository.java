package buildup.server.member.repository;

import buildup.server.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    @Query(value = "select i from Interest I where i.profile.id =: profileId", nativeQuery = true)
    List<Interest> findAllByProfileId(Long profileId);
}
