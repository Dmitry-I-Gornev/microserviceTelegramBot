package ru.inock.telebot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.inock.telebot.service.ConsumerService;
import ru.inock.telebot.service.ProducerService;
//import ru.inock.telebot.service.MainService;
import static ru.inock.telebot.model.RabbitQueue.*;

//NODE

@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {
    private final ProducerService producerService;
    //private final MainService mainService;

    /*public ConsumerServiceImpl(MainService mainService) {
        this.mainService = mainService;
    }*/
    public ConsumerServiceImpl(ProducerService producerService){
        this.producerService = producerService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        log.info("NODE: Text message is received");
        //mainService.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdates(Update update) {
	log.info("NODE: Doc message is received");
	//mainService.processDocMessage(update);
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdates(Update update) {
	log.info("NODE: Photo message is received");
        //mainService.processPhotoMessage(update);
    }
}
