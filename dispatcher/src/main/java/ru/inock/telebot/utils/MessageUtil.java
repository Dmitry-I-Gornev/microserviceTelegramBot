package ru.inock.telebot.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
@Scope("singleton")
public class MessageUtil {
    public SendMessage generateSendMessageWithText(Update update, String text){
       var sendMessage = new SendMessage();
       sendMessage.setChatId(update.getMessage().getChatId().toString());
       sendMessage.setText(text);
       return sendMessage;
    }


}
