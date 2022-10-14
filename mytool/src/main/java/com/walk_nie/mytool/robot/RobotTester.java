package com.walk_nie.mytool.robot;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class RobotTester {

    public static void main(String[] args) throws AWTException {
//        int x1=0,y1=0;
//        while(true){
//            PointerInfo a = MouseInfo.getPointerInfo();
//            int x = (int)a.getLocation().getX();
//            int y = (int)a.getLocation().getY();
//            if(x1!=x&&y1!=y){
//            System.out.println("[X]"+x+"[Y]"+y);
//            x1 = x;y1=y;
//            }
//        }
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(3000);
        robot.mouseMove(10, 90);
        robot.keyPress(KeyEvent.VK_CONTROL);
        //robot.keyPress(KeyEvent.VK_0);
        //robot.keyRelease(KeyEvent.VK_0);
        robot.mouseMove(90, 90);
        //robot.keyPress(KeyEvent.VK_0);
        //robot.keyRelease(KeyEvent.VK_0);
        
        
        
    }

}
