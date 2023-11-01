package ru.inock.telebot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.inock.telebot.dao.RawDataDAO;
import ru.inock.telebot.entity.RawData;
import ru.inock.telebot.service.MainService;
import ru.inock.telebot.service.ProducerService;

/*
import ru.inock.telebot.entity.enums.UserState;
import ru.inock.telebot.entity.enums.UserState;
import ru.inock.telebot.entity.AppUser;
import ru.inock.telebot.dao.AppUserDAO;
import static ru.inock.service.enums.ServiceCommands.*;
import static ru.inock.entity.enums.UserState.*;*/

@Service
@Slf4j
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;

    public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {

        saveRowData(update);
        log.info("MainService(Node): Text message is received");
        /*
        var appUser = findOrSaveArrUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var output = "";
        var chatID = update.getMessage().getChatId();

        if (CANCEL.equals(text)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = producerServiceCommand(text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            // TODO добавить обработку e-mail
        } else {
            log.error("Неизвестный статус пользователя " + userState);
            output = "Ошибка времени исполнения. Введите /cancel и попробуйте снова!";
        }

        sendAnswer(output, chatID);*/
    }

    private void saveRowData(Update update) {
        RawData rawData = RawData.builder().event(update).build();
        rawDataDAO.save(rawData);

    }

    @Override
    public void processDocMessage(Update update) {

    }

    @Override
    public void processPhotoMessage(Update update) {

    }


   /*
    private final AppUserDAO appUserDAO;*/

/*    public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService, AppUserDAO appUserDAO) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
    }



    @Override
    public void processDocMessage(Update update) {
        saveRowData(update);
        var appUser = findOrSaveArrUser(update);
        var chatID = update.getMessage().getChatId();
        if(!isAllowToSendContent(chatID,appUser)){
            return;
        }
        //TODO Добавить сохранение файла
        var output = "Файл успешно загружен! Ссылка для скачивания: https://test.ru/get-doc/????";
        sendAnswer(output, chatID);

    }



    @Override
    public void processPhotoMessage(Update update) {
        saveRowData(update);
        var appUser = findOrSaveArrUser(update);
        var chatID = update.getMessage().getChatId();
        if(!isAllowToSendContent(chatID,appUser)){
            return;
        }
        //TODO Добавить сохранение файла
        var output = "Фотография успешно загружена! Ссылка для скачивания: https://test.ru/get-photo/????";
        sendAnswer(output, chatID);


    }
    private boolean isAllowToSendContent(Long chatID, AppUser appUser) {
        var error = "Вы не зарегистрированы в системе. Пожалуйста, зарегистрируйтесь или подтвердите свой e-mail";
        if(!appUser.isActive()){
            return false;
        }
        if(!BASIC_STATE.equals(appUser.getState())){
        return false;
        }
        return true;
    }

    private void sendAnswer(String output, Long chatID) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }


    private void saveRowData(Update update) {
        RawData rawData = RawData.builder().event(update).build();
        rawDataDAO.save(rawData);
    }

    private AppUser findOrSaveArrUser(Update update) {
        var telegramUser = update.getMessage().getFrom();
        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserID(telegramUser.getId());
        if (persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserID(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO изменить значение по умолчанию после добавления регистрации
                    .active(true)
                    .state(UserState.BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return persistentAppUser;
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Команда отменена";
    }

    private String producerServiceCommand(String cmd) {
        cmd = cmd.toLowerCase();

        if(cmd.equals(REGISTRATION.toString())){
            return "Команда временно не доступна";
            //TODO добавить регистрацию
        }else if(cmd.equals(HELP.toString())){
            return help();
        }else if(cmd.equals(START.toString())){
            return "Приветствую! Чтобы посмотреть список доступных команд введите /help";
        }else{
            return "Неизвестная команда! Чтобы посмотреть список доступных команд введите /help";
        }
    }

    private String help() {
        return "Список доступных команд:\n" +
                "/registration - регистрация\n" +
                "/cancel - отмена выполнения текущей команды";
    }*/
}
