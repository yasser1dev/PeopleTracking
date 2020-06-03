import paho.mqtt.client as mqtt
import sqlite3
import json

import folium
from folium import plugins
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

'''
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
'''

data = dict()
data["latitude"] = list()
data["longitude"] = list()
data["time"] = list()
data["speed"] = list()
data["id_device"] = list()


def process_data(data):
    jsonData = json.loads(data)
    latitude = float(jsonData["latitude"])
    longitude = float(jsonData["longitude"])
    time = jsonData["time"]
    speed = float(jsonData["speed"])
    id_device = jsonData["id"]

    if id_device not in data["id_device"]:
        if speed >= 1.4:
            data["latitude"].append(latitude)
            data["longitude"].append(longitude)
            data["time"].append(time)
            data["speed"].append(speed)
            data["id_device"].append(id_device)
    else:
        data = data.set_index("id_device")
        data.drop(id_device, axis=0)
        if speed >= 1.4:
            data["latitude"].append(latitude)
            data["longitude"].append(longitude)
            data["time"].append(time)
            data["speed"].append(speed)
            data["id_device"].append(id_device)

    df = pd.DataFrame(data)
    data_viz(df)


def data_viz(dataFrame):
    '''
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    !!!!!!!! Still need to figure out how to display the map once for all while data can change dynamically !!!!!!!!
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    '''
    m = folium.Map([33.32, -7.35])
    # convert to (n, 2) nd-array format for heatmap
    stationArr = dataFrame[['latitude', 'longitude']].as_matrix()

    # plot heatmap
    m.add_children(plugins.HeatMap(stationArr, radius=2))




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
    # print("MQTT Message: " + str(msg.payload))
    jsonData = str(msg.payload).split("'")[1]
    print("Data: " + jsonData)
    # insertData(jsonData)
    process_data(jsonData)


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
