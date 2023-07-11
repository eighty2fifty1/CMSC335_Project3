package com.example.cmsc335_project3;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;

import java.util.Observable;
import java.util.concurrent.locks.ReentrantLock;

public class MyTimer extends Observable implements Runnable {
    final ReentrantLock lock = new ReentrantLock();
    SimpleIntegerProperty elapsedTime = new SimpleIntegerProperty(0);
    SimpleBooleanProperty running = new SimpleBooleanProperty(false);
    SimpleBooleanProperty paused = new SimpleBooleanProperty(false);

    @Override
    public void run() {
        //elapsedTimeLabel.textProperty().bind(this.elapsedTime.asString());

        System.out.println("run called");
        running.set(true);
        paused.set(false);
        while (running.get()) {
            try {
                //running.set(true);
                Thread.sleep(1000);

                if (!paused.get()) {
                    Platform.runLater(()->{
                    elapsedTime.set(elapsedTime.get() + 1);
                    });
                    System.out.println(elapsedTime.getValue());
                }
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    public void setRunning(boolean b) {
        synchronized (lock) {
            System.out.println("setrunning called");
            running.set(b);
        }
    }

    public void pause() {
        synchronized (lock) {
            paused.set(!paused.get());
            System.out.println(paused);
        }
    }

    /*
        synchronized SimpleIntegerProperty getElapsedTime() {
            return elapsedTime;
        }


     */
    synchronized ObservableValue<? extends String> getElapsedTime() {
        return elapsedTime.asString();
    }
}
