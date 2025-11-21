describe('Delete Session', () => {
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

  it('should allow admin to delete a session and display only one remaining', () => {
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/teacher/1', { body: teachers[0] }).as('getTeacher1');
    cy.intercept('GET', '/api/teacher/2', { body: teachers[1] }).as('getTeacher2');

    const sessionsBefore = [
      {
        id: 1,
        name: "A session name",
        date: new Date(),
        teacher_id: 1,
        description: "A small description",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        id: 2,
        name: "Another session",
        date: new Date(),
        teacher_id: 2,
        description: "Another description",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ];
    cy.intercept('GET', '/api/session', { body: sessionsBefore }).as('getSessions');

    cy.intercept('GET', '/api/session/1', {
      body: sessionsBefore[0]
    }).as('getSession');

    cy.intercept('DELETE', '/api/session/1', { statusCode: 200 }).as('deleteSession');

    const sessionsAfter = [
      {
        id: 2,
        name: "Another session",
        date: new Date(),
        teacher_id: 2,
        description: "Another description",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ];

    cy.loginAsAdmin();
    cy.wait('@getSessions');
    cy.wait(400);

    cy.contains('A session name').parents('mat-card').first().within(() => {
      cy.get('button').contains("Detail").click();
    });
    cy.wait('@getSession');
    cy.wait(400);

    cy.intercept('GET', '/api/session', { body: sessionsAfter }).as('getSessionsAfterDelete');

    cy.get('button span').contains("Delete").click();
    cy.wait('@deleteSession');
    cy.wait('@getSessionsAfterDelete');
    cy.wait(400);

    cy.url().should('include', '/sessions');
    cy.contains('Session deleted !').should('be.visible');
    cy.contains('Another session').should('be.visible');
  });
});
