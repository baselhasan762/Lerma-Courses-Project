package com.tenmacourses.tenmacourses.Controller;


import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    @PostMapping("/upload")
    public ResponseEntity<String> handleUpload(@RequestParam("file")MultipartFile file){
        try{
            String fileName = file.getOriginalFilename();
            Path path = Path.of("uploads/" +fileName);
            Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("Uploaded:"+fileName);

        }
        catch (IOException e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }
}
