package com.agn1kobi.e_commerce_backend.config.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final JdbcTemplate jdbcTemplate;

    public ConfigController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "OK");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> dbCheck() {
        Map<String, Object> body = new HashMap<>();
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            if (result != null && result == 1) {
                body.put("database", "OK");
                return ResponseEntity.ok(body);
            } else {
                body.put("database", "DOWN");
                body.put("error", "Unexpected DB response");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
            }
        } catch (Exception ex) {
            body.put("database", "DOWN");
            body.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
        }
    }
}
