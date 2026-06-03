function showNotification(type, title, message) {
    const old = document.getElementById('app-notification');
    if (old) old.remove();

    const icons = {
        error:   '❌',
        success: '✅',
        warning: '⚠️',
        info:    'ℹ️'
    };

    const colors = {
        error:   '#c0392b',
        success: '#00d26a',
        warning: '#f39c12',
        info:    '#007aff'
    };

    const notification = document.createElement('div');
    notification.id = 'app-notification';
    notification.innerHTML = `
        <div class="app-notification app-notification--${type}">
            <div class="app-notification__icon">${icons[type] || 'ℹ️'}</div>
            <div class="app-notification__content">
                <div class="app-notification__title">${title}</div>
                <div class="app-notification__message">${message}</div>
            </div>
            <button class="app-notification__close" onclick="closeNotification()">✕</button>
        </div>
    `;
    document.body.appendChild(notification);

    setTimeout(() => {
        const el = document.getElementById('app-notification');
        if (el) {
            el.style.opacity = '0';
            el.style.transform = 'translateX(120%)';
            setTimeout(() => el.remove(), 400);
        }
    }, 5000);
}

function closeNotification() {
    const el = document.getElementById('app-notification');
    if (el) {
        el.style.opacity = '0';
        el.style.transform = 'translateX(120%)';
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