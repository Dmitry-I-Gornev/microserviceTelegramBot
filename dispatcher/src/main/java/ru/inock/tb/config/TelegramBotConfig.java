package ru.inock.tb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.inock.tb.controller.UpdateController;

@Component
@PropertySource("classpath:application.properties")
public class TelegramBotConfig {
    public TelegramBotConfig() {
    }
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.name}")
    private String botName;

    @Value("${bot.admin.chat}")
    private Long adminChat;


    public String getBotToken() {
        return botToken;
    }

    public String getBotName() {
        return botName;
    }

    public Long getAdminChat() {
        return adminChat;
    }
}
