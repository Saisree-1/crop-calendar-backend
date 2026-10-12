package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.WhatIfScenario;

public interface WhatIfScenarioRepo extends JpaRepository<WhatIfScenario, Long> {
}