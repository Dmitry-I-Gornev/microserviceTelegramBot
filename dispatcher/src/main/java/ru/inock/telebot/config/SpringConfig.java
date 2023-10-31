package ru.inock.telebot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.inock.telebot.controller.TelegramBot;

@Configuration
@ComponentScan("ru.inock.telebot")
public class SpringConfig {

}
