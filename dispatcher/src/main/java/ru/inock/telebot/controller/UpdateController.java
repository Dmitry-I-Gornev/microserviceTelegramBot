package ru.inock.telebot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.inock.telebot.config.TelegramBotConfig;
import ru.inock.telebot.service.UpdateProducer;
import ru.inock.telebot.utils.MessageUtil;
import static ru.inock.telebot.model.RabbitQueue.*;

@Component
@Scope("singleton")
@Slf4j
public class UpdateController {
    @Autowired
    TelegramBotConfig config;
    private  TelegramBot telegramBot;
    private MessageUtil messageUtil;
    UpdateProducer updateProducer;

    public UpdateController(MessageUtil messageUtil, UpdateProducer updateProducer) {
        this.messageUtil = messageUtil;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot bot){
        this.telegramBot=bot;
    }

    public void processUpdate(Update update){
        if (update==null){
          log.error(this.getClass().getName() + ": в метод processUpdate передан null");
          return;
        }

        if(update.hasMessage()){
            distributeMessagesByType(update);
        }else{
            log.error(this.getClass().getName() + ": обаружен не поддерживаемый тип сообщения " + update);
        }
    }

    private void distributeMessagesByType(Update update) {
        var message = update.getMessage();
        if(message.hasText()){
            processTextMessage(update);
        }else if (message.hasDocument()){
            processDocMessage(update);
        }else if(message.hasPhoto()){
            processPhotoMessage(update);
        }else{
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var text = "Неподдерживаемый тип сообщения!";
        var sendMessage = messageUtil.generateSendMessageWithText(update, text);
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE,update);
        // log.info("Получена фотография");
        setFileIsReceivedView(update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE,update);
        // log.info("Получен файл");
        setFileIsReceivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE,update);
        /* System.out.println("Сообщение от пользователя @" + update.getMessage().getChat().toString());
        * // @Chat(id=1143035268, type=private, title=null, firstName=Dmitry (Inock), lastName=Gornev, userName=inock, photo=null, description=null, inviteLink=null, pinnedMessage=null, stickerSetName=null, canSetStickerSet=null, permissions=null, slowModeDelay=null, bio=null, linkedChatId=null, location=null, messageAutoDeleteTime=null, hasPrivateForwards=null, HasProtectedContent=null, joinToSendMessages=null, joinByRequest=null, hasRestrictedVoiceAndVideoMessages=null, isForum=null, activeUsernames=null, emojiStatusCustomEmojiId=null, hasAggressiveAntiSpamEnabled=null, hasHiddenMembers=null)
        */

        /*var text = "Сообщение от пользователя @" + update.getMessage().getChat().getUserName() +
                ": \"" + update.getMessage().getText() + "\"";
        sendMessageToAdmin(text);*/
    }
    private void setFileIsReceivedView(Update update) {
        var text = "Идет обработка полученного файла. Ожидайте.";
        var sendMessage = messageUtil.generateSendMessageWithText(update, text);
        setView(sendMessage);
    }

    private void sendMessageToAdmin(String texMessage) {
        SendMessage message = new SendMessage();
        message.setChatId(config.getAdminChat());
        message.setText(texMessage);
        telegramBot.sendAnswerMessage(message);
    }
}
