package ru.inock.telebot.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.inock.telebot.entity.AppDocument;
import ru.inock.telebot.entity.AppPhoto;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
}
