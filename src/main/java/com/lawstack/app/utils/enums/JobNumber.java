package com.lawstack.app.utils.enums;


public enum JobNumber {
    
    DEWDROPPER(3),
    SPRINKLE(6),
    RAINMAKER(12);

    private final int value;

    private JobNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public boolean canUpdate(JobNumber newJobNumber) {
        return newJobNumber.getValue() >= this.value;
    }
}
