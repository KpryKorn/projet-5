describe('Login spec', () => {
  beforeEach(() => {
    cy.clearCookies();
    cy.clearLocalStorage();
    cy.visit('/login');
  });

  it('should login successfully', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'First',
        lastName: 'Last',
        admin: true,
        token: 'fake-jwt-token',
      },
    }).as('loginRequest');

    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'First',
        lastName: 'Last',
        admin: true,
      },
    }).as('sessionRequest');

    cy.login('example@domain.com', 'test!1234', true);
    cy.url().should('include', '/sessions');
    cy.wait('@sessionRequest');
  });

  it('should display "An error occurred" when password is incorrect', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { message: 'Invalid credentials' }
    }).as('loginRequest');

    cy.get('input[formControlName=email]').type('example@domain.com');
    cy.get('input[formControlName=password]').type('wrongpassword');
    cy.get('form').submit();
    cy.contains('An error occurred').should('be.visible');
  });

  it('should display "An error occurred" when email is incorrect', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { message: 'Invalid credentials' }
    }).as('loginRequest');

    cy.get('input[formControlName=email]').type('wrong@domain.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('form').submit();
    cy.contains('An error occurred').should('be.visible');
  });

  it('should display "An error occurred" when email field is empty', () => {
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('form').submit();
    cy.contains('An error occurred').should('be.visible');
  });

  it('should display "An error occurred" when password field is empty', () => {
    cy.get('input[formControlName=email]').type("example@domain.com");
    cy.get('form').submit();
    cy.contains('An error occurred').should('be.visible');
  });

  it('should display "An error occurred" when both fields are empty', () => {
    cy.get('form').submit();
    cy.contains('An error occurred').should('be.visible');
  });

  it('should block access to protected pages when not logged in', () => {
    cy.visit('/sessions');
    cy.url().should('include', '/login');
  });

  it('should allow toggling password visibility', () => {
    cy.visit('/login');
    cy.get('input[formControlName=password]').should('have.attr', 'type', 'password');
    cy.get('button[aria-label="Hide password"]').click();
    cy.get('input[formControlName=password]').should('have.attr', 'type', 'text');
    cy.get('button[aria-label="Hide password"]').click();
    cy.get('input[formControlName=password]').should('have.attr', 'type', 'password');
  });
});
