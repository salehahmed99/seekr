package com.example.Search.Engine.controller;

import com.example.Search.Engine.BackendManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Controller for handling search requests.
 * Provides a REST API endpoint for searching documents.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.OPTIONS}, allowedHeaders = "*", allowCredentials = "true")
public class SearchController {

    private final BackendManager backendManager;

    @Autowired
    public SearchController(BackendManager backendManager) {
        this.backendManager = backendManager;
    }

    @GetMapping("/search")
    public ResponseEntity<BackendManager.SearchResponse> search(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (page < 0) {
            return ResponseEntity.badRequest().body(new BackendManager.SearchResponse(List.of(), 0));
        }

        if (size <= 0 || size > 100) {
            size = 10;
        }

        try {
            BackendManager.SearchResponse response = backendManager.search(query, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}