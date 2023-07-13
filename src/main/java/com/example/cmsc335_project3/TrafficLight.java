package com.example.cmsc335_project3;


import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.concurrent.locks.ReentrantLock;

public class TrafficLight implements Runnable {
    final ReentrantLock lock = new ReentrantLock();

    LightColorProperty ltCol = new LightColorProperty(LightColor.RED);

    SimpleBooleanProperty paused = new SimpleBooleanProperty(false);
    boolean running = true;

    boolean changed = false;

    int posit;


    TrafficLight() {
        ltCol.setColor(LightColor.RED);
    }

    TrafficLight(int p) {
        ltCol.setColor(LightColor.RED);
        posit = p;
    }

    @Override
    public void run() {
        while (running) {
            while (!paused.get()) {
                try {
                    switch (ltCol.getColor()) {
                        case RED: {
                            Thread.sleep(10000);
                            break;
                        }
                        case GREEN: {
                            Thread.sleep(12000);
                            break;
                        }
                        case YELLOW: {
                            Thread.sleep(3000);
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                changeColor();
            }
        }
    }

    synchronized void changeColor() {
        Platform.runLater(() -> {
            if (!paused.get()) {
                switch (ltCol.getColor()) {
                    case YELLOW: {
                        ltCol.setColor(LightColor.RED);
                        break;
                    }
                    case RED: {
                        ltCol.setColor(LightColor.GREEN);
                        break;
                    }
                    case GREEN: {
                        ltCol.setColor(LightColor.YELLOW);
                        break;
                    }
                }
            }
        });

        changed = true;
        notify();
    }

    synchronized  void setRunning(boolean b){
        running = b;
    }
    synchronized void waitForChange() {
        try {
            while (!changed) {
                wait();
                changed = false;
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
    public void pause() {
        synchronized (lock) {
            paused.set(!paused.get());
            System.out.println(paused);
        }
    }
}
