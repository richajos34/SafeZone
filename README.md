# Local Crime Data Map Web Application
## important update -> aplication moved from warnME to this repo
## Overview
This web application offers an interactive map interface to visualize and interact with local crime data based on Berkeley's Warn Me data.

## Features
- **Interactive Map**: presents crime incidents as markers on the map, providing details upon interaction.
- **Geolocation Support**: Detects and displays users' current location on the map.
- **Search Functionality**: Enables users to search for specific incidents by title.
- **Incident Creation**: Allows users to create incidents at any location with simple form with a double click at desired location
- **Smart Map**: Process user queries and translate them into search terms for your incident database. Ex. "Show me all theft incidents near the library" (I.P.)
- **SMS Updates**: pings a sms message to user (currently have not implemented user settings so custom numbers are allowed) (I.P.)
## Technologies Used
### Frontend
- HTML, CSS
- JavaScript for frontend logic

### Backend
- Spring Boot for the RESTful API
- Java for backend logic
- MongoDB for database

## Libraries 
- Leaflet.js/ Open Street Maps for mapping
- Google Geocoding API (smart search)
- Twillo for sms
- Firebase/ Google auth for authentication
- Flicker Photos (do not have access to google maps photos so had to use free service hence the seemingly random photos)

### Machine Learning
- OpenAI GPT-3

## Installation and Setup
1. **Clone the repository**: `git clone https://github.com/your/repository.git`, download docker image for faster set up
2. **Set up the backend**:
   - Ensure Java and Spring Boot are installed.
   - Configure and set up the chosen database (MongoDB/MySQL/your choice) in the backend code.
   - Run the backend application.
3. **Set up the frontend**:
   - Open the HTML file in a web browser or serve the HTML, CSS, and JavaScript files via a server. (local host 8080)
   - Ensure correct access to backend API endpoints in the frontend code.

## Usage
1. Access the web application via the browser.
2. Explore the map interface.
3. Use the search bar to find specific incidents by title.
4. Interact with markers to view incident details.

## API Endpoints
### Incidents
- `GET /api/incidents`: Retrieve all incidents.
- `GET /api/incidents/{id}`: Retrieve an incident by ID.
- `GET /api/incidents/?title={title}`: Retrieve incidents by title.
- `POST /api/incidents`: Create a new incident / sends SMS.
- `DELETE /api/incidents/{id}`: Delete an incident by ID.
- `GET /api/incidents/search`: Smart Searching with geolocation.
