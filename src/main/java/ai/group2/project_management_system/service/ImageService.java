package ai.group2.project_management_system.service;
import org.apache.commons.io.FilenameUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    @Value("${upload.directory}")
    private String uploadDir;

    @Async
    public String saveImageAsync(MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                String fileExtension = FilenameUtils.getExtension(originalFileName);

                String image = System.currentTimeMillis() + "." + fileExtension;
                Path path = Paths.get(uploadDir, image);

                // Use file.getBytes() to obtain the content of the file
                byte[] fileBytes = file.getBytes();

                // Create the directory if it doesn't exist
                Files.createDirectories(path.getParent());

                Files.write(path, fileBytes);

                return image;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }





}