describe('Existing User Profile', () => {
  const email = 'dupont@mail.com';
  const password = 'motdepasse123';

  beforeEach(() => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 5,
        token: 'mocked-jwt-token',
        username: email
      }
    }).as('loginRequest');

    cy.intercept('GET', '/api/user/5', {
      statusCode: 200,
      body: {
        id: 5,
        firstName: 'Paul',
        lastName: 'Dupont',
        email: email,
        admin: false,
        createdAt: '2025-03-10T10:00:00Z',
        updatedAt: '2025-05-10T15:42:00Z'
      }
    }).as('getUser');

    cy.intercept('GET', '/api/session', { body: [] }).as('getSessions');
  });

  it('should display user information on /me page', () => {
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(password);
    cy.get('form').submit();

    cy.wait('@loginRequest').then(() => {
      window.localStorage.setItem('sessionInformation', JSON.stringify({
        id: 5,
        token: 'mocked-jwt-token',
        username: email
      }));
    });

    cy.url().should('include', '/sessions');
    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me');
    cy.wait('@getUser');
    cy.get('p').contains('Name: Paul Dupont');
    cy.get('p').contains('Email: dupont@mail.com');
  });

  it('should go back when clicking the back button', () => {
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(password);
    cy.get('form').submit();
    cy.wait('@loginRequest');
    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me');
    cy.get('button').contains(/back/i).click();
    cy.url().should('include', '/sessions');
  });

  it('should delete the account and log out', () => {
    cy.intercept('DELETE', '/api/user/5', { statusCode: 200 }).as('deleteUser');
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(password);
    cy.get('form').submit();
    cy.wait('@loginRequest');
    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me');
    cy.get('button').contains(/delete/i).click();
    cy.wait('@deleteUser');
    cy.contains('Your account has been deleted !').should('be.visible');
    const baseUrl = Cypress.config().baseUrl?.replace(/\/$/, '') || '';
    cy.url().should('eq', baseUrl + '/');    
    cy.visit('/me');
    cy.url().should('include', '/login');
  });

  it('should display "You are admin" if user is admin', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 42,
        token: 'mocked-jwt-token',
        username: 'admin@example.com'
      }
    }).as('loginRequest');

    cy.intercept('GET', '/api/user/42', {
      statusCode: 200,
      body: {
        id: 42,
        firstName: 'Admin',
        lastName: 'User',
        email: 'admin@example.com',
        admin: true,
        createdAt: '2025-03-10T10:00:00Z',
        updatedAt: '2025-05-10T15:42:00Z'
      }
    }).as('getUserAdmin');

    cy.intercept('GET', '/api/session', { body: [] }).as('getSessions');
    cy.visit('/login');
    cy.get('input[formControlName=email]').type('admin@example.com');
    cy.get('input[formControlName=password]').type('motdepasse123');
    cy.get('form').submit();
    cy.wait('@loginRequest').then(() => {
      window.localStorage.setItem('sessionInformation', JSON.stringify({
        id: 42,
        token: 'mocked-jwt-token',
        username: 'admin@example.com'
      }));
    });

    cy.url().should('include', '/sessions');
    cy.contains('span.link', 'Account').click();
    cy.url().should('include', '/me');
    cy.wait('@getUserAdmin');
    cy.get('p.my2').contains('You are admin').should('be.visible');
  });
});
