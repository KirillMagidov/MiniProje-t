window.onload = function () {

    // Login Button (nur auf index.html vorhanden)
    const btnLogin = document.getElementById("btnLogin");
    if (btnLogin) {
        btnLogin.addEventListener("click", function () {
            window.location.href = "logIn.html";
        });
    }

    // Einstellungen (nur auf index.html vorhanden)
    const settings = document.getElementById("settings");
    if (settings) {
        settings.addEventListener("click", function () {
            alert("Einstellungen geöffnet!");
        });
    }

    // Weiterleitung zum createAccount Fenster
    const btnCreate = document.getElementById("btnCreate");
    if (btnCreate) {
        btnCreate.addEventListener("click", function () {
            window.location.href = "createAccount.html";
        });
    }

    // Account Speicherung
    const accountForm = document.getElementById("accountForm");
    if (accountForm) {
        accountForm.addEventListener("submit", function (event) {

            event.preventDefault();

            const foreName = document.getElementById("ForeName").value;
            const name = document.getElementById("Name").value;
            const street = document.getElementById("Street").value;
            const streetNumber = parseInt(document.getElementById("StreetNumber").value);
            const location = document.getElementById("Location").value;
            const plz = parseInt(document.getElementById("Plz").value);
            const email = document.getElementById("Email").value;
            const password = document.getElementById("Password").value;
            const repeatPassword = document.getElementById("RepeatPassword").value;

            if (password !== repeatPassword) {
                alert("Password is not the same");
                return;
            }

            const data = {
                foreName: foreName,
                name: name,
                street: street,
                streetNumber: streetNumber,
                location: location,
                plz: plz,
                email: email,
                password: password,
            };

            fetch("http://localhost:8080/createAccount", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (response.ok) {
                        window.location.href = "availability.html";
                    } else {
                        return response.text().then(msg => alert(msg));
                    }
                })
                .catch(error => alert("Verbindungsfehler: " + error));
        });
    }
}

// Map
if (document.getElementById("map")) {
    const map = L.map('map').setView([52.3759, 9.7320], 13);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);
}