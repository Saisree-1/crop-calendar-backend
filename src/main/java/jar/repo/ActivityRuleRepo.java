package jar.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.ActivityRule;

public interface ActivityRuleRepo extends JpaRepository<ActivityRule, Integer> {

    List<ActivityRule> findByCropId(Integer cropId);

}