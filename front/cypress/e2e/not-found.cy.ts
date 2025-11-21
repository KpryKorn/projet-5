describe('Page Not Found', () => {
  it('should display 404 page for unknown URL', () => {
    cy.visit('/unknown-page', { failOnStatusCode: false });
    cy.contains('Page not found !').should('be.visible');
    cy.contains(/not found/i).should('be.visible');
  });
 
  it('should redirect to /login when session expires', () => {
    cy.loginAsUser();
    cy.window().then(win => win.localStorage.removeItem('sessionInformation'));
    cy.visit('/sessions');
    cy.url().should('include', '/login');
  });
});

describe('Forbidden access to session creation for non-admin', () => {
  beforeEach(() => {
    cy.loginAsUser();
  });

  it('should redirect to /login when non-admin tries to access /sessions/create', () => {
    cy.visit('/sessions/create');
    cy.url().should('include', '/login');
    cy.contains(/access denied|unauthorized|login/i).should('be.visible');
  });
});