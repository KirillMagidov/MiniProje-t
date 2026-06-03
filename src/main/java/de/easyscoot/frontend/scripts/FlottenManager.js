const map = L.map('map', { zoomControl: false }).setView([52.3759, 9.7320], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
}).addTo(map);

L.control.zoom({ position: 'bottomright' }).addTo(map);

let activePanel = null;
let allScooters = [];
let mapMarkers = [];
let filterActive = false;

function togglePanel(name) {
    let panel;
    if (name === 'home') {
        panel = document.getElementById('home-panel');
    } else if (name === 'scooter') {
        panel = document.getElementById('scooter-panel');
    } else {
        return;
    }

    const btn = document.getElementById('btn-' + name);
    const allBtns = document.querySelectorAll('.nav-icon');

    if (activePanel === name) {
        panel.classList.remove('open');
        btn.classList.remove('active');
        activePanel = null;
        return;
    }

    document.getElementById('home-panel').classList.remove('open');
    document.getElementById('scooter-panel').classList.remove('open');
    allBtns.forEach(b => b.classList.remove('active'));

    panel.classList.add('open');
    btn.classList.add('active');
    activePanel = name;

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

// Profil Button
const profilbtn = document.getElementById("profilbtn");
if (profilbtn) {
    profilbtn.addEventListener("click", function () {
        window.location.href = "profil.html";
    });
}

const logoutBtn = document.getElementById("logoutBtn");
if (logoutBtn) {
    logoutBtn.addEventListener("click", function () {
        sessionStorage.clear();
        window.location.href = "index.html";
    });
}

const logoutBtn1 = document.getElementById("logoutBtn1");
if (logoutBtn1) {
    logoutBtn1.addEventListener("click", function () {
        sessionStorage.clear();
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

function clearMarkers() {
    mapMarkers.forEach(m => map.removeLayer(m));
    mapMarkers = [];
}

// Marker HTML direkt eingefügt, falls marker.js nicht geladen ist
function getFlottenmanagerMarkerHTML() {
    return `
        <div class="marker-wrapper">
            <div class="pin"></div>
            <div class="pulse"></div>
        </div>
    `;
}

function renderMarkers(scooters) {
    clearMarkers();
    scooters.forEach(scooter => {
        const customIcon = L.divIcon({
            className: 'clear-leaflet-bg',
            html: getFlottenmanagerMarkerHTML(),
            iconSize: [30, 30],
            iconAnchor: [15, 30],
            popupAnchor: [0, -30]
        });
        const marker = L.marker([scooter.latitude, scooter.longitude], { icon: customIcon }).addTo(map);
        marker.bindPopup(`
            <div class="scooter-popup">
                <div class="scooter-header">
                    <span>${scooter.marke} ${scooter.modell}</span>
                    <span>🔋 ${scooter.ladezustand}%</span>
                </div>
                <div class="scooter-id">ID: ${scooter.id}</div>
                <div class="scooter-id" style="margin-top: 8px;">🔧 ${scooter.status}</div>
                <div class="scooter-id">🛴 ${scooter.availability || scooter.drivestatus}</div>
            </div>
        `);
        mapMarkers.push(marker);
    });
}

function renderScooterList(scooters) {
    const list = document.getElementById('scooter-list');
    list.innerHTML = '';

    if (scooters.length === 0) {
        list.innerHTML = '<p style="color:#555;font-size:12px;text-align:center;margin-top:20px">Keine Scooter gefunden</p>';
        return;
    }

    scooters.forEach(scooter => {
        const batteryColor = scooter.ladezustand <= 50 ? '#f44336' : '#4caf50';
        const card = document.createElement('div');
        card.className = 'scooter-card';
        card.innerHTML = `
            <p class="scooter-title">${scooter.marke} ${scooter.modell}</p>
            <p class="scooter-id">ID: ${scooter.id}</p>
            <p class="scooter-battery" style="color:${batteryColor}">🔋 ${scooter.ladezustand}%</p>
            <p class="scooter-maintenance">🔧 Wartung: ${scooter.status}</p>
            <p class="scooter-maintenance">🛴 Fahrstatus: ${scooter.availability || scooter.drivestatus}</p>
            <p class="scooter-maintenance">📍 ${scooter.latitude}, ${scooter.longitude}</p>
        `;
        card.addEventListener("click", () => {
            map.flyTo([scooter.latitude, scooter.longitude], 15, { duration: 1.2 });
        });
        list.appendChild(card);
    });
}

function toggleFilter() {
    filterActive = !filterActive;
    const btn = document.querySelector('.filter-btn');

    if (filterActive) {
        btn.style.color = '#c0392b';
        const filtered = allScooters.filter(s => s.ladezustand <= 50);
        renderScooterList(filtered);
        renderMarkers(filtered);
    } else {
        btn.style.color = '';
        renderScooterList(allScooters);
        renderMarkers(allScooters);
    }
}

fetch('http://localhost:8080/scooterList', {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' }
})
    .then(response => response.json())
    .then(data => {
        allScooters = data;
        renderScooterList(data);
        renderMarkers(data);
    })
    .catch(() => showError("Fehler beim Anzeigen der Scooter"));

async function loadCustomerProfile() {
    const customerId = sessionStorage.getItem('customerId');
    if (!customerId) return;

    try {
        const response = await fetch(`http://localhost:8080/getCustomer?customerId=${customerId}`);
        if (!response.ok) return;
        const customer = await response.json();

        const nameEl   = document.querySelector('.profile-name');
        const avatarEl = document.querySelector('.avatar');

        if (nameEl && customer.foreName && customer.name) {
            nameEl.textContent = `${customer.foreName} ${customer.name}`;
        }
        if (avatarEl && customer.foreName && customer.name) {
            avatarEl.textContent = customer.foreName[0] + customer.name[0];
        }
    } catch (e) {
        showError("Fehler beim Laden der Profildaten.");
    }
}

loadCustomerProfile();