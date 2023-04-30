package com.lawstack.app.utils;


public enum JobNumber {
    
    NORMAL(5),
    EXPERT(10),
    PRO(15);

    private final int value;

    private JobNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
}
