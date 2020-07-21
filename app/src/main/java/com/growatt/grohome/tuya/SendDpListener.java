package com.growatt.grohome.tuya;

public interface SendDpListener {
    void sendCommandSucces();

    void sendCommandError(String code, String error);
}
