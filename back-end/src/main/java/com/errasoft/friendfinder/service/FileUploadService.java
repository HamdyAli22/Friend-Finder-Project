package com.errasoft.friendfinder.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String uploadFile(MultipartFile file, String folder) throws IOException;
}
