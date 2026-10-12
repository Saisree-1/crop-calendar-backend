package jar.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.Language;

public interface LanguageRepo extends JpaRepository<Language, Byte> {

}
