package com.example.cmsc335_project3;

import javafx.beans.property.SimpleObjectProperty;

enum LightColor {
    RED, GREEN, YELLOW
}

public class LightColorProperty {
    private SimpleObjectProperty<LightColor> colorProperty;

    public LightColorProperty(LightColor initialColor) {
        colorProperty = new SimpleObjectProperty<>(initialColor);
    }

    public LightColor getColor() {
        return colorProperty.get();
    }

    public SimpleObjectProperty<LightColor> colorProperty() {
        return colorProperty;
    }

    public void setColor(LightColor color) {
        try {
            colorProperty.set(color);
            System.out.println("color is " + colorProperty);
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }
}

