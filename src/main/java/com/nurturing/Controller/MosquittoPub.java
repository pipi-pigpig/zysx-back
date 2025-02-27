package com.nurturing.Controller;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MosquittoPub {
public static void main(String[] args) throws MqttException {

try {

    MqttClient mqttClient=new MqttClient("tcp://3.tcp.vip.cpolar.cn:10138","test123");
    mqttClient.connect();
    MqttMessage mqttMessage=new MqttMessage();
    String abc="1234567899";
    mqttMessage.setPayload(abc.getBytes());
    mqttClient.publish("test123",mqttMessage);
} catch (MqttException e) {
    e.printStackTrace();
}


}


}
