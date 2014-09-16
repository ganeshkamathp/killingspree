package com.sillygames.killingSpree.networking;

import com.sillygames.killingSpree.controls.InputController;
import com.sillygames.killingSpree.networking.messages.ControlsMessage;
import com.sillygames.killingSpree.pool.MessageObjectPool;

public class ControlsSender {
    
    public ControlsMessage sendControls() {
        // left | right | up | down | jump | shoot
        ControlsMessage message = MessageObjectPool.instance.
                controlsMessagePool.obtain();
        message.buttonPresses = (byte) ((InputController.instance.buttonX() ? 1 : 0) |
                ((InputController.instance.buttonA()    ? 1 : 0) << 1) |
                ((InputController.instance.axisDown()   ? 1 : 0) << 2) |
                ((InputController.instance.axisUp()     ? 1 : 0) << 3) |
                ((InputController.instance.axisRight()  ? 1 : 0) << 4) |
                ((InputController.instance.axisLeft()   ? 1 : 0) << 5));
        return message;
    }

}
