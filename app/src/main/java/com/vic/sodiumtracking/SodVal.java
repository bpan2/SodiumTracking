package com.vic.sodiumtracking;

public class SodVal {

    private int _id;
    private String name;
    private double value;

    public SodVal(int _id, String name, double value) {
        this._id = _id;
        this.name = name;
        this.value = value;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
