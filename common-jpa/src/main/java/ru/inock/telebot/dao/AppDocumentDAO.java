package ru.inock.telebot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inock.telebot.entity.AppDocument;

public interface AppDocumentDAO extends JpaRepository<AppDocument, Long> {
}
