package ru.inock.telebot.service;

import org.springframework.core.io.FileSystemResource;
import ru.inock.telebot.entity.AppDocument;
import ru.inock.telebot.entity.AppPhoto;
import ru.inock.telebot.entity.BinaryContent;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
    FileSystemResource getFileSystemResourcee(BinaryContent binaryContent);
}
