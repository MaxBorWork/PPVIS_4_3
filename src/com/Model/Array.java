package com.Model;

import java.util.ArrayList;
import java.util.List;

public class Array {
    private List<Integer> array;

    Array() {
        array = new ArrayList<>();
    }

    public void addValue(int value) {
        array.add(value);
    }

    public void setArray(List<Integer> array) {
        this.array = array;
    }

    public List<Integer> getArray() {
        return array;
    }
}
