package com.hack.a.drone.io.controls;

import com.hack.a.drone.model.Command;

import java.awt.*;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.*;

public class Keyboard implements IController, KeyEventDispatcher {
    private final KeyboardFocusManager focusManager;
    private CommandListener commandListener;
    private Command command = new Command();

    public Keyboard(KeyboardFocusManager focusManager) {
        this.focusManager = focusManager;
    }

    public void start() {
        focusManager.addKeyEventDispatcher(this);
    }

    public void stop() {
        focusManager.removeKeyEventDispatcher(this);
    }

    public boolean isAvailable() {
        return false;
    }

    public void setListener(CommandListener controlListener) {
        this.commandListener = controlListener;
    }

    private void onKeyEvent(KeyEvent e, boolean isPressed) {
        int value = isPressed ? 127 : 0;
        boolean newInput = true;
//         TODO implement
        switch (e.getKeyCode()) {
            case VK_W: // TODO implement (UP)
                command.setThrottle(value);
                break;
            case VK_Z: // TODO implement (DOWN)
            case VK_X:    
                command.setThrottle(-value);
                break; 
            case VK_D: // TODO implement (TURN RIGHT)
                command.setYaw(value);
                break;
            case VK_A: // TODO implement (TURN LEFT)
                command.setYaw(-value);
                break;
            case VK_UP: // TODO implement (FORWARD)
                command.setPitch(value);
                break;
            case VK_DOWN:// TODO implement (BACKWARD)
                command.setPitch(-value);
                break;
            case VK_RIGHT:// TODO implement (LEAN RIGHT)
                command.setRoll(value);
                break;
            case VK_LEFT:// TODO implement (LEAN LEFT)
                command.setRoll(-value);
                break;
            case VK_SPACE:// TODO implement
                command.setTakeOff(isPressed);
                break;
            case VK_S:// TODO implement
                command.setLand(isPressed);
                break;
            default:
                newInput = false;
        }

        if (commandListener != null && newInput) {
            commandListener.onCommandReceived(command);
        }
        e.consume();
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KEY_PRESSED) {
            onKeyEvent(e, true);
        } else if (e.getID() == KEY_RELEASED) {
            onKeyEvent(e, false);
        }
        return false;
    }
}
