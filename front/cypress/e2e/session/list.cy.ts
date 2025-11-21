describe('Session list', () => {
  const teachers = [
    {
      id: 1,
      lastName: "Doe",
      firstName: "John",
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id: 2,
      lastName: "Dupont",
      firstName: "Louis",
      createdAt: new Date(),
      updatedAt: new Date(),
    }
  ];

  const sessions = [
    {
      id: 1,
      name: "Morning Yoga",
      date: new Date(),
      teacher_id: 1,
      description: "Morning session",
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      name: "Evening Pilates",
      date: new Date(),
      teacher_id: 2,
      description: "Evening session",
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  it('should display the list of sessions for an admin', () => {
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/session', { body: sessions }).as('getSessions');
    cy.loginAsAdmin();
    cy.wait('@getSessions');
    cy.wait(400);
    cy.contains('Morning Yoga').should('be.visible');
    cy.contains('Evening Pilates').should('be.visible');
  });

  it('should display the list of sessions for a non-admin', () => {
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/session', { body: sessions }).as('getSessions');
    cy.loginAsUser();
    cy.wait('@getSessions');
    cy.wait(400);
    cy.contains('Morning Yoga').should('be.visible');
    cy.contains('Evening Pilates').should('be.visible');
  });
});
