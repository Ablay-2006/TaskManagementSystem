package kz.ablaysharimov.taskmanagementsystem.repository;

import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AblaySharimovUserRepository extends JpaRepository<AblaySharimovUser, Long> {
    Optional<AblaySharimovUser> findByUsername(String username);
    Optional<AblaySharimovUser> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Page<AblaySharimovUser> findByIsActiveTrue(Pageable pageable);
}

