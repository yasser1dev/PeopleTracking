import random
import paho.mqtt.client as mqtt
import json

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


def dataGen():
    gpsData = [];

    for i in range(500):
        newLatitude = round(random.uniform(33.580000, 33.588888), 6)
        newLongitude = round(random.uniform(-7.600000, -7.610000), 6)
        gpsData.append([newLatitude, newLongitude])

    gpsJsonData = json.dumps(gpsData)
    publishToTopic(MQTT_Topic, gpsJsonData)

    '''
    print("var locations = " + str(gpsData))

    file = open("locations.js", "w")
    file.write("var locations = " + str(gpsData))
    '''



mqttc=mqtt.Client()
mqttc.on_connect=on_connect
mqttc.on_disconnect=on_disconnect
mqttc.on_publish=on_publish
mqttc.connect(MQTT_BROKER,int(MQTT_Port),int(Keep_Alive_Interval))

dataGen()

