package com.inkrecords.controller;

import com.inkrecords.dto.RecordRequest;
import com.inkrecords.model.RecordCategory;
import com.inkrecords.model.RecordEntry;
import com.inkrecords.service.RecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService service;

    public RecordController(RecordService service) {
        this.service = service;
    }

    @GetMapping
    public List<RecordEntry> findAll(@RequestParam(required = false) RecordCategory category) {
        return service.findAll(category);
    }

    @GetMapping("/{id}")
    public RecordEntry findOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecordEntry create(@Valid @RequestBody RecordRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public RecordEntry update(@PathVariable Long id, @Valid @RequestBody RecordRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
