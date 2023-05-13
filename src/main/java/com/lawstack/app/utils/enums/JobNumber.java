package com.lawstack.app.utils.enums;


public enum JobNumber {
    
    NORMAL(3),
    EXPERT(6),
    PRO(12);

    private final int value;

    private JobNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
}
