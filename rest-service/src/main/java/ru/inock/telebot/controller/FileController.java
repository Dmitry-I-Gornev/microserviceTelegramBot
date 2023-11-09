package ru.inock.telebot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.inock.telebot.service.FileService;

@RestController
@RequestMapping("/getFile")
@Slf4j
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
@RequestMapping(method = RequestMethod.GET, value = "get-doc")
    public ResponseEntity<?> getDoc(@RequestParam("id") String id) {
        var doc = fileService.getDocument(id);
        if (doc == null) {
            return ResponseEntity.badRequest().build();
        }
        var binareContent = doc.getBinaryContent();
        var fileSystemResource = fileService.getFileSystemResourcee(binareContent);
        if(fileSystemResource==null){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                .header("Content-disposition","attachment; filename=" + doc.getDocName())
                .body(fileSystemResource);
    }

    @RequestMapping(method = RequestMethod.GET, value = "get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        var photo = fileService.getPhoto(id);
        if (photo == null) {
            return ResponseEntity.badRequest().build();
        }
        var binareContent = photo.getBinaryContent();
        var fileSystemResource = fileService.getFileSystemResourcee(binareContent);
        if(fileSystemResource==null){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                //.header("Content-disposition","attachment;")
                .body(fileSystemResource);
    }
}
