package com.example.cmsc335_project3;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

public class Car implements Runnable {
    SimpleIntegerProperty speed = new SimpleIntegerProperty(25); // meters/sec
    SimpleIntegerProperty xPos = new SimpleIntegerProperty(0);

    SimpleBooleanProperty paused = new SimpleBooleanProperty(false);
    List<TrafficLight> trafficLights;

    public Car(List<TrafficLight> t) {
        this.trafficLights = t;
    }

    @Override
    public void run() {
        while (!paused.get()) {
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

    private void checkLight(SimpleIntegerProperty pos) {
        synchronized (this) {
            if (pos.get() % 1000 == 0) {
                if (trafficLights.get(1 + (pos.get()) / 1000).ltCol.getColor().equals(LightColor.RED)) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
