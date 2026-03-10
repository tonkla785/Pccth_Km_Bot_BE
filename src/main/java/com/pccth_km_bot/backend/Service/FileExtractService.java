package com.pccth_km_bot.backend.Service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileExtractService {

    public String extractText(MultipartFile file) throws Exception {
        try {
            Tika tika = new Tika();

            return tika.parseToString(file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
