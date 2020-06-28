
// MAP CREATION 

var mymap = L.map('mapid2').setView([33.589886, -7.603869], 13);
var tiles = L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
}).addTo(mymap);


async function getLogs() {
    var locations = [];

    var device_id = $("#device_id").val();
    var numberOfDays = $("#numberOfDays").val();
    var spanDeviceId = $("#deviceIdValidation").text();
    var spanNumberOfDays = $("#numberOfDaysValidation").text();

    if(device_id != "" && numberOfDays != "") {
        if(spanDeviceId != "") {
            $("#deviceIdValidation").html("");
        }

        if(spanNumberOfDays != "") {
            $("#numberOfDaysValidation").html("");
        }

        console.log(device_id, numberOfDays);

        $("#mapTitle").html("Pattern of Device"+device_id+" in the last "+numberOfDays+" days");

        const response = await fetch('http://localhost:8080/apiData/getContacts/'+device_id+'/'+numberOfDays);
        const myJson = await response.json(); //extract JSON from the http response
        // do something with myJson
        myJson.target.forEach(e => {
            locations.push([e.latitude, e.longitude]);
        })

        // var locations = [
        //     [33.585087, -7.608075],
        //     [33.581574, -7.601644],
        //     [33.582451, -7.608346],
        //     [33.583782, -7.604181],
        //     [33.587819, -7.603154]
        // ];

        var polyline = L.polyline(locations, {color: 'red'}).addTo(mymap);
        // zoom the map to the polyline
        mymap.fitBounds(polyline.getBounds());


        // filling up the table

        let table = document.querySelector("#contactsTable");
        let data = myJson.contacts;
        generateTable(table, data);
        
    }
    else {
        if(device_id == ""){
            $("#deviceIdValidation").html("Empty input...<br>");
        }
        else {
            $("#deviceIdValidation").html("");
        }

        if(numberOfDays == ""){
            $("#numberOfDaysValidation").html("Empty input...<br>");
        }
        else {
            $("#numberOfDaysValidation").html("");
        }
    }
    
}



function generateTable(table, data) {
    for (let element of data) {
      let row = table.insertRow();
      for (key in element.citizen) {
        let cell = row.insertCell();
        let text = document.createTextNode(element.citizen[key]);
        cell.appendChild(text);
      }
    }
  }

