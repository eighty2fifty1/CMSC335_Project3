package com.example.cmsc335_project3;


import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.concurrent.locks.ReentrantLock;

public class TrafficLight implements Runnable {
    final ReentrantLock lock = new ReentrantLock();

    LightColorProperty ltCol = new LightColorProperty(LightColor.RED);

    SimpleBooleanProperty paused = new SimpleBooleanProperty(false);
    SimpleBooleanProperty running = new SimpleBooleanProperty(true);


    int posit;

    TrafficLight(int p) {
        //ltCol.setColor(LightColor.RED);
        posit = p;
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            if (!paused.get()) {
                try {
                    switch (ltCol.getColor()) {
                        case RED: {
                            Thread.sleep(10000);
                            Platform.runLater(() -> ltCol.setColor(LightColor.GREEN));
                            break;
                        }
                        case GREEN: {
                            Thread.sleep(12000);
                            Platform.runLater(() -> ltCol.setColor(LightColor.YELLOW));
                            break;
                        }
                        case YELLOW: {
                            Thread.sleep(3000);
                            Platform.runLater(() -> ltCol.setColor(LightColor.RED));
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ;
            }
        }
        System.out.println("light thread ended");
    }


    public void pause() {
        synchronized (lock) {
            paused.set(!paused.get());
            System.out.println(paused);
        }
    }
}
