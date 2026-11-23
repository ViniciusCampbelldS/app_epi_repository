package com.projeto.api_epi.controllers;

import com.projeto.api_epi.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    // GET /dashboard?adminId=1 -> indicadores do painel
    @GetMapping
    public ResponseEntity<?> indicadores(@RequestParam Long adminId) {
        try {
            return ResponseEntity.ok(service.indicadores(adminId)); // 200
        } catch (SecurityException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN) // 403
                    .body(e.getMessage());
        }
    }
}