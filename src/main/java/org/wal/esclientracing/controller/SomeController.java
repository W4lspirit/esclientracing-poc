package org.wal.esclientracing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.wal.esclientracing.service.SomeService;

@RestController
public class SomeController {
    private final SomeService someService;

    public SomeController(SomeService someService) {
        this.someService = someService;
    }


    @GetMapping(path = "/some{id}")
    boolean f(@PathVariable String id) {
        return someService.some(id);
    }
}
