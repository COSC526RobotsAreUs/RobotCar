package com.example.will.robotcar;

import java.io.OutputStream;

/**
 * Created by Will on 12/14/15.
 */
public class MotorMover {
    private OutputStream os;
    private int power = 60;
    private int turnPower = 30;
    private MotorMover() { }
    public MotorMover(OutputStream os){
        this.os = os;
    }

    public void moveForward(){
        this.moveMotor(0, power, 0x20);
        this.moveMotor(1, power, 0x20);
    }
    public void moveLeft(){
        moveMotor(0, turnPower, 0x20);
        moveMotor(1, -turnPower, 0x20);
    }

    public void moveRight() {
        moveMotor(0, -turnPower, 0x20);
        moveMotor(1, turnPower, 0x20);
    }

    public void moveBack(){
        moveMotor(0, -power, 0x20);
        moveMotor(1, -power, 0x20);
    }

    public void stop(){
        moveMotor(0, power, 0x00);
        moveMotor(1, power, 0x00);
    }

    private void moveMotor(int motor,int speed, int state) {
        try {
            byte[] buffer = new byte[15];

            buffer[0] = (byte) (15-2);			//length lsb
            buffer[1] = 0;						// length msb
            buffer[2] =  0;						// direct command (with response)
            buffer[3] = 0x04;					// set output state
            buffer[4] = (byte) motor;			// output 1 (motor B)
            buffer[5] = (byte) speed;			// power
            buffer[6] = 1 + 2;					// motor on + brake between PWM
            buffer[7] = 0;						// regulation
            buffer[8] = 0;						// turn ration??
            buffer[9] = (byte) state; //0x20;					// run state
            buffer[10] = 0;
            buffer[11] = 0;
            buffer[12] = 0;
            buffer[13] = 0;
            buffer[14] = 0;

            if(os == null){
                os = MainActivity.socket.getOutputStream();
            }
            os.write(buffer);
            os.flush();
        }
        catch (Exception e) {
        }
    }
}
