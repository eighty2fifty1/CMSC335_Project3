package com.example.cmsc335_project3;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class TrafficController {
    @FXML
    public Label elapsedTimeLabel, lt1Color, lt2Color, lt3Color, car1Spd, car2Spd, car3Spd, car1Pos, car2Pos, car3Pos;
    @FXML
    public GridPane gridPane;
    MyTimer timer;

    int lightCount = 3, carCount = 3;
    List<Thread> threads = new ArrayList<>();
    List<TrafficLight> lights = new ArrayList<>();
    List<Car> cars = new ArrayList<>();
    TrafficLight light1, light2, light3;

    Car car1, car2, car3;

    @FXML
    protected void onStartButtonClick() {
        if (threads.get(0).isAlive()) {
            pauseResumeAll();
            //if (paused) paused = false;
        }
        //System.out.println("Start");
        //paused = false;
        for (Thread t : threads) {
            if (!t.isAlive()) {
                t.start();
            }
        }
    }

    @FXML
    public void onPauseButtonClick() {
        if (threads.get(0).isAlive()) pauseResumeAll();
        //paused = false;
    }

    @FXML
    public void onStopButtonClick() {
System.out.println("stop");
        try {
            timer.running.set(false);
            for (TrafficLight l : lights) {
                l.running.set(false);
            }
            for (Car c : cars) {
                c.running.set(false);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        resetThreads();
    }


    private void resetThreads() {
        timer = new MyTimer();
        //timerThread = new Thread(timer);
        light1 = new TrafficLight(1000);
        light2 = new TrafficLight(2000);
        light3 = new TrafficLight(3000);
        lights.add(light1);
        lights.add(light2);
        lights.add(light3);
        car1 = new Car(lights);
        car2 = new Car(975, lights);
        car3 = new Car(1000, lights);
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);

        threads.add(new Thread(timer));
        threads.add(new Thread(light1));
        threads.add(new Thread(light2));
        threads.add(new Thread(light3));
        threads.add(new Thread(car1));
        threads.add(new Thread(car2));
        threads.add(new Thread(car3));
    }

    @FXML
    public void initialize() {
        resetThreads();
        setBindings();
    }

    @FXML
    public void setBindings() {
        elapsedTimeLabel.textProperty().bind(timer.elapsedTime.asString());
        lt1Color.textProperty().bind(light1.ltCol.colorProperty().asString());
        lt2Color.textProperty().bind(light2.ltCol.colorProperty().asString());
        lt3Color.textProperty().bind(light3.ltCol.colorProperty().asString());
        car1Spd.textProperty().bind(car1.speed.asString());
        car2Spd.textProperty().bind(car2.speed.asString());
        car3Spd.textProperty().bind(car3.speed.asString());
        car1Pos.textProperty().bind(car1.xPos.asString());
        car2Pos.textProperty().bind(car2.xPos.asString());
        car3Pos.textProperty().bind(car3.xPos.asString());
        //conditional formatting using css
        lt1Color.styleProperty().bind(Bindings.createStringBinding(() ->
                "-fx-text-fill: " + getColor(light1), light1.ltCol.colorProperty()
        ));
        lt2Color.styleProperty().bind(Bindings.createStringBinding(() ->
                "-fx-text-fill: " + getColor(light2), light2.ltCol.colorProperty()
        ));
        lt3Color.styleProperty().bind(Bindings.createStringBinding(() ->
                "-fx-text-fill: " + getColor(light3), light3.ltCol.colorProperty()
        ));


    }

    private String getColor(TrafficLight t) {
        LightColor l = t.ltCol.getColor();
        if (l == LightColor.RED) {
            return "red";
        } else if (l == LightColor.YELLOW) {
            return "yellow";
        } else return "green";

    }

    private void addLightRow(TrafficLight lt) {
        gridPane.add(new Label(String.valueOf(lightCount)), 5, lightCount); //number
        //Label l = new Label(String.valueOf(lt.posit));
        gridPane.add(new Label(String.valueOf(lt.posit)), 6, lightCount); //position
        Label l = new Label();
        l.textProperty().bind(lt.ltCol.colorProperty().asString());
        l.styleProperty().bind(Bindings.createStringBinding(() ->
                "-fx-text-fill: " + getColor(lt), lt.ltCol.colorProperty()
        ));
        gridPane.add(l, 7, lightCount); //color
    }

    private void addCarRow(Car car) {
        gridPane.add(new Label(String.valueOf(carCount)), 0, carCount);
        Label l = new Label();
        l.textProperty().bind(car.xPos.asString());
        gridPane.add(l, 1, carCount);
        gridPane.add(new Label("0"), 2, carCount);
        l = new Label();
        l.textProperty().bind(car.speed.asString());
        gridPane.add(l, 3, carCount);
    }

    public void onAddLightButtonClicked() {
        if (lightCount < 5) {
            lightCount += 1;
            TrafficLight lt = new TrafficLight(lightCount * 1000);
            lights.add(lt);
            Thread t = new Thread(lt);
            if (threads.get(0).isAlive()) {
                t.start();
            }
            threads.add(t);
            addLightRow(lt);
        }

    }

    public void onAddCarButtonClicked() {
        if (carCount < 5) {
            carCount += 1;
            Car car = new Car(lights);
            cars.add(car);
            Thread t = new Thread(car);
            if (threads.get(0).isAlive()) {
                t.start();
            }
            threads.add(t);
            addCarRow(car);
        }
    }

    public void pauseResumeAll() {
        timer.pause();
        for (TrafficLight l : lights) {
            l.pause();
        }
        for (Car c : cars) {
            c.pause();
        }
    }
}