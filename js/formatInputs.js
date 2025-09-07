// js/formatInputs.js
document.addEventListener("DOMContentLoaded", () => {
  const numericInputs = document.querySelectorAll("input[type='number']");

  numericInputs.forEach(input => {
    // Evita ingresar valores negativos
    input.addEventListener("input", () => {
      if (input.value < 0) input.value = "";
    });

    // Formatear al perder el foco (separadores de miles)
    input.addEventListener("blur", () => {
      let value = input.value.replace(/,/g, ""); // quita comas previas
      if (value && !isNaN(value)) {
        input.value = new Intl.NumberFormat("es-CO").format(value);
      }
    });

    // Quitar formato cuando el campo gana foco (para poder editar)
    input.addEventListener("focus", () => {
      input.value = input.value.replace(/\./g, "").replace(/,/g, "");
    });
  });
});
