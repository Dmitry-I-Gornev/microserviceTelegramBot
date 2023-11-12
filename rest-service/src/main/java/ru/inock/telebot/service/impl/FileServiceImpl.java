package ru.inock.telebot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import ru.inock.telebot.CryptoTool;
import ru.inock.telebot.dao.AppDocumentDAO;
import ru.inock.telebot.dao.AppPhotoDAO;
import ru.inock.telebot.entity.AppDocument;
import ru.inock.telebot.entity.AppPhoto;
import ru.inock.telebot.entity.BinaryContent;
import ru.inock.telebot.service.FileService;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private final AppDocumentDAO appDocumentDAO;
    private final AppPhotoDAO appPhotoDAO;
    @Autowired
    CryptoTool cryptoTool;

    public FileServiceImpl(AppDocumentDAO appDocumentDAO, AppPhotoDAO appPhotoDAO) {
        this.appDocumentDAO = appDocumentDAO;
        this.appPhotoDAO = appPhotoDAO;
    }


    @Override
    public AppDocument getDocument(String docId){

        var id = cryptoTool.idOf(docId);
        return appDocumentDAO.findById(id).orElse(null);
    };

    @Override
    public AppPhoto getPhoto(String idPhoto){
        System.out.println("idPhoto: " + idPhoto);
        var id = cryptoTool.idOf(idPhoto);
        System.out.println("id: " + id);
        return appPhotoDAO.findById(id).orElse(null);

    };

    @Override
    public FileSystemResource getFileSystemResourcee(BinaryContent binaryContent){
        try {
            File temp= File.createTempFile("tempFile",".bin");
            temp.deleteOnExit();
            FileUtils.writeByteArrayToFile(temp,binaryContent.getFileAsArrayOfBytes());
            return new FileSystemResource(temp);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    };
}
