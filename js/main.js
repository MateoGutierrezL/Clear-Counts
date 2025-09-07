document.addEventListener("DOMContentLoaded", () => {
  // Scroll suave para anclas 
  document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener("click", function (e) {
      const targetId = this.getAttribute("href");

      if (targetId.length > 1) {
        e.preventDefault();
        document.querySelector(targetId).scrollIntoView({
          behavior: "smooth"
        });
      }
    });
  });

  // Resaltar sección activa en el navbar
  const sections = document.querySelectorAll("section");
  const navLinks = document.querySelectorAll("nav a");

  window.addEventListener("scroll", () => {
    let current = "";

    sections.forEach(section => {
      const sectionTop = section.offsetTop - 80;
      if (pageYOffset >= sectionTop) {
        current = section.getAttribute("id");
      }
    });

    navLinks.forEach(link => {
      link.classList.remove("active");
      if (link.getAttribute("href") === `#${current}`) {
        link.classList.add("active");
      }
    });
  });

  // Toggle menú móvil 
  const toggleBtn = document.querySelector(".menu-toggle");
  const navMenu = document.querySelector(".nav-links");

  if (toggleBtn && navMenu) {
    toggleBtn.addEventListener("click", () => {
      navMenu.classList.toggle("active");
    });
  }

  // Evento para botón de descarga (dinámico)
  setTimeout(() => {
    const downloadBtn = document.querySelector('.download-btn');
    if (downloadBtn) {
      downloadBtn.addEventListener('click', () => {
        console.log('Botón clickeado: Producto');
      });
    }
  }, 100); // Ajuste para pruebas con Cypress
});
