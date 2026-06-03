window.onload = function () {

    const btnLogin = document.getElementById("btnLogin");
    if (btnLogin) {
        btnLogin.addEventListener("click", function () {
            window.location.href = "auth.html?mode=login";
        });
    }

    const settings = document.getElementById("settings");
    if (settings) {
        settings.addEventListener("click", function () {
            showInfo("Einstellungen geöffnet!");
        });
    }

    const btnCreate = document.getElementById("btnCreate");
    if (btnCreate) {
        btnCreate.addEventListener("click", function () {
            window.location.href = "auth.html?mode=register";
        });
    }
}