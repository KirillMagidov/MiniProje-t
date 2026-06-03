// Slider-Logik
const slider       = document.getElementById('slider');
const panelLogin   = document.getElementById('panel-login');
const panelReg     = document.getElementById('panel-register');
const toReg        = document.getElementById('slider-to-register');
const toLogin      = document.getElementById('slider-to-login');
const btnShowReg   = document.getElementById('btn-show-register');
const btnShowLogin = document.getElementById('btn-show-login');

btnShowReg.addEventListener('click', () => {
    slider.classList.add('slide-right');
    panelLogin.classList.remove('move-left');
    panelReg.classList.add('move-right');
    toReg.style.display   = 'none';
    toLogin.style.display = 'block';
});

btnShowLogin.addEventListener('click', () => {
    slider.classList.remove('slide-right');
    panelLogin.classList.add('move-left');
    panelReg.classList.remove('move-right');
    toReg.style.display   = 'block';
    toLogin.style.display = 'none';
});

document.getElementById('btn-back-home').addEventListener('click', () => {
    window.location.href = 'index.html';
});

const params = new URLSearchParams(window.location.search);
if (params.get('mode') === 'register') {
    btnShowReg.click();
} else {
    panelLogin.classList.add('move-left');
}

// Register-Formular
const accountForm = document.getElementById("accountForm");
if (accountForm) {
    accountForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const foreName     = document.getElementById("ForeName").value;
        const name         = document.getElementById("Name").value;
        const street       = document.getElementById("Street").value;
        const streetNumber = parseInt(document.getElementById("StreetNumber").value);
        const location     = document.getElementById("Location").value;
        const plz          = parseInt(document.getElementById("Plz").value);
        const email        = document.getElementById("Email").value;
        const password     = document.getElementById("Password").value;
        const repeatPassword = document.getElementById("RepeatPassword").value;

        if (password !== repeatPassword) {
            alert("Passwörter stimmen nicht überein!");
            return;
        }

        const data = { foreName, name, street, streetNumber, location, plz, email, password };

        fetch("http://localhost:8080/createAccount", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (response.ok) {
                    alert("Konto erfolgreich erstellt! Bitte melde dich an.");
                    btnShowLogin.click();
                } else {
                    return response.text().then(msg => alert(msg));
                }
            })
            .catch(error => alert("Verbindungsfehler: " + error));
    });
}

// Login-Formular
const loginForm = document.getElementById("loginForm");
if (loginForm) {
    loginForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const email    = document.getElementById("LoginEmail").value;
        const password = document.getElementById("LoginPassword").value;

        fetch("http://localhost:8080/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        })
            .then(response => {
                if (response.ok) {
                    return response.text().then(customerId => {
                        sessionStorage.setItem('customerId', customerId);
                        window.location.href = "availability.html";
                    });
                } else {
                    return response.text().then(msg => {
                        alert(msg);
                        throw new Error(msg);
                    });
                }
            })
            .catch(error => {
                console.error("Fehler beim Login:", error);
            });
    });
}