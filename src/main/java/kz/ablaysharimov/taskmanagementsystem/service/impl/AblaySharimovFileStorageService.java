package kz.ablaysharimov.taskmanagementsystem.service.impl;

import kz.ablaysharimov.taskmanagementsystem.dto.response.AblaySharimovAttachmentResponse;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovAttachment;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovTask;
import kz.ablaysharimov.taskmanagementsystem.entity.AblaySharimovUser;
import kz.ablaysharimov.taskmanagementsystem.exception.AblaySharimovFileStorageException;
import kz.ablaysharimov.taskmanagementsystem.exception.AblaySharimovResourceNotFoundException;
import kz.ablaysharimov.taskmanagementsystem.mapper.AblaySharimovAttachmentMapper;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovAttachmentRepository;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovTaskRepository;
import kz.ablaysharimov.taskmanagementsystem.repository.AblaySharimovUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AblaySharimovFileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final AblaySharimovAttachmentRepository attachmentRepository;
    private final AblaySharimovTaskRepository taskRepository;
    private final AblaySharimovUserRepository userRepository;
    private final AblaySharimovAttachmentMapper attachmentMapper;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_FILE_TYPES = {"pdf", "doc", "docx", "png", "jpg", "jpeg", "txt"};

    public AblaySharimovAttachmentResponse storeFile(MultipartFile file, Long taskId, Long uploadedById) {
        log.info("Storing file: {} for task: {}", file.getOriginalFilename(), taskId);

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AblaySharimovFileStorageException("File size exceeds maximum limit of 10MB");
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!isAllowedFileType(fileExtension)) {
            throw new AblaySharimovFileStorageException("File type not allowed: " + fileExtension);
        }

        AblaySharimovTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Task not found"));

        AblaySharimovUser uploadedBy = userRepository.findById(uploadedById)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("User not found"));

        try {
            String fileName = UUID.randomUUID() + "." + fileExtension;
            Path taskUploadDir = Paths.get(uploadDir).resolve(taskId.toString());
            Files.createDirectories(taskUploadDir);

            Path filePath = taskUploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            AblaySharimovAttachment attachment = AblaySharimovAttachment.builder()
                    .fileName(fileName)
                    .originalFileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .filePath(filePath.toString())
                    .task(task)
                    .uploadedBy(uploadedBy)
                    .build();

            AblaySharimovAttachment savedAttachment = attachmentRepository.save(attachment);
            log.info("File stored successfully with id: {}", savedAttachment.getId());
            return attachmentMapper.toResponse(savedAttachment);

        } catch (IOException ex) {
            throw new AblaySharimovFileStorageException("Failed to store file: " + ex.getMessage(), ex);
        }
    }

    @Transactional(readOnly = true)
    public Resource loadFileAsResource(Long attachmentId) {
        log.debug("Loading file: {}", attachmentId);

        AblaySharimovAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Attachment not found"));

        try {
            Path filePath = Paths.get(attachment.getFilePath()).toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new AblaySharimovFileStorageException("File not found or not readable");
            }
        } catch (MalformedURLException ex) {
            throw new AblaySharimovFileStorageException("Failed to load file: " + ex.getMessage(), ex);
        }
    }

    public void deleteFile(Long attachmentId) {
        log.info("Deleting file: {}", attachmentId);

        AblaySharimovAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new AblaySharimovResourceNotFoundException("Attachment not found"));

        try {
            Path filePath = Paths.get(attachment.getFilePath());
            Files.deleteIfExists(filePath);
            attachmentRepository.deleteById(attachmentId);
            log.info("File deleted successfully");
        } catch (IOException ex) {
            throw new AblaySharimovFileStorageException("Failed to delete file: " + ex.getMessage(), ex);
        }
    }

    @Transactional(readOnly = true)
    public List<AblaySharimovAttachmentResponse> getAttachmentsByTask(Long taskId) {
        log.debug("Getting attachments for task: {}", taskId);
        List<AblaySharimovAttachment> attachments = attachmentRepository.findByTaskId(taskId);
        List<AblaySharimovAttachmentResponse> responses = new ArrayList<>();
        for (AblaySharimovAttachment attachment : attachments) {
            responses.add(attachmentMapper.toResponse(attachment));
        }
        return responses;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) return "";
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1 || lastIndexOf == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1).toLowerCase();
    }

    private boolean isAllowedFileType(String fileExtension) {
        for (String allowedType : ALLOWED_FILE_TYPES) {
            if (allowedType.equalsIgnoreCase(fileExtension)) {
                return true;
            }
        }
        return false;
    }
}

