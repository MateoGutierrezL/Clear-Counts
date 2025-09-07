/// <reference types="cypress" />

describe('ClearCounts - Página principal', () => {
  it('Debe mostrar el título principal', () => {
    cy.visit('/index.html');
    cy.contains('h1', 'Controla tus finanzas con claridad y seguridad').should('be.visible');
  });

  it('La navegación debe tener todas las secciones', () => {
    const items = ['Inicio', 'Producto', 'Descargar', '¿Cómo funciona?', 'Desarrolladores', 'Compañía'];
    cy.visit('/index.html');
    items.forEach(text => {
      cy.get('.nav-menu').contains('a', text).should('exist');
    });
  });

  it('Botón Descargar debe registrar en consola', () => {
    cy.visit('/index.html', {
      onBeforeLoad(win) {
        cy.spy(win.console, 'log').as('consoleLog');
      }
    });

    cy.get('.download-btn', { timeout: 6000 }).should('be.visible').click();
    cy.get('@consoleLog').should('have.been.calledWithMatch', /Botón clickeado: Producto/);
  });

  it('Debe hacer scroll a la sección de Producto', () => {
    cy.visit('/index.html');
    cy.contains('a', 'Producto').click();
    cy.url().should('include', '#producto');
    cy.get('#producto').should('be.visible');
  });

  it('Simulador de Presupuesto debe calcular correctamente', () => {
    cy.visit('/index.html');
    cy.get('#ingresos').clear().type('1423500');
    cy.get('#egresos').clear().type('900000');
    cy.get('#meta').clear().type('500000');
    cy.contains('button', 'Calcular').click();

    cy.get('#resultado', { timeout: 6000 }).should('be.visible');

    cy.get('#resultado').invoke('text').then(text => {
      expect(text).to.include('✅ Ingresos');
      expect(text).to.include('❌ Egresos');
      expect(text).to.include('📌 Disponible');
      expect(text).to.include('Ahorrar');
      expect(text).to.include('Gasto libre');
      expect(text).to.match(/en \d+ meses/); 
    });
  });

  it('Inputs deben dar formato con comas al escribir números', () => {
    cy.visit('/index.html');
    cy.get('#ingresos').clear().type('1000000');
    cy.get('#ingresos').should('have.value', '1,000,000');
  });

  it('Footer debe contener correo de soporte y redes sociales', () => {
    cy.visit('/index.html');
    cy.get('footer').within(() => {
      cy.contains('soporte@clearcounts.com').should('be.visible');
      ['Facebook', 'Instagram', 'LinkedIn', 'TikTok'].forEach(red => {
        cy.contains(red).should('exist');
      });
    });
  });
});
