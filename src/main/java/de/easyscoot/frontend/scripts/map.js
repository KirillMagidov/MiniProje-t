const map = L.map('map', { zoomControl: false }).setView([52.3759, 9.7320], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
}).addTo(map);

L.control.zoom({ position: 'bottomright' }).addTo(map);

window.addEventListener('load', () => {
    if (localStorage.getItem('bookingId')) {
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
            marker.bindPopup(getPopupHTML(scooter));
        });

    } catch (error) {
        console.error("Fehler beim Laden der Scooter:", error);
    }
}

loadScooters();