const customerId = sessionStorage.getItem('customerId');
let editing = false;
let pendingAction = null;
let authCredentials = { email: '', password: '' };



fetch(`http://localhost:8080/getCustomer?customerId=${customerId}`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' }
})
    .then(response => response.json())
    .then(customer => {
        document.getElementById('foreName').value     = customer.foreName || '';
        document.getElementById('name').value         = customer.name || '';
        document.getElementById('email').value        = customer.email || '';
        document.getElementById('street').value       = customer.street || '';
        document.getElementById('streetNumber').value = customer.streetNumber || '';
        document.getElementById('plz').value          = customer.plz || '';
        document.getElementById('location').value     = customer.location || '';

        const avatarEl = document.querySelector('.avatar');
        const nameEl   = document.querySelector('.left-name');
        if (avatarEl && customer.foreName && customer.name) {
            avatarEl.textContent = customer.foreName[0] + customer.name[0];
        }
        if (nameEl && customer.foreName && customer.name) {
            nameEl.textContent = `${customer.foreName} ${customer.name}`;
        }
    })
    .catch(() => alert('Kundendaten konnten nicht geladen werden.'));

function openPopup(action) {
    pendingAction = action;
    const title   = document.getElementById('popupTitle');
    const sub     = document.getElementById('popupSub');
    const confirm = document.getElementById('popupConfirm');

    if (action === 'delete') {
        title.textContent   = 'Konto löschen';
        sub.textContent     = 'Bitte bestätige deine Identität. Diese Aktion kann nicht rückgängig gemacht werden.';
        confirm.textContent = 'Konto löschen';
        confirm.className   = 'popup-confirm danger-confirm';
    } else {
        title.textContent   = 'Identität bestätigen';
        sub.textContent     = 'Bitte gib deine Zugangsdaten ein um Änderungen vorzunehmen.';
        confirm.textContent = 'Bestätigen';
        confirm.className   = 'popup-confirm';
    }

    document.getElementById('authEmail').value          = '';
    document.getElementById('authPassword').value       = '';
    document.getElementById('popupError').style.display = 'none';
    document.getElementById('popupOverlay').classList.add('open');
    setTimeout(() => document.getElementById('authEmail').focus(), 50);
}

function closePopup() {
    document.getElementById('popupOverlay').classList.remove('open');
    pendingAction = null;
}

const saveBtn = document.getElementById("saveBtn");
if (saveBtn) {
    saveBtn.addEventListener("click", function () {
        const foreName       = document.getElementById("foreName").value;
        const name           = document.getElementById("name").value;
        const street         = document.getElementById("street").value;
        const streetNumber   = parseInt(document.getElementById("streetNumber").value);
        const location       = document.getElementById("location").value;
        const plz            = parseInt(document.getElementById("plz").value);
        const email          = document.getElementById("email").value;
        const newPassword    = document.getElementById("password").value;
        const repeatPassword = document.getElementById("repeatPassword").value;

        if (newPassword && newPassword !== repeatPassword) {
            alert('Passwörter stimmen nicht überein!');
            return;
        }

        if (newPassword && newPassword.length < 8) {
            alert('Das Passwort muss mindestens 8 Zeichen lang sein.');
            return;
        }

        const passwordToSend = newPassword || authCredentials.password;

        fetch(`http://localhost:8080/updateAccount?email=${authCredentials.email}&password=${authCredentials.password}&customerId=${customerId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ foreName, name, street, streetNumber, location, plz, email, password: passwordToSend })
        })
            .then(res => {
                if (res.ok) {
                    const avatarEl = document.querySelector('.avatar');
                    const nameEl   = document.querySelector('.left-name');
                    if (avatarEl) avatarEl.textContent = foreName[0] + name[0];
                    if (nameEl)   nameEl.textContent   = `${foreName} ${name}`;
                    alert('Änderungen gespeichert!');
                    toggleEdit();
                } else {
                    return res.text().then(msg => alert(msg));
                }
            })
            .catch(() => alert("Fehler beim Ändern der Daten!"));
    });
}

function confirmAuth() {
    const email    = document.getElementById('authEmail').value.trim();
    const password = document.getElementById('authPassword').value;
    const error    = document.getElementById('popupError');

    fetch('http://localhost:8080/verify', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    })
        .then(res => {
            if (!res.ok) {
                error.style.display = 'block';
                return;
            }
            authCredentials = { email, password };
            const action = pendingAction;
            closePopup();
            if (action === 'edit') {
                enableEditing();
            } else if (action === 'delete') {
                fetch(`http://localhost:8080/deleteAccount?email=${email}&password=${password}&customerId=${customerId}`, {
                    method: 'DELETE'
                })
                    .then(res => {
                        if (res.ok) {
                            sessionStorage.clear();
                            window.location.href = 'index.html';
                        } else {
                            return res.text().then(msg => alert(msg));
                        }
                    })
                    .catch(() => alert("Fehler beim Löschen des Kontos!"));
            }
        });
}

function enableEditing() {
    editing = true;
    const inputs  = document.querySelectorAll('.right input');
    const btn     = document.getElementById('editBtn');
    const saveBtn = document.getElementById('saveBtn');

    inputs.forEach(input => input.removeAttribute('readonly'));
    btn.textContent       = 'Abbrechen';
    btn.style.borderColor = 'rgba(192,57,43,0.5)';
    btn.style.color       = 'rgba(255,80,60,0.85)';
    saveBtn.style.display = 'block';

    const repeatField = document.getElementById('repeatPasswordField');
    repeatField.style.maxHeight = '80px';
    repeatField.style.opacity   = '1';
    document.getElementById('repeatPassword').removeAttribute('readonly');
}

function toggleEdit() {
    if (!editing) {
        openPopup('edit');
    } else {
        editing = false;
        const inputs  = document.querySelectorAll('.right input');
        const btn     = document.getElementById('editBtn');
        const saveBtn = document.getElementById('saveBtn');

        inputs.forEach(input => input.setAttribute('readonly', true));
        btn.textContent       = 'Änderung eingeben';
        btn.style.borderColor = '';
        btn.style.color       = '';
        saveBtn.style.display = 'none';

        document.getElementById('password').value = '';
        document.getElementById('repeatPassword').value = '';

        const repeatField = document.getElementById('repeatPasswordField');
        repeatField.style.maxHeight = '0';
        repeatField.style.opacity   = '0';
        document.getElementById('repeatPassword').setAttribute('readonly', true);
    }
}

function confirmDelete() {
    openPopup('delete');
}

document.addEventListener('keydown', function (e) {
    if (e.key === 'Enter' && document.getElementById('popupOverlay').classList.contains('open')) {
        confirmAuth();
    }
});

const logoutBtn2 = document.getElementById("logoutBtn2");
if (logoutBtn2) {
    logoutBtn2.addEventListener("click", function () {
        sessionStorage.clear();
        window.location.href = "index.html";
    });
}