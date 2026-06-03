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
            marker.bindPopup(getPopupHTML(scooter));
        });

    } catch (error) {
        console.error("Fehler beim Laden der Scooter:", error);
    }
}


function openScooterPopup(id) {
    document.getElementById("popup-overlay").classList.add("active");
    document.getElementById("popup-overlay").dataset.scooterId = id;

    document.getElementById("wartungWegBtn").onclick = function() {
        updateMaintenance(id, "NOT_IN_WARTUNG");
    };
    document.getElementById("wartungBtn").onclick = function() {
        updateMaintenance(id, "IN_WARTUNG");
    };
}

function updateMaintenance(id, status) {
    fetch(`http://localhost:8080/setWartung?id=${id}&status=${status}`, {
        method: 'PUT'
    })
        .then (res => {
            if (res.ok) {
                alert(inWartung ? "Scooter in Wartung gesetzt." : "Scooter aus Wartung genommen.");
                closeScooterPopup();
            } else {
                return res.text().then(msg => alert(msg));
            }
        })
        .catch (() => alert("Fehler beim in Wartung setzen"));
}

function closeScooterPopup() {
    document.getElementById("popup-overlay").classList.remove("active");
}

function toggleFilter() {
    const box = document.getElementById("filter-box");
    if (box) {
        box.classList.toggle("active");
    }
}

//noch bearbeiten
let allScooters = [];
fetch('http://localhost:8080/scooterList', {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' }
})
    .then(response => response.json())
    .then(data => {
        allScooters = data;
        renderScooterList(data);
    })
    .catch(() => alert("Fehler beim Anzeigen der Scooter"))

function renderScooterList(scooters) {
    const list = document.getElementById('scooter-list');
    list.innerHTML = '';

    if (scooters.length === 0) {
        list.innerHTML = '<p style="color:#555;font-size:12px;text-align:center;margin-top:20px">Keine Scooter gefunden</p>';
        return;
    }

    scooters.forEach(scooter => {
        const batteryColor = scooter.ladezustand <= 50 ? '#f44336' : '#4caf50';
        const statusText   = scooter.drivestatus === 'IN_BENUTZUNG' ? '🔴 In Benutzung' : '🟢 Verfügbar';
        const card = document.createElement('div');
        card.className = 'scooter-card';
        card.innerHTML = `
            <p class="scooter-title">${scooter.marke} ${scooter.modell}</p>
            <p class="scooter-id">ID: ${scooter.id}</p>
            <p class="scooter-battery" style="color:${batteryColor}">🔋 ${scooter.ladezustand}%</p>
            <p class="scooter-maintenance">🔧 Wartung: ${scooter.status}</p>
            <p class="scooter-maintenance">🛴 Fahrstatus: ${scooter.drivestatus}</p>
            <p class="scooter-maintenance">📍  ${scooter.latitude}, ${scooter.longitude} </p>
        `;
        card.addEventListener("click", () => {
            map.flyTo([scooter.latitude, scooter.longitude], 15, { duration: 1.2 });
            openScooterPopup(scooter.id);
        });
      list.appendChild(card);
    });
}

window.addEventListener("load", () => {
    const slider = document.getElementById("battery-filter");
    if (slider) {
        slider.addEventListener("input", function() {
            document.getElementById("battery-value").textContent = this.value + '%';
        });
    }
});

document.getElementById('filterGoBtn').addEventListener('click', function() {
    const value = document.getElementById('battery-filter').value;

    fetch(`http://localhost:8080/scooterListFilter?thresholdPercentage=${value}`)
        .then(response => response.json())
        .then(data => {
            renderScooterList(data);
        })
        .catch(() => alert('Fehler beim Filtern.'));
});
loadScooters();
