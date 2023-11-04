package ru.inock.telebot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inock.telebot.entity.BinaryContent;

public interface BinaryContentDAO extends JpaRepository<BinaryContent, Long> {
}
