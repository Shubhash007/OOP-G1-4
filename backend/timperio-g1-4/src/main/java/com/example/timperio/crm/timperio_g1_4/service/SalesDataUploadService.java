package com.example.timperio.crm.timperio_g1_4.service;

import org.springframework.web.multipart.MultipartFile;

public interface SalesDataUploadService {
    void parseSalesData(MultipartFile file) throws Exception;
}
