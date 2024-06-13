var map = L.map('map').setView([0, 0], 2); // Default coordinates and zoom level
console.log("Map created");
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

// Function to add incident markers to the map
function addIncidentMarkers(incidents) {
    incidents.forEach(incident => {
        L.marker([incident.location.x, incident.location.y]).addTo(map)
            .bindPopup(`<b>${incident.title}</b><br>${incident.description}`);
    });
}

function fetchIncidentsData() {
    fetch('http://localhost:8080/api/incidents')
      .then(response => response.json())
      .then(data => {
        // create markers
        data.forEach(function (incident) {
          var marker = L.marker([incident.location.x, incident.location.y]).addTo(map);
          marker.bindPopup(`
                  <p>${incident.title}</p>
                  <p>${incident.description}</p>
                  <p>${incident.timestamp}</p>
                  <p>${incident.status}</p>
                  <p>${incident.type}</p>
                  <button onclick="handlePopupButtonClick(${incident.location.x}, ${incident.location.y})">Photos</button>`).openPopup();
        })
  
      }
      ).catch(error => console.error('Error fetching incidents:', error));
  }

//TODO
function onLocationFound(e) {
    var radius = e.accuracy / 2;
    L.marker(e.latlng).addTo(map)
        .bindPopup("You are within " + radius + " meters from this point").openPopup();
    map.setView(e.latlng, 15); // Set map view to the found location with a specific zoom level
}

function onLocationError(e) {
    alert(e.message);
}

// geolocation support -- for later routing controls
if ('geolocation' in navigator) {
    navigator.geolocation.getCurrentPosition(onLocationFound, onLocationError);
} else {
    alert("Geolocation is not supported by this browser.");
}
//function to fetch incident data
fetchIncidentsData();

function searchLocation() {
    var searchTerm = document.getElementById('searchInput').value;
    if (searchTerm.trim() !== '') {
        // API data based on the search term
        fetch(`http://localhost:8080/api/incidents/?title=${searchTerm}`)
            .then(response => response.json())
            .then(data => {
                if (data && data.location) {
                    var lat = data.location.x;
                    var lon = data.location.y;
                    map.setView([lat, lon], 20); // Set map view to the location with zoom level 10 (adjust as needed)
                } else {
                    alert('Location not found for the searched title.');
                }
            })
            .catch(error => console.error('Error searching location:', error));
    } else {
        alert('Please enter a title to search.');
    }
}
var clickedLocation = null;
var clickedMarker = null;

map.on('dblclick', function (e) {
    clickedLocation = e.latlng;

    // Show the incident form
    document.getElementById("incidentContainer").style.display = "flex";
    console.log('Clicked location:', clickedLocation);
});

document.getElementById('chatbotBtn').addEventListener('click', () => {
    document.getElementById('chatbotContainer').style.display = 'block';
});

document.getElementById('closeChatbotBtn').addEventListener('click', () => {
    document.getElementById('chatbotContainer').style.display = 'none';
});

document.getElementById('sendChatbotBtn').addEventListener('click', async () => {
    const input = document.getElementById('chatbotInput').value;
    if (input.trim() !== '') {
        addChatMessage('user', input);
        document.getElementById('chatbotInput').value = '';

        // Send the input to the backend
        const response = await fetch('/api/incidents/search?query=' + encodeURIComponent(input));
        const data = await response.json();

        if (data.length === 0) {
            addChatMessage('bot', 'No incidents found near the specified location.');
        } else {
            const messages = data.map(incident => `${incident.title} at (${incident.location.x}, ${incident.location.y})`).join('<br>');
            addChatMessage('bot', messages);
        }
    }
});

function addChatMessage(sender, message) {
    const chatBody = document.getElementById('chatbotBody');
    const messageElement = document.createElement('div');
    messageElement.classList.add(sender);
    messageElement.innerHTML = message;
    chatBody.appendChild(messageElement);
    chatBody.scrollTop = chatBody.scrollHeight; // Scroll to the bottom
}


document.getElementById("incidentForm").addEventListener("submit", function (event) {
    console.log("got to form");
    event.preventDefault(); // Prevent the default form submission
    document.getElementById("incidentContainer").style.display = "none";
    const selectedTimeInput = document.getElementById('selected-time');
    const selectedTime = selectedTimeInput.value;

    const [hours, minutes] = selectedTime.split(':');
    const selectedTimestamp = Date.parse(`01 Jan 1970 ${hours}:${minutes}:00 GMT`);

    const timestampDiv = document.getElementById('timestamp');
    timestampDiv.textContent = `Timestamp: ${selectedTimestamp}`;

    // Get the details from the form
    var title = document.getElementById("title").value;
    var description = document.getElementById("description").value;

    var loc = {
        x: clickedLocation.lat,
        y: clickedLocation.lng
    }
    var type = document.getElementById("type").value;
    var status = document.getElementById("status").value;

    // incident data JSON
    var newIncident = {
        title: title,
        description: description,
        timestamp: "",
        location: loc,
        type: type,
        status: status
    };
    console.log(newIncident);
    // Sending to API
    fetch('/api/incidents', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(newIncident),
    })
        .then(response => response.json())
        .then(data => {
            console.log('Incident created:', data);
            addIncidentMarkers([data]);
        })
        .catch(error => {
            console.error('Error creating incident:', error);
        });
});

window.closeIncidentContainer = function () {
    document.getElementById('incidentContainer').style.display = 'none';
  }
fetchIncidentsData();