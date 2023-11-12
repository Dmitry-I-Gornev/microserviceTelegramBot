package ru.inock.telebot;

import org.hashids.Hashids;

public class CryptoTool {
    private final Hashids hashids;

    public CryptoTool(String salt) {
        var minHashLenght = 25;
        this.hashids = new Hashids(salt, minHashLenght);
    }
    public String hashOf(Long value) {
        return hashids.encode(value);
    }

    public Long idOf(String hash){
        var res = hashids.decode(hash);
        if(res != null && res.length>=1){
            return res[0];
        }
        return null;
    }
}
