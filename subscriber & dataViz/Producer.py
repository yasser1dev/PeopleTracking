import threading
import paho.mqtt.client as mqtt
import json
import random
from datetime import datetime

MQTT_BROKER="mqtt.eclipse.org"
MQTT_Topic = "ENSETM/BDCC2/S4/IotBigData/PeopleTracking"

MQTT_Port = 1883
Keep_Alive_Interval = 30
def on_connect(client,userdata,rc):
    if rc !=0:
        pass
        print("Unable to connect to broker")
    else :
        print("Connected with MQTT Broker :"+str(MQTT_BROKER))

def on_publish(client,userdata,mid):
    pass

def on_disconnect(client,userdata,rc):
    if rc!=0:
        pass

def publishToTopic(topic,message):
    mqttc.publish(topic,message)
    print("Publish message : "+str(message)+ " to topic : "+str(topic))
    print("")


def publishDataToMqtt():
    threading.Timer(2.0,publishDataToMqtt).start()
    gpsData = {}
    gpsData['latitude'] = round(random.uniform(33.580000, 33.588888), 6)
    gpsData['longitude'] = round(random.uniform(-7.600000, -7.610000), 6)
    gpsData['time'] = str(datetime.now())
    gpsData['speed'] = str(random.randint(1, 5))
    gpsData['id'] = str(random.randint(1, 1000))
    gpsJsonData = json.dumps(gpsData)

    publishToTopic(MQTT_Topic,gpsJsonData)




mqttc=mqtt.Client()
mqttc.on_connect=on_connect
mqttc.on_disconnect=on_disconnect
mqttc.on_publish=on_publish
mqttc.connect(MQTT_BROKER,int(MQTT_Port),int(Keep_Alive_Interval))

publishDataToMqtt()
