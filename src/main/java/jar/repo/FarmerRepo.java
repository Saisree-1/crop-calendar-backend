package jar.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jar.model.Farmer;

public interface FarmerRepo extends JpaRepository<Farmer, Integer> {

    // Find a farmer using their email address
    Optional<Farmer> findByEmail(String email);

    // Find a farmer using their phone number
    Optional<Farmer> findByPhone(String phone);

    // Check whether an email is already registered
    boolean existsByEmail(String email);

    // Check whether a phone number is already registered
    boolean existsByPhone(String phone);
}