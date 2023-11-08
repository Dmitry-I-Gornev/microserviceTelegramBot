package ru.inock.telebot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.inock.telebot.dao.AppUserDAO;
import ru.inock.telebot.dao.RawDataDAO;
import ru.inock.telebot.entity.AppDocument;
import ru.inock.telebot.entity.AppPhoto;
import ru.inock.telebot.entity.AppUser;
import ru.inock.telebot.entity.RawData;
import ru.inock.telebot.exceptions.UploadFileException;
import ru.inock.telebot.service.FileService;
import ru.inock.telebot.service.MainService;
import ru.inock.telebot.service.ProducerService;
import ru.inock.telebot.service.enums.ServiceCommands;

import static ru.inock.telebot.entity.enums.UserState.*;
import static ru.inock.telebot.service.enums.ServiceCommands.*;


@Service
@Slf4j
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;
    private final AppUserDAO appUserDAO;
    private final FileService fileService;

    public MainServiceImpl(RawDataDAO rawDataDAO,
                           ProducerService producerService,
                           AppUserDAO appUserDAO,
                           FileService fileService) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
        this.fileService = fileService;
    }

    @Override
    public void processTextMessage(Update update) {

        saveRowData(update);
        var textMessage = update.getMessage().getText();
        var telegramUser = update.getMessage().getFrom();
        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var output = "";

        var serviceCommand = ServiceCommands.fromValue(textMessage);
        if (CANCEL.equals(serviceCommand)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = producerServiceCommand(textMessage);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            // TODO добавить обработку e-mail
        } else {
            log.error("Неизвестный статус пользователя " + userState);
            output = "Ошибка времени исполнения. Введите /cancel и попробуйте снова!";
        }
        sendAnswer(output, update.getMessage().getChatId());

    }


    @Override
    public void processDocMessage(Update update) {
        saveRowData(update);
        var appUser = findOrSaveAppUser(update);
        var chatID = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatID, appUser)) {
            return;
        }
        //TODO Добавить сохранение докуменета (файла)
        try {
            AppDocument doc = fileService.processDoc(update.getMessage());
            //TODO Добавить генерацию ссылки для скачивания документа
            var answer = "Документ успешно загружен! "
                    + "Ссылка для скачивания: http://test.ru/get-doc/777";
            sendAnswer(answer, chatID);
        } catch (UploadFileException ex) {
            log.error(ex.toString());
            String error = "К сожалению, загрузка файла не удалась. Повторите попытку позже.";
            sendAnswer(error, chatID);
        }


    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRowData(update);
        var appUser = findOrSaveAppUser(update);
        var chatID = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatID, appUser)) {
            return;
        }
        //TODO Добавить сохранение фотографии
        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            //TODO добавить генерацию ссылки для скачивания фото
            var answer = "Фото успешно загружено! "
                    + "Ссылка для скачивания: http://test.ru/get-photo/777";
            sendAnswer(answer, chatID);
        } catch (UploadFileException ex) {
            log.error(String.valueOf(ex));
            String error = "К сожалению, загрузка фото не удалась. Повторите попытку позже.";
            sendAnswer(error, chatID);
        }
    }

    private void saveRowData(Update update) {
        RawData rawData = RawData.builder().event(update).build();
        rawDataDAO.save(rawData);
    }

    private AppUser findOrSaveAppUser(Update update) {
        var telegramUser = update.getMessage().getFrom();

        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserID(telegramUser.getId());
        if (persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserID(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    //TODO изменить значение по умолчанию после добавления регистрации
                    .isActive(true)
                    .state(BASIC_STATE)
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

        if (cmd.equals(REGISTRATION.toString())) {
            return "Команда временно не доступна";
            //TODO добавить регистрацию
        } else if (cmd.equals(HELP.toString())) {
            return help();
        } else if (cmd.equals(START.toString())) {
            return "Приветствую! Чтобы посмотреть список доступных команд введите /help";
        } else {
            return "Неизвестная команда! Чтобы посмотреть список доступных команд введите /help";
        }

    }

    private void sendAnswer(String output, Long chatID) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    private String help() {
        return "Список доступных команд:\n" +
                "/registration - регистрация\n" +
                "/cancel - отмена выполнения текущей команды";
    }

    private boolean isNotAllowToSendContent(Long chatID, AppUser appUser) {
        var userState = appUser.getState();
        if (!appUser.getIsActive()) {
            var msg = "Зарегистрируйтесь или подтвердите Вышу учетную запись для активации!";
            sendAnswer(msg, chatID);
            return true;
        } else if (!BASIC_STATE.equals(appUser.getState())) {
            var msg = "Некорректный формат данных. Введите команду /Cancel и попробуйте еще раз";
            sendAnswer(msg, chatID);
            return true;
        }
        return false;
    }
}


