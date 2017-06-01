package com.hack.a.drone.io.controls;

import com.hack.a.drone.model.Command;

public interface IController {

    void start();

    void stop();

    boolean isAvailable();

    void setListener(CommandListener controlListener);

    interface CommandListener{
        void onCommandReceived(Command command);
    }

}
