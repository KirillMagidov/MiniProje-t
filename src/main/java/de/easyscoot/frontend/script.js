

// Login Button
document.getElementById("btnLogin").addEventListener("click", function() {
    alert("Anmelden clicked!");
});

// Einstellungen (Zahnrad)
document.getElementById("settings").addEventListener("click", function() {
    alert("Einstellungen geöffnet!");
});

//Weiterleitung zum LogIn Fenster
document.getElementById("btnCreate").addEventListener("click", function () {
    window.location.href = "createAccount.html";
});
``