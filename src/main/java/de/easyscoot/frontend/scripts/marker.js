/**
 * Генерирует HTML для маркера-точки
 * Эту функцию Бао использует при добавлении маркера на карту
 */
function getMarkerHTML() {
    return `
        <div class="marker-wrapper" onclick="selectMarker(this)">
            <div class="pin"></div>
            <div class="pulse"></div>
        </div>
    `;
}

/**
 * Меняет цвет маркера при клике (оставляет зеленым только один)
 */
function selectMarker(clickedElement) {
    // Находим все пины на странице и убираем выделение
    document.querySelectorAll('.pin').forEach(pin => pin.classList.remove('selected'));

    // Выделяем тот, на который только что кликнули
    const pinInside = clickedElement.querySelector('.pin');
    if (pinInside) {
        pinInside.classList.add('selected');
    }
}

/**
 * Генерирует HTML для всплывающего окна (по твоему эскизу)
 * @param {Object} scooter - Объект самоката из твоего бэкенда (JSON)
 */
function getPopupHTML(scooter) {
    return `
        <div class="scooter-popup">
            <div class="scooter-header">
                <span>${scooter.marke} ${scooter.modell}</span>
                <span>🔋 ${scooter.ladezustand}%</span>
            </div>
            <div class="scooter-id">ID: ${scooter.id}</div>
            
            <div class="scooter-price">
                0.15 € / Min
            </div>
            
            <div class="scooter-actions">
                <button class="btn-abbrechen" onclick="cancelBooking()">Abbrechen</button>
                <button class="btn-buchen" onclick="startBooking('${scooter.id}')">Buchen</button>
            </div>
        </div>
    `;
}

/**
 * Логика кнопки "Buchen"
 * @param {String} scooterId
 */
function startBooking(scooterId) {
    const customerId = localStorage.getItem('customerId');

    if (!customerId) {
        alert("Bitte loggen Sie sich zuerst ein!");
        return;
    }

    console.log(`Отправляем POST запрос к бэкенду: начать поездку для скутера ${scooterId}`);

    // Заготовка для вызова твоего BookingController (мы её допишем чуть позже)
    /*
    fetch(`http://localhost:8080/bookings/start?customerId=${customerId}&scooterId=${scooterId}`, {
        method: 'POST'
    })
    .then(response => response.json())
    .then(bookingData => {
        // Здесь мы запустим таймер (Dashboard)
    });
    */
}

/**
 * Логика кнопки "Abbrechen"
 */
function cancelBooking() {
    console.log("Пользователь отменил выбор.");
    // Убираем зеленое выделение со всех маркеров
    document.querySelectorAll('.pin').forEach(pin => pin.classList.remove('selected'));

    // Закрытие самого окна зависит от того, как Бао настроит карту (в Leaflet это map.closePopup())
    // Этот момент он добавит в своем map.js
}