window.onload = function() {

    // Login Button (nur auf index.html vorhanden)
    const btnLogin = document.getElementById("btnLogin");
    if (btnLogin) {
        btnLogin.addEventListener("click", function() {
            alert("Anmelden clicked!");
        });
    }

    // Einstellungen (nur auf index.html vorhanden)
    const settings = document.getElementById("settings");
    if (settings) {
        settings.addEventListener("click", function() {
            alert("Einstellungen geöffnet!");
        });
    }

    // Weiterleitung zum createAccount Fenster
    const btnCreate = document.getElementById("btnCreate");
    if (btnCreate) {
        btnCreate.addEventListener("click", function() {
            window.location.href = "createAccount.html";
        });
    }

    // Konto erstellen Button (nur auf createAccount.html vorhanden)
    const btn = document.getElementById("btnCreateAccount");
    if (btn) {
        btn.addEventListener("click", function() {
            const foreName     = document.getElementById("ForeName").value;
            const name         = document.getElementById("Name").value;
            const street       = document.getElementById("Street").value;
            const streetNumber = document.getElementById("StreetNumber").value;
            const location     = document.getElementById("Location").value;
            const plz          = document.getElementById("Plz").value;
            const email        = document.getElementById("Email").value;

            alert("Account wird erstellt!");
            console.log(foreName, name, street, streetNumber, location, plz, email);
        });
    }

}