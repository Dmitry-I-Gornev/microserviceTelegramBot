package ru.inock.telebot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.inock.telebot.CryptoTool;

@Configuration
public class RestConfiguration {
    @Value("${hash.salt}")
    private String solt;

    @Bean
    public CryptoTool getCryptoTool() {
        return new CryptoTool(solt);
    }
}
