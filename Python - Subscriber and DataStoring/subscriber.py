import paho.mqtt.client as mqtt
import sqlite3
import json


dbName = "PeopleTracking.db"
conn = sqlite3.connect(dbName)
curs = conn.cursor()



def insertData(data):
    jsonData = json.loads(data)
    latitude = float(jsonData["latitude"])
    longitude = float(jsonData["longitude"])
    Date_time = jsonData["time"]
    speed = float(jsonData["speed"])
    id_device = jsonData["id"]
    try:
        sql = """insert into movementData(latitude,longitude,Date_time,speed,id_device) values(?,?,?,?,?)"""
        curs.execute(sql, [latitude, longitude, Date_time, speed, id_device])
        conn.commit()
        print("------- Data Inserted ---------")
    except sqlite3.Error as e:
        print(e)



def on_connect(mosq, obj, rc):
    if rc == 0:
        print("Connected")
        mqttc.subscribe(MQTT_Topic, 0)  # Subscribe to all sensors at Base Topic
    else:
        print("Bad Connection")


def on_message(mosq, obj, msg):
    # this is the Master call for saving MQTT Date into DB
    print("MQTT Data Received ...")
    print("MQTT Topic: " + msg.topic)
    #print("MQTT Message: " + str(msg.payload))
    jsonData = str(msg.payload).split("'")[1]
    print("Data: " + jsonData)

    insertData(jsonData)


    
def on_subscribe(mosq, obj, mid, granted_qos):
    pass


# MQTT Settings
MQTT_Broker = "mqtt.eclipse.org"
MQTT_Port = 1883
Keep_alive_interval = 30
MQTT_Topic = "ENSETM/BDCC2/S4/IotBigData/PeopleTracking"
mqttc = mqtt.Client()
mqttc.on_message = on_message
mqttc.on_connect = on_connect
mqttc.on_subscribe = on_subscribe

# Connect & Subscribe
mqttc.connect(MQTT_Broker, int(MQTT_Port), int(Keep_alive_interval))
mqttc.subscribe((MQTT_Topic, 0))

mqttc.loop_forever()  # Continue the network loop
