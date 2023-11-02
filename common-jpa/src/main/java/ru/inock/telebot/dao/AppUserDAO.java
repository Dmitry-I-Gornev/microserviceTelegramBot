package ru.inock.telebot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inock.telebot.entity.AppUser;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByTelegramUserID(Long id);
}
