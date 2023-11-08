package ru.inock.telebot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inock.telebot.entity.AppPhoto;

public interface AppPhotoDAO extends JpaRepository<AppPhoto, Long> {
}
