package ru.inock.tb.service.impl;

import lombok.extern.slf4j.Slf4j;
//import org.jvnet.hk2.annotations.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.inock.tb.controller.UpdateController;
import ru.inock.tb.service.AnswerConsumer;

import static ru.inock.robbitmq.model.RabbitQueue.ANSWER_MESSAGE;

@Service
@Slf4j
public class AnswerConsumerImpl implements AnswerConsumer {
    private final UpdateController updateController;

    public AnswerConsumerImpl(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateController.setView(sendMessage);

    }
}
