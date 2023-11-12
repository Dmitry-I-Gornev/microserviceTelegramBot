package ru.inock.telebot.service.enums;

public enum LinkType {
    GET_DOC("/getFile/get-doc"),
    GET_PHOTO("/getFile/get-photo");
    private final String link;

    LinkType(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return link;
    }
}
