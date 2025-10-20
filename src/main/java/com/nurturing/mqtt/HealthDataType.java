//package com.nurturing.mqtt;
//
//
//public enum HealthDataType {
//    BLOOD("blood"),
//    HEART("heart"),
//    OXYGEN("oxygen"),
//    PI("pi"),
//    PRESSURE("pressure"),
//    SLEEP("sleep");
//
//    private final String value;
//
//    HealthDataType(String value) {
//        this.value = value;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public static HealthDataType fromValue(String value) {
//        for (HealthDataType type : HealthDataType.values()) {
//            if (type.getValue().equalsIgnoreCase(value)) {
//                return type;
//            }
//        }
//        throw new IllegalArgumentException("Unknown HealthDataType value: " + value);
//    }
//}
