package com.example.cmsc335_project3;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Car implements Runnable {
    SimpleIntegerProperty speed = new SimpleIntegerProperty(25); // meters/sec
    SimpleIntegerProperty xPos = new SimpleIntegerProperty(0);
    final ReentrantLock lock = new ReentrantLock();


    SimpleBooleanProperty paused = new SimpleBooleanProperty(false);
    List<TrafficLight> trafficLights;

    SimpleBooleanProperty running = new SimpleBooleanProperty(true);

    public Car(List<TrafficLight> t) {
        this.trafficLights = t;
    }

    public Car(int x, List<TrafficLight> t) {
        this.xPos.set(x);
        this.trafficLights = t;
    }

    @Override
    public void run() {
        while (running.get()) {
            if (!paused.get()) {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        xPos.set(xPos.get() + speed.get());
                        checkLight(xPos);
                    });
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        System.out.println("car thread ended");
    }

    private void checkLight(SimpleIntegerProperty pos) {
            if (pos.get() % 1000 == 0) {
                try {
                    if (trafficLights.get(((pos.get()) / 1000)).ltCol.getColor() == LightColor.RED) {
                        speed.set(0);
                    } else {
                        speed.set(25);
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("car exited");
                    speed.set(0);
                    running.set(false);    //car has exited the section of road
                }
            }

    }


    public void pause() {
        synchronized (lock) {
            paused.set(!paused.get());
            System.out.println(paused);
        }
    }
}
