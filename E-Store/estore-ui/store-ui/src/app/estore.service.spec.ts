import { TestBed } from '@angular/core/testing';

import { EstoreService } from './estore.service';

describe('EstoreService', () => {
  let service: EstoreService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EstoreService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
