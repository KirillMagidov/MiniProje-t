let activeBookingId = null;
let rideTimer = null;
let rideSeconds = 0;

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
    const isRiding = !!localStorage.getItem('bookingId');
    return `
        <div class="scooter-popup">
            <div class="scooter-header">
                <span>${scooter.marke} ${scooter.modell}</span>
                <span>🔋 ${scooter.ladezustand}%</span>
            </div>
            <div class="scooter-id">ID: ${scooter.id}</div>
            <div class="scooter-price">0.15 € / Min</div>
            <div class="scooter-actions">
                <button class="btn-abbrechen" onclick="cancelBooking()" ${isRiding ? 'disabled' : ''}>Abbrechen</button>
                <button class="btn-buchen" onclick="startBooking('${scooter.id}')" ${isRiding ? 'disabled style="background:#888;cursor:not-allowed;"' : ''}>
                    ${isRiding ? 'Aktive Fahrt' : 'Buchen'}
                </button>
            </div>
        </div>
    `;
}

function startBooking(scooterId) {
    const customerId = localStorage.getItem('customerId');
    if (!customerId) {
        alert("Bitte loggen Sie sich zuerst ein!");
        return;
    }

    // Wenn schon eine Fahrt aktiv ist, Dashboard anzeigen
    if (localStorage.getItem('bookingId')) {
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
            localStorage.setItem('bookingId', activeBookingId);
            showRideDashboard();
        })
        .catch(err => alert("Fehler beim Buchen: " + err.message));
}

function showRideDashboard() {
    // Entferne altes Dashboard falls vorhanden
    const old = document.getElementById('ride-dashboard');
    if (old) old.remove();

    const dashboard = document.createElement('div');
    dashboard.id = 'ride-dashboard';
    dashboard.innerHTML = `
        <div class="ride-dashboard">
            <div class="ride-title">🛴 Fahrt läuft</div>
            <div class="ride-timer" id="ride-timer">00:00</div>
            <div class="ride-price">Preis: <span id="ride-price">0.00 €</span></div>
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
        document.getElementById('ride-price').textContent =
            (Math.floor(rideSeconds / 60) * 0.15).toFixed(2) + ' €';
    }, 1000);
}

function endRide() {
    const bookingId = activeBookingId || localStorage.getItem('bookingId');
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
            localStorage.removeItem('bookingId');
            activeBookingId = null;

            // Dashboard entfernen
            const dashboard = document.getElementById('ride-dashboard');
            if (dashboard) dashboard.remove();

            // Zusammenfassung anzeigen
            showRideSummary(rideSeconds);
        })
        .catch(err => alert("Fehler beim Beenden: " + err.message));
}

function showRideSummary(seconds) {
    const minutes = Math.floor(seconds / 60);
    const price = Math.max(minutes * 0.15, 1.00).toFixed(2);

    const summary = document.createElement('div');
    summary.id = 'ride-summary';
    summary.innerHTML = `
        <div class="ride-summary">
            <div class="summary-title">✅ Fahrt beendet</div>
            <div class="summary-row"><span>Dauer</span><span>${minutes} Min ${seconds % 60} Sek</span></div>
            <div class="summary-row"><span>Preis</span><span>${price} €</span></div>
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
}