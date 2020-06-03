package org.ensetm.serviceweb;
/*
import com.google.gson.JsonObject;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
*/
/*@RestController
@RequestMapping("/apiData")
public class GpsData {

    @PostMapping("/log")
    public String gpsData(@RequestParam(name = "latitude") String latitude,
                          @RequestParam(name="longitude") String longitude,
                          @RequestParam(name = "time") String time,
                          @RequestParam(name = "speed") String speed,
                          @RequestParam(name = "id") String id) throws IOException, MqttException, ParseException {
        System.out.println("--------- HERE ----------");
        // Create new JSON Object
        JsonObject gpsData = new JsonObject();
        gpsData.addProperty("latitude", latitude);
        gpsData.addProperty("longitude", longitude);
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        //gpsData.addProperty("time", String.valueOf(dateFormat.parse(time)));
        gpsData.addProperty("time", time);
        gpsData.addProperty("speed", speed);
        gpsData.addProperty("id", id);


        System.out.println(gpsData.toString());
        brokerConnection(gpsData);
        return "latitude : "+ latitude +"\n" +
                "longitude : "+ longitude+"\n"+
                "time : "+ time +"\n"+
                "speed : "+ speed+"\n"+
                "id : "+ id;
    }

    public void brokerConnection(JsonObject gpsData) throws MqttException {
        String mqqtBroker="tcp://mqtt.eclipse.org:1883";
        String mqqtTopic="ENSETM/BDCC2/S4/IotBigData/PeopleTracking";
        int qos=1;
        MqttClient mqttClient = new MqttClient(mqqtBroker,String.valueOf(System.nanoTime()));
        MqttConnectOptions connOpts = new MqttConnectOptions();

        connOpts.setCleanSession(true); //no persistent session
        connOpts.setKeepAliveInterval(1000);
        MqttMessage message = new MqttMessage(gpsData.toString().getBytes());
        message.setQos(qos);     //sets qos level 1
        message.setRetained(true); //sets retained message

        MqttTopic topic2 = mqttClient.getTopic(mqqtTopic);

        mqttClient.connect(connOpts); //connects the broker with connect options
        topic2.publish(message);

    }
}*/
