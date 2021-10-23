package org.wal.esclientracing.service;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;
import org.wal.esclientracing.repository.SomeRepository;

@Service
public class SomeService {
    private final SomeRepository someRepository;

    public SomeService(SomeRepository someRepository) {
        this.someRepository = someRepository;
    }

    @NewSpan
    public boolean some(String id) {
        return someRepository.exists(id);
    }
}
