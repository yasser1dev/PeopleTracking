
// MAP CREATION 

var mymap = L.map('mapid').setView([33.589886, -7.603869], 13);
var tiles = L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
}).addTo(mymap);
/*
L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox/streets-v11',
    tileSize: 512,
    zoomOffset: -1,
    accessToken: 'pk.eyJ1Ijoib3Vzc2FtYWFpdGFsbGEiLCJhIjoiY2thdWNqeWFiMHFieTJxbzNiZ243aTU1eCJ9.4TINux1c4WOpaVZIsL5uaQ'
}).addTo(mymap);
*/


// BROKER CONNECTION

// Generate a random client ID
clientID = "clientID-" + parseInt(Math.random() * 100);

host = "mqtt.eclipse.org";
port = 80;

// Initialize new Paho client connection
client = new Paho.MQTT.Client(host, Number(port), clientID);

// Set callback handlers
client.onConnectionLost = onConnectionLost;
client.onMessageArrived = onMessageArrived;

// Connect the client, if successful, call onConnect function
client.connect({ 
    onSuccess: onConnect,
});

// called when the client connects
function onConnect() {
    // Once a connection has been made, make a subscription and send a message.
    console.log("onConnect");
    client.subscribe("ENSETM/BDCC2/S4/IotBigData/PeopleTracking");
  }

  // called when the client loses its connection
function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
      console.log("onConnectionLost:"+responseObject.errorMessage);
    }
  }


  locations = [];
  data = {};
  refreshHeat = null;
  markers={};
  markersNotUnique=[];
  // called when a message arrives
function onMessageArrived(message) {
    console.log("onMessageArrived:"+message.payloadString);

    obj = JSON.parse(message.payloadString);
    console.log(obj);

    
    if(!(obj.id in data)) {
        if(obj.speed >= 2) {
            data[obj.id] = [obj.latitude, obj.longitude]; // ---heat version---
            locations.push(data[obj.id]); // ---heat version---
            showMarkers(markers,locations[locations.length-1],obj,mymap);
        }
    }
    else {
        locations = locations.filter(function(value, index, arr) { return value != data[obj.id]}); // ---heat version---
        //mymap.removeLayer(data[obj.id]); // +++marker version+++

        if(obj.speed >= 2) {
            data[obj.id] = [obj.latitude, obj.longitude]; // ---heat version---
            locations.push(data[obj.id]); // ---heat version---
            //marker = L.marker([obj.latitude, obj.longitude]).addTo(mymap); // +++marker version+++
            //data[obj.id] = marker; // +++marker version+++
            showMarkers(markers,locations[locations.length-1],obj,mymap);
        }
    }
    
    

                                    // ---heat version---  
    if(refreshHeat == null) {
        refreshHeat = L.heatLayer(locations).addTo(mymap); 
    }
    else {
        mymap.removeLayer(refreshHeat);
        refreshHeat = L.heatLayer(locations).addTo(mymap); 
    }
    
        console.log(mymap.getZoom())
        if (mymap.getZoom() <16){

            markersNotUnique.forEach(marker => {
                mymap.removeLayer(marker);
            });
               
        }
        else {
            markersNotUnique.forEach(marker => {
                mymap.removeLayer(marker);
            });
            
            for(var key in markers){
                mymap.addLayer(markers[key])
            }
        }

    //console.log(locations);
    //console.log(data);
    
     
    
  }

  function showMarkers(markers,location,obj,mymap){
    marker = L.marker([location[0],location[1]])
    .bindPopup(obj.id)
    .openPopup().addTo(mymap); // +++marker version+++
    markers[obj.id] = marker; // +++marker version+++

    markersNotUnique.push(marker)

    

}
