package ru.inock.telebot.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.inock.telebot.config.TelegramBotConfig;


@Component("telegramBot")
@PropertySource("classpath:application.properties")
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    TelegramBotConfig config;
    @Autowired
    private UpdateController updateController;

    public TelegramBot(@Value("${bot.token}") String botToken) {
        super(botToken);
        /*System.out.println("initialising telegramBot:\n"+
                "token = " + botToken);*/
    }

    /*
    Overwriting the getBotToken() method is deprecated. Use the constructor instead
    Переопределение метода getBotToken() устарело. Вместо этого используется конструктор
    @Override
    public String getBotToken() {
        System.out.println("run getBotToken method");
        //log.info("run getBotToken method");
        return config.getBotToken();
    }*/


    @Override
    public String getBotUsername() {
        // System.out.println("run getBotUsername method\n"+
        //         "botName = " + config.getBotName());
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    public void sendAnswerMessage(SendMessage sendMessage){
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            String errText = "Отправка сообщения не удалась! \n" + e.getMessage();
            log.error(errText);
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

}
