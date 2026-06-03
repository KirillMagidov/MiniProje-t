let activeBookingId = null;
let rideTimer = null;
let rideSeconds = 0;
let activeRoute = null;

function calculateDistance(lat1, lon1, lat2, lon2) {
    if (!lat1 || !lon1 || !lat2 || !lon2) return null;
    const R = 6371;
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
        Math.sin(dLon/2) * Math.sin(dLon/2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c;
}

function getMarkerHTML() {
    return `
        <div class="marker-wrapper" onclick="selectMarker(this)">
            <div class="pin"></div>
            <div class="pulse"></div>
        </div>
    `;
}

function selectMarker(clickedElement) {
    document.querySelectorAll('.pin').forEach(pin => pin.classList.remove('selected'));
    const pinInside = clickedElement.querySelector('.pin');
    if (pinInside) pinInside.classList.add('selected');
}

function getPopupHTML(scooter) {
    const isRiding = !!sessionStorage.getItem('bookingId');

    let distanceHtml = '';
    let routeBtn = '';

    if (window.currentUserLat && window.currentUserLon) {
        const distKm = calculateDistance(window.currentUserLat, window.currentUserLon, scooter.latitude, scooter.longitude);
        if (distKm !== null) {
            const walkTime = Math.ceil((distKm / 5) * 60);
            const driveTime = Math.ceil((distKm / 30) * 60);
            distanceHtml = `
                <div class="scooter-distance">
                    <span>🚶 ${walkTime} Min</span>
                    <span>🚗 ${driveTime} Min</span>
                    <span>📍 ${distKm.toFixed(2)} km</span>
                </div>
            `;
        }
        routeBtn = `
            <button class="btn-route" onclick="showRouteTo(${scooter.latitude}, ${scooter.longitude})">
                🗺️ Route anzeigen
            </button>
        `;
    }

    return `
        <div class="scooter-popup">
            <div class="scooter-header">
                <span>${scooter.marke} ${scooter.modell}</span>
                <span>🔋 ${scooter.ladezustand}%</span>
            </div>
            <div class="scooter-id">ID: ${scooter.id}</div>
            ${distanceHtml}
            ${routeBtn}
            <div class="scooter-price">2.00 € Start + 0.15 € / Min</div>
            <div class="scooter-actions">
                <button class="btn-abbrechen" onclick="cancelBooking()" ${isRiding ? 'disabled' : ''}>Abbrechen</button>
                <button class="btn-buchen" onclick="startBooking('${scooter.id}')" ${isRiding ? 'disabled style="background:#888;cursor:not-allowed;"' : ''}>
                    ${isRiding ? 'Aktive Fahrt' : 'Buchen'}
                </button>
            </div>
        </div>
    `;
}

function showRouteTo(scooterLat, scooterLon) {
    if (activeRoute) {
        map.removeControl(activeRoute);
        activeRoute = null;
    }

    if (!window.currentUserLat || !window.currentUserLon) {
        showWarning("Standort nicht verfügbar.");
        return;
    }

    activeRoute = L.Routing.control({
        waypoints: [
            L.latLng(window.currentUserLat, window.currentUserLon),
            L.latLng(scooterLat, scooterLon)
        ],
        routeWhileDragging: false,
        show: false,
        addWaypoints: false,
        draggableWaypoints: false,
        fitSelectedRoutes: true,
        lineOptions: {
            styles: [{ color: '#007aff', weight: 4, opacity: 0.8 }]
        },
        createMarker: () => null
    }).addTo(map);
}

function clearRoute() {
    if (activeRoute) {
        map.removeControl(activeRoute);
        activeRoute = null;
    }
}

function startBooking(scooterId) {
    const customerId = sessionStorage.getItem('customerId');
    if (!customerId) {
        showWarning("Bitte loggen Sie sich zuerst ein!");
        return;
    }

    if (sessionStorage.getItem('bookingId')) {
        const existing = document.getElementById('ride-dashboard');
        if (!existing) showRideDashboard();
        return;
    }

    fetch('http://localhost:8080/booking/start', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ customerId, scooterId })
    })
        .then(response => {
            if (!response.ok) return response.text().then(msg => { throw new Error(msg); });
            return response.json();
        })
        .then(booking => {
            activeBookingId = booking.bookingID;
            sessionStorage.setItem('bookingId', activeBookingId);
            clearRoute();
            showRideDashboard();
        })
        .catch(err => {
            const msg = err.message;
            if (msg.includes('Guthaben')) {
                showInsufficientFundsNotification(msg);
            } else {
                showError("Fehler beim Buchen: " + msg);
            }
        });
}

function showRideDashboard() {
    const old = document.getElementById('ride-dashboard');
    if (old) old.remove();

    const dashboard = document.createElement('div');
    dashboard.id = 'ride-dashboard';
    dashboard.innerHTML = `
        <div class="ride-dashboard">
            <div class="ride-title">🛴 Fahrt läuft</div>          
            <img src="../assets/fahrt.gif" alt="Fahrt Animation" class="ride-gif" />
            <div class="ride-timer" id="ride-timer">00:00</div>
            <div class="ride-price">Preis: <span id="ride-price">2.00 €</span></div>
            <button class="btn-end-ride" onclick="endRide()">Fahrt beenden</button>
        </div>
    `;
    document.body.appendChild(dashboard);

    rideSeconds = 0;
    rideTimer = setInterval(() => {
        rideSeconds++;
        const min = String(Math.floor(rideSeconds / 60)).padStart(2, '0');
        const sec = String(rideSeconds % 60).padStart(2, '0');
        document.getElementById('ride-timer').textContent = `${min}:${sec}`;
        const currentCost = 2.00 + (Math.floor(rideSeconds / 60) * 0.15);
        document.getElementById('ride-price').textContent = currentCost.toFixed(2) + ' €';
    }, 1000);
}

function endRide() {
    const bookingId = activeBookingId || sessionStorage.getItem('bookingId');
    if (!bookingId) return;

    clearInterval(rideTimer);

    fetch(`http://localhost:8080/booking/end?bookingId=${bookingId}`, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) return response.text().then(msg => { throw new Error(msg); });
            return response.json();
        })
        .then(booking => {
            sessionStorage.removeItem('bookingId');
            activeBookingId = null;

            const dashboard = document.getElementById('ride-dashboard');
            if (dashboard) dashboard.remove();

            showRideSummary(rideSeconds, booking.bookingPrice);
        })
        .catch(err => showError("Fehler beim Beenden: " + err.message));
}

function showRideSummary(seconds, finalPrice) {
    const minutes = Math.floor(seconds / 60);
    const displayPrice = finalPrice ? finalPrice.toFixed(2) : "0.00";

    const summary = document.createElement('div');
    summary.id = 'ride-summary';
    summary.innerHTML = `
        <div class="ride-summary">
            <div class="summary-title">✅ Fahrt beendet</div>
            <div class="summary-row"><span>Dauer</span><span>${minutes} Min ${seconds % 60} Sek</span></div>
            <div class="summary-row"><span>Preis</span><span>${displayPrice} €</span></div>
            <button class="btn-summary-close" onclick="closeSummary()">Schließen</button>
        </div>
    `;
    document.body.appendChild(summary);
}

function closeSummary() {
    const summary = document.getElementById('ride-summary');
    if (summary) {
        summary.style.opacity = '0';
        summary.style.transform = 'translateX(-50%) translateY(20px)';
        setTimeout(() => summary.remove(), 300);
    }
    document.querySelectorAll('.pin').forEach(pin => pin.classList.remove('selected'));
}

function cancelBooking() {
    document.querySelectorAll('.pin').forEach(pin => pin.classList.remove('selected'));
    clearRoute();
}

function showInsufficientFundsNotification(message) {
    const old = document.getElementById('funds-notification');
    if (old) old.remove();

    const customerId = sessionStorage.getItem('customerId');

    const notification = document.createElement('div');
    notification.id = 'funds-notification';
    notification.innerHTML = `
        <div class="funds-notification">
            <div class="funds-icon">💳</div>
            <div class="funds-title">Nicht genug Guthaben</div>
            <div class="funds-message">${message}</div>
            <div class="funds-actions">
                <button class="btn-deposit" onclick="depositMoney('${customerId}')">
                    + 10 € aufladen
                </button>
                <button class="btn-funds-close" onclick="closeFundsNotification()">
                    Schließen
                </button>
            </div>
        </div>
    `;
    document.body.appendChild(notification);
}

function depositMoney(customerId) {
    fetch(`http://localhost:8080/${customerId}/deposit?deposit=10`, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) return response.text().then(msg => { throw new Error(msg); });
            return response.text();
        })
        .then(result => {
            closeFundsNotification();
            showToast('✅ 10 € wurden aufgeladen!');
        })
        .catch(err => showError("Fehler: " + err.message));
}

function closeFundsNotification() {
    const notification = document.getElementById('funds-notification');
    if (notification) {
        notification.style.opacity = '0';
        notification.style.transform = 'translateX(-50%) translateY(20px)';
        setTimeout(() => notification.remove(), 300);
    }
}

function showToast(message) {
    const old = document.getElementById('toast');
    if (old) old.remove();

    const toast = document.createElement('div');
    toast.id = 'toast';
    toast.innerHTML = `<div class="toast">${message}</div>`;
    document.body.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}