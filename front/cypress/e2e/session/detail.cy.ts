describe('Session Detail - display info and buttons based on role', () => {
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

  const session = {
    id: 1,
    name: "Test",
    date: new Date(),
    teacher_id: 1,
    description: "A small description",
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  function interceptCommon() {
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/session', { body: [session] }).as('getSessions');
    cy.intercept('GET', '/api/session/1', { body: session }).as('getSession');
  }

  it('should display details and Participate button for a non-admin', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: { id: 2, username: 'nonAdminUser', admin: false }
    }).as('login');
    interceptCommon();

    cy.login('nonadmin@studio.com', 'test!1234');
    cy.wait('@getSessions');

    cy.contains('Test').parents('mat-card').first().within(() => {
      cy.get('button').contains("Detail").click();
    });
    cy.wait('@getSession');

    cy.url().should('include', '/sessions/detail/1');
    cy.contains('Test').should('be.visible');
    cy.contains('A small description').should('be.visible');
    cy.contains('0 attendees').should('be.visible');
    cy.contains('Create at:').should('be.visible');
    cy.contains('Last update:').should('be.visible');
    cy.contains('Participate').should('be.visible');
    cy.contains('Delete').should('not.exist');
  });

  it('should display details and Delete button for an admin', () => {
    interceptCommon();
    cy.loginAsAdmin();
    cy.wait('@getSessions');

    cy.contains('Test').parents('mat-card').first().within(() => {
      cy.get('button').contains("Detail").click();
    });
    cy.wait('@getSession');

    cy.url().should('include', '/sessions/detail/1');
    cy.contains('Test').should('be.visible');
    cy.contains('A small description').should('be.visible');
    cy.contains('0 attendees').should('be.visible');
    cy.contains('Create at:').should('be.visible');
    cy.contains('Last update:').should('be.visible');
    cy.contains('Delete').should('be.visible');
    cy.contains('Participate').should('not.exist');
  });

  it('should allow user to participate and then unparticipate from a session', () => {
    const sessionNotRegistered = {
      id: 1,
      name: "Test",
      date: new Date(),
      teacher_id: 1,
      description: "A small description",
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    };
    const sessionRegistered = {
      ...sessionNotRegistered,
      users: [2]
    };

    cy.intercept('POST', '/api/auth/login', {
      body: { id: 2, username: 'nonAdminUser', admin: false }
    }).as('login');
    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/session', { body: [sessionNotRegistered] }).as('getSessions');
    cy.intercept('GET', '/api/session/1', { body: sessionNotRegistered }).as('getSession');

    cy.login('nonadmin@studio.com', 'test!1234');
    cy.wait('@getSessions');

    cy.contains('Test').parents('mat-card').first().within(() => {
      cy.get('button').contains("Detail").click();
    });
    cy.wait('@getSession');

    cy.intercept('POST', '/api/session/1/participate/2', { statusCode: 200 }).as('participate');
    cy.intercept('GET', '/api/session/1', { body: sessionRegistered }).as('getSessionAfterParticipate');

    cy.get('button').contains('Participate').click();
    cy.wait('@participate');
    cy.wait('@getSessionAfterParticipate');
    cy.get('button').contains('Do not participate').should('be.visible');

    cy.intercept('DELETE', '/api/session/1/participate/2', { statusCode: 200 }).as('unparticipate');
    cy.intercept('GET', '/api/session/1', { body: sessionNotRegistered }).as('getSessionAfterUnparticipate');

    cy.get('button').contains('Do not participate').click();
    cy.wait('@unparticipate');
    cy.wait('@getSessionAfterUnparticipate');
    cy.get('button').contains('Participate').should('be.visible');
  });

  it('should allow going back with the back button', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      }
    }).as('login');
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [{
        id: 1,
        name: "Test",
        date: new Date(),
        teacher_id: 1,
        description: "Test description",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }]
    }).as('sessions');
    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: {
        id: 1,
        name: "Test",
        date: new Date(),
        teacher_id: 1,
        description: "Test description",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    }).as('sessionDetail');
    cy.intercept('GET', '/api/teacher/1', {
      statusCode: 200,
      body: {
        id: 1,
        lastName: "Doe",
        firstName: "John",
        createdAt: new Date(),
        updatedAt: new Date()
      }
    }).as('getTeacher1');

    cy.login('yoga@studio.com', 'test!1234');
    cy.url().should('include', '/sessions');

    cy.contains('Test').parents('mat-card').first().within(() => {
      cy.get('button').contains("Detail").click();
    });
    cy.wait('@sessionDetail');
    cy.wait('@getTeacher1');

    cy.get('button').find('mat-icon').contains('arrow_back').click();
    cy.url().should('include', '/sessions');
  });
});
