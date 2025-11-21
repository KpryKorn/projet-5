describe('Session editing by admin', () => {
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

  const sessionBefore = {
    id: 1,
    name: "Morning Yoga",
    date: new Date(),
    teacher_id: 1,
    description: "Gentle session",
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const sessionAfter = {
    ...sessionBefore,
    name: "Modified Yoga"
  };

  beforeEach(() => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'admin',
        email: 'yoga@studio.com',
        admin: true,
        token: 'fake-jwt-token'
      }
    }).as('loginRequest');

    cy.intercept('GET', '/api/auth/me', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'admin',
        email: 'yoga@studio.com',
        admin: true
      }
    }).as('me');

    cy.intercept('GET', '/api/teacher', { body: teachers }).as('getTeachers');
    cy.intercept('GET', '/api/session', { body: [sessionBefore] }).as('getSessions');
    cy.intercept('GET', '/api/session/1', { body: sessionBefore }).as('getSessionDetail');
  });

  it('should allow admin to edit a session successfully', () => {
    cy.loginAsAdmin();
    cy.wait('@getSessions');

    cy.contains('Morning Yoga').parents('mat-card').first().within(() => {
      cy.get('button').contains('Edit').click();
    });
    cy.wait('@getSessionDetail');
    cy.url().should('include', '/sessions/update/1');

    cy.get('input[formControlName=name]').clear().type('Modified Yoga');

    cy.intercept('PUT', '/api/session/1', {
      statusCode: 200,
      body: sessionAfter
    }).as('updateSession');

    cy.intercept('GET', '/api/session', { body: [sessionAfter] }).as('getSessionsAfterUpdate');

    cy.get('button[type=submit]').click();
    cy.wait('@updateSession');
    cy.wait('@getSessionsAfterUpdate');

    cy.url().should('include', '/sessions');
    cy.contains('Session updated !').should('be.visible');
    cy.contains('Modified Yoga').should('be.visible');
  });

  it('should keep Save button disabled if a required field is missing', () => {
    cy.loginAsAdmin();
    cy.wait('@getSessions');

    cy.contains('Morning Yoga').parents('mat-card').first().within(() => {
      cy.get('button').contains('Edit').click();
    });
    cy.wait('@getSessionDetail');
    cy.url().should('include', '/sessions/update/1');

    cy.get('input[formControlName=name]').clear();
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=name]').type('Morning Yoga');
    cy.get('input[formControlName=date]').clear();
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=date]').type('2025-06-01');
    cy.get('textarea[formControlName=description]').clear();
    cy.get('button[type=submit]').should('be.disabled');
  });
});
