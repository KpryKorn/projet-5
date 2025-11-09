import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { MatSnackBar } from '@angular/material/snack-bar';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let matSnackBar: MatSnackBar;
  let router: Router;
  let route: ActivatedRoute;

  const mockSession: Session = {
    name: 'Session 1',
    description: 'Description de la session 1',
    date: new Date(),
    teacher_id: 1,
    users: [],
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService,
        TeacherService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    route = TestBed.inject(ActivatedRoute);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form for creation', () => {
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/create');
    component.ngOnInit();
    expect(component.onUpdate).toBeFalsy();
    expect(component.sessionForm).toBeDefined();
  });

  it('should initialize form for update', () => {
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/1');
    jest.spyOn(route.snapshot.paramMap, 'get').mockReturnValue('1');
    jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
    component.ngOnInit();
    expect(component.onUpdate).toBeTruthy();
    expect(sessionApiService.detail).toHaveBeenCalledWith('1');
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.get('name')?.value).toBe(mockSession.name);
  });

  it('should create session and exit', () => {
    component.onUpdate = false;
    component.sessionForm = new FormBuilder().group({
      name: ['test'],
      date: ['2023-01-01'],
      teacher_id: [1],
      description: ['description']
    });
    const createSpy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockSession));
    const routerSpy = jest.spyOn(router, 'navigate').mockImplementation(async () => true);
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');

    component.submit();

    expect(createSpy).toHaveBeenCalled();
    expect(snackBarSpy).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should update session and exit', () => {
    component.onUpdate = true;
    (component as any).id = '1';
    component.sessionForm = new FormBuilder().group({
      name: ['test'],
      date: ['2023-01-01'],
      teacher_id: [1],
      description: ['description']
    });
    const updateSpy = jest.spyOn(sessionApiService, 'update').mockReturnValue(of(mockSession));
    const routerSpy = jest.spyOn(router, 'navigate').mockImplementation(async () => true);
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');

    component.submit();

    expect(updateSpy).toHaveBeenCalled();
    expect(snackBarSpy).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should navigate to /sessions if user is not admin', () => {
    mockSessionService.sessionInformation.admin = false;
    const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation(async () => true);
    component.ngOnInit();
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });
});
