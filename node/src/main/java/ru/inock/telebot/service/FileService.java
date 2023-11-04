package ru.inock.telebot.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.inock.telebot.entity.AppDocument;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
}
