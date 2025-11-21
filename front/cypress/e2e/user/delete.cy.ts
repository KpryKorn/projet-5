describe('User deletion', () => {
  const fakeSession = {
    id: 2,
    name: "Evening Pilates",
    date: new Date(),
    teacher_id: 2,
    description: "A gentle class to end the day relaxed.",
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const fakeUser = {
    id: 2,
    firstName: 'Alice',
    lastName: 'Martin',
    email: 'alice.martin@example.org',
    admin: false,
    createdAt: '2024-02-10T09:00:00Z',
    updatedAt: '2024-05-10T09:00:00Z'
  };

  beforeEach(() => {
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [fakeSession]
    }).as('getSessions');

    cy.intercept('GET', '/api/user/2', {
      statusCode: 200,
      body: fakeUser
    }).as('getUser');

    cy.intercept('DELETE', '/api/user/2', {
      statusCode: 200,
      body: { message: 'User deleted successfully' }
    }).as('deleteUser');
  });

  it('should allow logged-in user to view their sessions, go to /me and see the delete button', () => {
    cy.loginAsUser();
    cy.url().should('include', '/sessions');
    cy.wait('@getSessions');
    cy.contains(fakeSession.name);
    cy.contains(fakeSession.description);
    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me');
    cy.wait('@getUser');
    cy.contains('Name: Alice MARTIN');
    cy.contains('alice.martin@example.org');
    cy.contains('Delete my account:');
    cy.get('button').contains(/delete/i).should('be.visible');
    cy.get('button').contains(/delete/i).click();
    cy.wait('@deleteUser');
    cy.contains('Your account has been deleted!').should('be.visible');
  });
});
