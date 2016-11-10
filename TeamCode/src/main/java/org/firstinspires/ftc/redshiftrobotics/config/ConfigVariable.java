package org.firstinspires.ftc.redshiftrobotics.config;

/**
 * Created by adam on 9/29/16.
 */
public class ConfigVariable<E extends Number> {
    E value;
    E min;
    E max;
    public ConfigVariable(E value) {
        this.value = value;
    }
    public E getValue() {
        return this.value;
    }
    public void setValue(E val) { this.value = val; }
}
