package kz.ablaysharimov.taskmanagementsystem.service.impl;

import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovLoginRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovRegisterRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovAuthResponse;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovUserResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovRole;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovUser;
import kz.ablaysharimov.taskmanagementsystem.exception.AblaySharimovBadRequestException;
import kz.ablaysharimov.taskmanagementsystem.exception.AblaySharimovResourceNotFoundException;
import kz.ablaysharimov.taskmanagementsystem.mapper.AblaySharimovUserMapper;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovUserRepository;
import kz.ablaysharimov.taskmanagementsystem.security.AblaySharimovJwtUtil;
import kz.ablaysharimov.taskmanagementsystem.service.AblaySharimovUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AblaySharimovUserServiceImpl implements AblaySharimovUserService {

    private final AblaySharimovUserRepository userRepository;
    private final AblaySharimovUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AblaySharimovJwtUtil jwtUtil;

    @Override
    public AblaySharimovAuthResponse registerUser(AblaySharimovRegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AblaySharimovBadRequestException("Username already exists: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AblaySharimovBadRequestException("Email already exists: " + request.getEmail());
        }

        AblaySharimovUser user = AblaySharimovUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : AblaySharimovRole.USER)
                .isActive(true)
                .build();

        AblaySharimovUser savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getId());

        String token = jwtUtil.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(savedUser.getUsername())
                        .password(savedUser.getPassword())
                        .authorities(java.util.Collections.singletonList(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + savedUser.getRole().name())))
                        .build()
        );

        return AblaySharimovAuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .expiresIn(86400000L)
                .build();
    }

    @Override
    public AblaySharimovAuthResponse loginUser(AblaySharimovLoginRequest request) {
        log.info("User login attempt: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AblaySharimovUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found"));

        String token = jwtUtil.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(java.util.Collections.singletonList(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                        .build()
        );

        log.info("User login successful: {}", user.getId());

        return AblaySharimovAuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .expiresIn(86400000L)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AblaySharimovUserResponse getUserById(Long id) {
        log.debug("Getting user by id: {}", id);
        AblaySharimovUser user = userRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AblaySharimovUserResponse> getAllUsers(Pageable pageable) {
        log.debug("Getting all active users");
        return userRepository.findByIsActiveTrue(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public AblaySharimovUserResponse updateUser(Long id, String email) {
        log.info("Updating user: {}", id);
        AblaySharimovUser user = userRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found with id: " + id));
        user.setEmail(email);
        AblaySharimovUser updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deactivateUser(Long id) {
        log.info("Deactivating user: {}", id);
        AblaySharimovUser user = userRepository.findById(id)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found with id: " + id));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public AblaySharimovUserResponse uploadProfilePicture(Long userId, MultipartFile file) {
        log.info("Uploading profile picture for user: {}", userId);
        AblaySharimovUser user = userRepository.findById(userId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found with id: " + userId));
        user.setProfilePicturePath("/uploads/" + file.getOriginalFilename());
        AblaySharimovUser updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AblaySharimovUserResponse getCurrentUser() {
        log.debug("Getting current user");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("User not authenticated");
        }

        String username = auth.getName();
        AblaySharimovUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

}

