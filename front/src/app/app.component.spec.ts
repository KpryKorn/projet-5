import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { AuthService } from './features/auth/services/auth.service';
import { Router } from '@angular/router';


describe('AppComponent', () => {
  let component: AppComponent;
  let sessionService: SessionService;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]),
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();

    const fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should check if the user is logged in', () => {
    const isLoggedSpy = jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(true));
    const result = component.$isLogged();
    expect(isLoggedSpy).toHaveBeenCalled();
    result.subscribe(isLogged => {
      expect(isLogged).toBe(true);
    });
  });

  it('should log out the user', () => {
    const logOutSpy = jest.spyOn(sessionService, 'logOut');
    const navigateSpy = jest.spyOn(router, 'navigate');
    component.logout();
    expect(logOutSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });
});
