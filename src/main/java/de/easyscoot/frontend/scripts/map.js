const map = L.map('map', { zoomControl: false }).setView([52.3759, 9.7320], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
}).addTo(map);

L.control.zoom({ position: 'bottomright' }).addTo(map);

let userMarker = null;

function initUserLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                const lat = position.coords.latitude;
                const lon = position.coords.longitude;

                window.currentUserLat = lat;
                window.currentUserLon = lon;

                map.setView([lat, lon], 15);

                const userIconHTML = `
                    <div class="user-location-marker">
                        <div class="user-location-pulse"></div>
                        <div class="user-location-dot"></div>
                    </div>
                `;

                const userIcon = L.divIcon({
                    className: 'clear-leaflet-bg',
                    html: userIconHTML,
                    iconSize: [20, 20],
                    iconAnchor: [10, 10]
                });

                if (userMarker) {
                    map.removeLayer(userMarker);
                }
                userMarker = L.marker([lat, lon], { icon: userIcon, zIndexOffset: 1000 }).addTo(map);
            },
            (error) => {
                console.warn("Geolocation warning:", error.message);
                map.setView([52.3759, 9.7320], 13);
            },
            {
                enableHighAccuracy: false,
                timeout: 10000,
                maximumAge: 60000
            }
        );
    }
}

initUserLocation();

window.addEventListener('load', () => {
    if (sessionStorage.getItem('bookingId')) {
        showRideDashboard();
    }
});

let activePanel = null;

function togglePanel(name) {
    const panel = document.getElementById('home-panel');
    const btn = document.getElementById('btn-' + name);
    const allBtns = document.querySelectorAll('.nav-icon');

    if (activePanel === name) {
        panel.classList.remove('open');
        btn.classList.remove('active');
        activePanel = null;
    } else {
        panel.classList.add('open');
        allBtns.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        activePanel = name;
    }

    setTimeout(() => map.invalidateSize(), 280);
}

function toggleLocationPopup() {
    const popup = document.getElementById('location-popup');
    popup.classList.toggle('open');
    if (popup.classList.contains('open')) {
        setTimeout(() => document.getElementById('location-input').focus(), 50);
    }
}

document.getElementById('location-input').addEventListener('keydown', function(e) {
    if (e.key === 'Enter') goToLocation();
});

document.addEventListener('click', function(e) {
    const popup = document.getElementById('location-popup');
    const btn = document.getElementById('location-btn');
    if (!popup.contains(e.target) && !btn.contains(e.target)) {
        popup.classList.remove('open');
    }
});

const profilbtn = document.getElementById("profilbtn");
if (profilbtn) {
    profilbtn.addEventListener("click", function () {
        window.location.href = "profil.html";
    });
}

const logoutBtn = document.getElementById("logoutBtn");
if(logoutBtn) {
    logoutBtn.addEventListener("click", function () {
        sessionStorage.clear()
        window.location.href = "index.html";
    });
}

const logoutBtn1 = document.getElementById("logoutBtn1");
if(logoutBtn1) {
    logoutBtn1.addEventListener("click", function () {
        sessionStorage.clear()
        window.location.href = "index.html";
    });
}

async function goToLocation() {
    const input = document.getElementById('location-input').value.trim();
    const error = document.getElementById('location-error');
    const label = document.getElementById('location-label');
    error.style.display = 'none';

    if (!input) return;

    try {
        const res = await fetch(`https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(input)}&format=json&limit=1`);
        const data = await res.json();

        if (!data.length) {
            error.style.display = 'block';
            return;
        }

        const { lat, lon, display_name } = data[0];
        map.flyTo([parseFloat(lat), parseFloat(lon)], 13, { duration: 1.2 });

        const shortName = display_name.split(',').slice(0, 2).join(',').trim();
        label.textContent = shortName;

        document.getElementById('location-popup').classList.remove('open');
        document.getElementById('location-input').value = '';
    } catch {
        error.style.display = 'block';
    }
}

async function loadScooters() {
    try {
        const response = await fetch('http://localhost:8080/scooters/available');
        const scooters = await response.json();

        scooters.forEach(scooter => {
            const customIcon = L.divIcon({
                className: 'clear-leaflet-bg',
                html: getMarkerHTML(),
                iconSize: [30, 30],
                iconAnchor: [15, 30],
                popupAnchor: [0, -30]
            });

            const marker = L.marker([scooter.latitude, scooter.longitude], { icon: customIcon }).addTo(map);

            marker.bindPopup(() => {
                return getPopupHTML(scooter);
            });
        });

    } catch (error) {
        console.error("Error loading scooters:", error);
    }
}

loadScooters();

const locateBtn = document.createElement('button');
locateBtn.className = 'locate-me-btn';
locateBtn.innerHTML = `<svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" stroke-width="2" fill="none"><circle cx="12" cy="12" r="10"></circle><polygon points="16.24 7.76 14.12 14.12 7.76 16.24 9.88 9.88 16.24 7.76"></polygon></svg>`;
document.body.appendChild(locateBtn);

locateBtn.addEventListener('click', () => {
    if (window.currentUserLat && window.currentUserLon) {
        map.flyTo([window.currentUserLat, window.currentUserLon], 15, { duration: 1.5 });
    } else {
        initUserLocation();
    }
});