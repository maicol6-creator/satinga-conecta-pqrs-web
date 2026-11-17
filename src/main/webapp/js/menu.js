/* Satinga Conecta · menú lateral en celular (mismo comportamiento del front-end) */
(function () {
  "use strict";
  var tog = document.querySelector(".side-toggle");
  var enlaces = document.getElementById("sideLinks");
  if (!tog || !enlaces) return;
  tog.addEventListener("click", function () {
    var abierto = enlaces.classList.toggle("open");
    tog.setAttribute("aria-expanded", abierto ? "true" : "false");
  });
  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape" && enlaces.classList.contains("open")) {
      enlaces.classList.remove("open");
      tog.setAttribute("aria-expanded", "false");
      tog.focus();
    }
  });
})();
