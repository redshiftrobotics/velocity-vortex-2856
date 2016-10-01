package org.firstinspires.ftc.redshiftrobotics.config;

/**
 * Created by adam on 9/29/16.
 */
public class ConfigVariable<E extends Number> {
    E value;
    E min;
    E max;
    public ConfigVariable(E value, E min, E max) {
        this.value = value;
        this.min = min;
        this.max = max;
    }
    public E getValue() {
        return this.value;
    }
    public void setValue(E val) { this.value = val; }
    public void setMin(E min) { this.min = min; }
    public void setMax(E max) { this.max = max; }
}
