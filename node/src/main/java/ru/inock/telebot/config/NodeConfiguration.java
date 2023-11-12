package ru.inock.telebot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.inock.telebot.CryptoTool;

@Configuration
public class NodeConfiguration {
    @Value("${hash.salt}")
    private String solt;

    @Bean
    public CryptoTool getCryptoTool() {
        return new CryptoTool(solt);
    }
}
