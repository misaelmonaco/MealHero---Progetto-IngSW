const defaultLatitude = 41.8978;
const defaultLongitude = 12.5248;
const defaultZoom = 12;

const map = L.map('map').setView([defaultLatitude, defaultLongitude], defaultZoom);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

let userLocation = null;

function onLocationFound(e) {
    userLocation = e.latlng;
    map.setView(e.latlng, 12);
    findLocation(userLocation);
}

function onLocationError(e) {
    console.error(e.message);
    alert("Unable to retrieve your location. Please ensure that location services are enabled.");
}

if ("geolocation" in navigator) {
    map.locate({ setView: true, maxZoom: 16 });
} else {
    alert("Geolocation is not supported by this browser.");
}

map.on('locationfound', onLocationFound);
map.on('locationerror', onLocationError);

var marker = L.marker([41.12434, 14.78941], { alt: 'Pepe Nero - Benevento' })
    .addTo(map)
    .bindPopup('Pepe Nero');

function findLocation(userLocation) {
    let locationName = '';

    const lat = userLocation.lat;
    const lng = userLocation.lng;
    const geocodingUrl = `https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${lat}&lon=${lng}`;

    fetch(geocodingUrl)
        .then(response => response.json())
        .then(data => {
            if (data.address) {
                locationName = data.address.city || data.address.town || data.address.village || data.address.hamlet || 'Località non trovata';
                findNearbyRestaurants(locationName);
            } else {
                console.error('Geocoding error: no address found.');
            }
        })
        .catch(error => {
            console.error('Geocoding error:', error);
        });
}

function findNearbyRestaurants(location) {
    const restaurantListDiv = document.getElementById('restaurantList');
    restaurantListDiv.innerHTML = '<p>Loading...</p>'; // Display loading message

    const apiUrl = `/restaurant/location/${encodeURIComponent(location)}`;

    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            restaurantListDiv.innerHTML = ''; // Clear loading message
            if (Array.isArray(data)) {
                data.forEach(restaurant => {
                    const restaurantItem = document.createElement('div');
                    restaurantItem.className = 'box_restaurants';

                    restaurantItem.innerHTML = `
                    <div class="container-restaurant">
                        <span>
                            <img src="restaurantsImages/${restaurant.name}.png">  
                        </span>  
                        <div id="card">
                            <h4>${restaurant.name}</h4><br>
                            <a>
                                <svg xmlns="http://www.w3.org/2000/svg" id="Outline" viewBox="0 0 24 24" width="15px" height="15px"><path d="M12,6a4,4,0,1,0,4,4A4,4,0,0,0,12,6Zm0,6a2,2,0,1,1,2-2A2,2,0,0,1,12,12Z"/><path d="M12,24a5.271,5.271,0,0,1-4.311-2.2c-3.811-5.257-5.744-9.209-5.744-11.747a10.055,10.055,0,0,1,20.11,0c0,2.538-1.933,6.49-5.744,11.747A5.271,5.271,0,0,1,12,24ZM12,2.181a7.883,7.883,0,0,0-7.874,7.874c0,2.01,1.893,5.727,5.329,10.466a3.145,3.145,0,0,0,5.09,0c3.436-4.739,5.329-8.456,5.329-10.466A7.883,7.883,0,0,0,12,2.181Z"/></svg>
                                <label>${restaurant.address}</label><br>
                            </a>
                            <a>
                                <svg xmlns="http://www.w3.org/2000/svg" id="Layer_1" data-name="Layer 1" viewBox="0 0 24 24" width="14px" height="14px"><path d="M13,1a1,1,0,0,1,1-1A10.011,10.011,0,0,1,24,10a1,1,0,0,1-2,0,8.009,8.009,0,0,0-8-8A1,1,0,0,1,13,1Zm1,5a4,4,0,0,1,4,4,1,1,0,0,0,2,0,6.006,6.006,0,0,0-6-6,1,1,0,0,0,0,2Zm9.093,10.739a3.1,3.1,0,0,1,0,4.378l-.91,1.049c-8.19,7.841-28.12-12.084-20.4-20.3l1.15-1A3.081,3.081,0,0,1,7.26.906c.031.031,1.884,2.438,1.884,2.438a3.1,3.1,0,0,1-.007,4.282L7.979,9.082a12.781,12.781,0,0,0,6.931,6.945l1.465-1.165a3.1,3.1,0,0,1,4.281-.006S23.062,16.708,23.093,16.739Zm-1.376,1.454s-2.393-1.841-2.424-1.872a1.1,1.1,0,0,0-1.549,0c-.027.028-2.044,1.635-2.044,1.635a1,1,0,0,0-.007,1.418c.007.007,1.843,1.365,1.843,1.365l.678-.678A1,1,0,0,1,18.26,18.191Z"/></svg>
                                <label>${restaurant.phone}</label>
                            </a>
                        </div>
                    </div>`;
                    restaurantListDiv.appendChild(restaurantItem);
                });
            } else {
                restaurantListDiv.innerHTML = '<p>No restaurants found in your area.</p>'; // Display message if no restaurants are found
            }
        })
        .catch(error => {
            restaurantListDiv.innerHTML = '<p>Error fetching restaurants. Please try again later.</p>'; // Display error message
            console.error('Error fetching restaurants:', error);
        });
}
