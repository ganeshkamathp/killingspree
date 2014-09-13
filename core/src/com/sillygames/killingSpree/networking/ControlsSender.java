package com.sillygames.killingSpree.networking;

import com.sillygames.killingSpree.controls.InputController;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.pool.MessageObjectPool;

public class ControlsSender {
    
    public ControlsMessage sendControls() {
        ControlsMessage message = MessageObjectPool.instance.
                controlsMessagePool.obtain();
        message.direction = 0;
        message.action = 0;
        if (InputController.instance.axisRight()) {
            message.direction = 3;
        } else if (InputController.instance.axisLeft()) {
            message.direction = 7;
        }
        if (InputController.instance.buttonA()) {
            message.action = 2;
        }
        if (InputController.instance.buttonX()) {
            message.action += 1;
        }
        
        return message;
    }

}
