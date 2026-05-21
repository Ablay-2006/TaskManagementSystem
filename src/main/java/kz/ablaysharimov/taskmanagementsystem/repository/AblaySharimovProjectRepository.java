package kz.ablaysharimov.taskmanagementsystem.repository;

import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovProject;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AblaySharimovProjectRepository extends JpaRepository<AblaySharimovProject, Long> {
    List<AblaySharimovProject> findByOwnerId(Long ownerId);
    Page<AblaySharimovProject> findByStatus(AblaySharimovProjectStatus status, Pageable pageable);
    List<AblaySharimovProject> findByMembersId(Long userId);
    Page<AblaySharimovProject> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT p FROM AblaySharimovProject p WHERE p.owner.id = :userId OR :userId IN (SELECT m.id FROM p.members m) ORDER BY p.createdAt DESC")
    Page<AblaySharimovProject> findProjectsByOwnerOrMember(@Param("userId") Long userId, Pageable pageable);
}

