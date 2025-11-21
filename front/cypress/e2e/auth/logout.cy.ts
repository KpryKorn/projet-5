describe('Logout', () => {
  beforeEach(() => {
    cy.login('yoga@studio.com', 'test!1234');
  });

  it('should log out the user correctly', () => {
    cy.contains('Logout').should('be.visible').click();
    cy.url().should('match', /\/($|login)/);
    cy.contains('Logout').should('not.exist');
    cy.window().then((win) => {
      expect(win.localStorage.getItem('sessionInformation')).to.be.null;
    });
    cy.visit('/sessions');
    cy.url().should('include', '/login');
  });
});
