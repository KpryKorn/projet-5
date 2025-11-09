import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const mockSession: Session = {
    id: 1,
    name: 'Test Session',
    description: 'Test Description',
    date: new Date('2023-12-01'),
    teacher_id: 1,
    users: [1, 2, 3]
  };

  const mockSessions: Session[] = [
    mockSession,
    {
      id: 2,
      name: 'Another Session',
      description: 'Another Description',
      date: new Date('2023-12-02'),
      teacher_id: 2,
      users: [4, 5]
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('all()', () => {
    it('should return all sessions', () => {
      service.all().subscribe(sessions => {
        expect(sessions).toEqual(mockSessions);
        expect(sessions.length).toBe(2);
      });

      const req = httpMock.expectOne('api/session');
      expect(req.request.method).toBe('GET');
      req.flush(mockSessions);
    });
  });

  describe('detail()', () => {
    it('should return a session by id', () => {
      const sessionId = '1';

      service.detail(sessionId).subscribe(session => {
        expect(session).toEqual(mockSession);
      });

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockSession);
    });
  });

  describe('update()', () => {
    it('should update an existing session', () => {
      const sessionId = '1';
      const updatedSession: Session = {
        ...mockSession,
        name: 'Updated Session Name'
      };

      service.update(sessionId, updatedSession).subscribe(session => {
        expect(session.name).toBe('Updated Session Name');
      });

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updatedSession);
      req.flush(updatedSession);
    });
  });

  describe('delete()', () => {
    it('should delete a session', () => {
      const sessionId = '1';

      service.delete(sessionId).subscribe();

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush({});
    });
  });

  describe('participate()', () => {
    it('should add user to session', () => {
      const sessionId = '1';
      const userId = '5';

      service.participate(sessionId, userId).subscribe();

      const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('POST');
      req.flush(null);
    });
  });

  describe('unParticipate()', () => {
    it('should remove user from session', () => {
      const sessionId = '1';
      const userId = '5';

      service.unParticipate(sessionId, userId).subscribe();

      const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });
})
