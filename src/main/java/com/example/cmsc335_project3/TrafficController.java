package com.example.cmsc335_project3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class TrafficController {
    @FXML
    public Label elapsedTimeLabel;
    @FXML
    public Label lt1Color;
    @FXML
    public Label lt2Color;
    @FXML
    public Label lt3Color;
    public GridPane gridPane;
    public Label car1Spd;
    public Label car2Spd;
    public Label car3Spd;
    public Label car1Pos;
    public Label car2Pos;
    public Label car3Pos;
    MyTimer timer;
    Thread timerThread;

    int lightCount = 3;
    int carCount = 3;
    List<Thread> threads = new ArrayList<>();
    List<TrafficLight> lights = new ArrayList<>();
    List<Car> cars = new ArrayList<>();
    TrafficLight light1;
    TrafficLight light2;
    TrafficLight light3;

    Car car1;
    Car car2;
    Car car3;
    /*
    Thread light1Thread;
    Thread light2Thread;
    Thread light3Thread;

     */

    @FXML
    private Label welcomeText;
/*
    public TrafficController() {
        onStopButtonClick();
    }
 */

    @FXML
    protected void onStartButtonClick() {

        System.out.println("Start");
        for (Thread t: threads) {
            t.start();
        }
        /*
        timerThread.start();        //TODO: crashes when used to resume timer.  probably because the thread tries to start when its already started

        light1Thread.start();
        light2Thread.start();
        light3Thread.start();

         */
    }

    @FXML
    public void onPauseButtonClick(ActionEvent actionEvent) {
        timer.pause();
        for (TrafficLight l : lights) {
            l.paused.set(true);
        }
    }

    @FXML
    public void onStopButtonClick() {
        try {
            timer.setRunning(false);
            for (TrafficLight l : lights) {
                l.paused.set(true);
            }
            /*
            light1.pause = true;    // TODO: these should be in a group and looped through
            light2.pause = true;
            light3.pause = true;
             */
        } catch (Exception e) {
            System.out.println(e);
        }
        resetThreads();

    }

    private void resetThreads(){
        timer = new MyTimer();
        //timerThread = new Thread(timer);
        light1 = new TrafficLight(1000);
        light2 = new TrafficLight(2000);
        light3 = new TrafficLight(3000);
        lights.add(light1);
        lights.add(light2);
        lights.add(light3);
        car1 = new Car(lights);
        car2 = new Car(lights);
        car3 = new Car(lights);
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
        /*
        light1Thread = new Thread(light1);
        light2Thread = new Thread(light2);
        light3Thread = new Thread(light3);

         */
    }

    //clears extra cars and lights
    @FXML
    public void onResetButtonClick(ActionEvent actionEvent) {
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



    }

    private void addLightRow(TrafficLight lt){
        gridPane.add(new Label(String.valueOf(lightCount)), 5, lightCount); //number
        //Label l = new Label(String.valueOf(lt.posit));
        gridPane.add(new Label(String.valueOf(lt.posit)), 6, lightCount); //position
        Label l = new Label();
        l.textProperty().bind(lt.ltCol.colorProperty().asString());
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

    public void onAddLightButtonClicked(ActionEvent actionEvent) {
        lightCount += 1;
        TrafficLight lt = new TrafficLight(lightCount * 1000);
        lights.add(lt);
        Thread t = new Thread(lt);
        t.start();
        threads.add(t);
        addLightRow(lt);

    }

    public void onAddCarButtonClicked(ActionEvent actionEvent) {
        carCount += 1;
        Car car = new Car(lights);
        cars.add(car);
        Thread t = new Thread(car);
        t.start();  //car should wait to go until "start" clicked
        threads.add(t);
        addCarRow(car);
    }
}