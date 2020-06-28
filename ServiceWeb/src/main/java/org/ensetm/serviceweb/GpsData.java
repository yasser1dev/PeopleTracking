package org.ensetm.serviceweb;

import DataProcessing.Processing;
import com.google.gson.JsonObject;
import net.minidev.json.JSONObject;
import org.eclipse.paho.client.mqttv3.*;
import org.ensetm.serviceweb.dao.ICitizensRepository;
import org.ensetm.serviceweb.dao.IGpsLogRepository;
import org.ensetm.serviceweb.entities.Citizen;
import org.ensetm.serviceweb.entities.GpsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/apiData")
public class GpsData {
    @Autowired
    IGpsLogRepository gpsLogRepository;
    @Autowired
    ICitizensRepository citizensRepository;
    Processing dataProcessing;
    Citizen target;
    List<Object> contacts;
    JSONObject json;

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


    @GetMapping("/getContacts/{idCitizen}/{nbrJour}")
    public JSONObject  getContacts(@PathVariable(name = "idCitizen") long id, @PathVariable(name = "nbrJour") int nbrJr) throws ParseException, IOException, ClassNotFoundException, InterruptedException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date=new Date();
        String currentDate=formatter.format(date);
        Date date1=formatter.parse(currentDate);

        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH,-nbrJr);
        String previousDate=formatter.format(c.getTime());

        Date date2=formatter.parse(previousDate);

        Collection<GpsLog> gpsData=gpsLogRepository.findGpsDataLastDays(date2,date1);

        File fileContact = new File("src/main/resources/input/gpsDataContact.txt");
        FileWriter fwrContact = new FileWriter(fileContact, true);
        BufferedWriter br = new BufferedWriter(fwrContact);
        PrintWriter pr = new PrintWriter(br);
        for(GpsLog gpsDataContact:gpsData){
            pr.println(gpsDataContact.getId()+","+gpsDataContact.getLatitude()+","+gpsDataContact.getLongitude()+":");
        }
        pr.close();
        br.close();
        fwrContact.close();

        for(GpsLog gpsLog:gpsData){
            if(gpsLog.getId()==id) Processing.sickGpsData.add(gpsLog);
        }

        boolean resultState=ProcessData();
        target=citizensRepository.findById(id).get();
        if (!resultState) System.exit(1);
        else{

            gpsData.forEach((gpsLog)->{
                if(id==gpsLog.getId()){
                    GpsLog g=new GpsLog();
                    g.setId(gpsLog.getId());
                    g.setLongitude(gpsLog.getLongitude());
                    g.setLatitude(gpsLog.getLatitude());
                    g.setDateTime(gpsLog.getDateTime());
                    g.setSpeed(gpsLog.getSpeed());
                    target.getGpsLogCollection().add(g);
                }
            });
           contacts=structureData(gpsData);
           //contacts.add(target);
            json = new JSONObject();
            json.put("contacts",contacts);
            json.put("target",target.getGpsLogCollection());

        }
        return json;
    }

    public boolean ProcessData() throws InterruptedException, IOException, ClassNotFoundException {
        return Processing.mapReduce();
    }

    public List<Object> structureData(Collection<GpsLog> gpsData) throws IOException {
        List<Long> idContactList = new ArrayList<>();
        List<Object> contacts = new ArrayList<>();

        File file = new File(Processing.outputFile + "/part-r-00000");
        BufferedReader br1 = new BufferedReader(new FileReader(file));
        String st;

        while ((st = br1.readLine()) != null) {
            Long idContact = Long.valueOf(st.split("\t")[0]);
            idContactList.add(idContact);
        }

        idContactList.forEach((idContact) -> {
            gpsData.forEach((gpsLog) -> {
                if (gpsLog.getId() == idContact) contacts.add(gpsLog);
            });
        });

        return contacts;
    }
}
