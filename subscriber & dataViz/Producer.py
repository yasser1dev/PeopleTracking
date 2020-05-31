import json
import random
import threading
from datetime import datetime
import paho.mqtt.client as mqtt
import json
import random
import time
import requests

MQTT_BROKER="mqtt.eclipse.org"
MQTT_Topic = "peopletracking/track"

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
    gpsData['latitude'] = "%.6f" % random.uniform(33.5, 33.6)
    gpsData['longitude'] = "%.6f" % random.uniform(-7.7, -7.5)
    gpsData['time'] = str(time.timezone / -(60 * 60))
    #gpsData['speed'] = str(random.randint(1, 10))
    #gpsData['id'] = str(random.randint(1, 1000))
    gpsData['speed'] = str(random.randint(1, 3))
    gpsData['id'] = str(random.randint(1, 3))
    gpsJsonData = json.dumps(gpsData)

    publishToTopic(MQTT_Topic,gpsJsonData)







mqttc=mqtt.Client()
mqttc.on_connect=on_connect
mqttc.on_disconnect=on_disconnect
mqttc.on_publish=on_publish
mqttc.connect(MQTT_BROKER,int(MQTT_Port),int(Keep_Alive_Interval))
toggle=0
publishDataToMqtt()
