package ru.inock.telebot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inock.telebot.entity.RawData;

public interface RawDataDAO extends JpaRepository<RawData, Long> {

}
