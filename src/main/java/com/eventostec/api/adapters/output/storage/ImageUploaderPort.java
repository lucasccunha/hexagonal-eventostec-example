package com.eventostec.api.adapters.output.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploaderPort {
    String uploadImage(MultipartFile file);
}
