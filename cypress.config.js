const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    baseUrl: "http://127.0.0.1:5500",  // Mover el puerto en caso de necesitarlo
    setupNodeEvents(on, config) {
      // Agregar eventos personalizados aquí
    },
  },
});

