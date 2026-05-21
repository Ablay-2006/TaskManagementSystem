package kz.ablaysharimov.taskmanagementsystem.config;

import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("securityChecker")
@RequiredArgsConstructor
public class AblaySharimovSecurityChecker {

    private final AblaySharimovUserRepository userRepository;

    public boolean isCurrentUser(Long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .map(user -> user.getId().equals(userId))
                .orElse(false);
    }
}

