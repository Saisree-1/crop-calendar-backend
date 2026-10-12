package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.Translation;

public interface TranslationRepo extends JpaRepository<Translation, Long> {
}