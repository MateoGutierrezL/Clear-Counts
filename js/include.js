
document.addEventListener("DOMContentLoaded", function () {
  const elements = document.querySelectorAll("[include-html]");

  elements.forEach(el => {
    const file = el.getAttribute("include-html");

    if (file) {
      fetch(file)
        .then(response => {
          if (!response.ok) throw new Error(`No se pudo cargar: ${file}`);
          return response.text();
        })
        .then(data => {
          el.innerHTML = data;
          el.removeAttribute("include-html");

          // Re-ejecuta include.js en caso de includes anidados
          if (el.querySelector("[include-html]")) {
            document.dispatchEvent(new Event("DOMContentLoaded"));
          }
        })
        .catch(error => {
          el.innerHTML = `<p style="color:red">❌ Error cargando ${file}</p>`;
          console.error(error);
        });
    }
  });
});
