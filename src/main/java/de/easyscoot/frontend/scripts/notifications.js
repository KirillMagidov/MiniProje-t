function showNotification(type, title, message) {
    const old = document.getElementById('app-notification');
    if (old) old.remove();

    const icons = {
        error: '<i class="ti ti-circle-x notification-icon-animated"></i>',
        success: '<i class="ti ti-circle-check notification-icon-animated"></i>',
        warning: '<i class="ti ti-alert-triangle notification-icon-animated"></i>',
        info: '<i class="ti ti-info-circle notification-icon-animated"></i>'
    };

    const notification = document.createElement('div');
    notification.id = 'app-notification';
    notification.className = `custom-alert alert-${type}`;

    // Вставляем HTML-структуру
    notification.innerHTML = `
        <div class="custom-alert-icon">
            ${icons[type] || icons.info}
        </div>
        <div class="custom-alert-content">
            <div class="custom-alert-title">${title}</div>
            <div class="custom-alert-message">${message}</div>
        </div>
        <button class="custom-alert-close" onclick="closeNotification()">
            <i class="ti ti-x"></i>
        </button>
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        closeNotification();
    }, 5000);
}

function closeNotification() {
    const el = document.getElementById('app-notification');
    if (el) {
        el.classList.add('hide');

        setTimeout(() => el.remove(), 400);
    }
}

function showSuccess(message) {
    showNotification('success', 'Erfolgreich', message);
}

function showError(message) {
    showNotification('error', 'Fehler', message);
}

function showWarning(message) {
    showNotification('warning', 'Hinweis', message);
}

function showInfo(message) {
    showNotification('info', 'Info', message);
}