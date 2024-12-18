package com.example.demo.Service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void init();

    void strore(MultipartFile file);

}
