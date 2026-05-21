package kz.ablaysharimov.taskmanagementsystem.service;

import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovLoginRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.request.AblaySharimovRegisterRequest;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovAuthResponse;
import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AblaySharimovUserService {
    AblaySharimovAuthResponse registerUser(AblaySharimovRegisterRequest request);
    AblaySharimovAuthResponse loginUser(AblaySharimovLoginRequest request);
    AblaySharimovUserResponse getUserById(Long id);
    Page<AblaySharimovUserResponse> getAllUsers(Pageable pageable);
    AblaySharimovUserResponse updateUser(Long id, String email);
    void deactivateUser(Long id);
    AblaySharimovUserResponse uploadProfilePicture(Long userId, MultipartFile file);
    AblaySharimovUserResponse getCurrentUser();
}

