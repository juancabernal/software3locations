package com.co.eatupapi.controllers.user;

import com.co.eatupapi.dto.user.CityResponse;
import com.co.eatupapi.dto.user.DepartmentResponse;
import com.co.eatupapi.dto.user.DocumentTypeResponse;
import com.co.eatupapi.services.user.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/userapi/v1")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/document-types")
    public ResponseEntity<List<DocumentTypeResponse>> getDocumentTypes() {
        List<DocumentTypeResponse> types = catalogService.getDocumentTypes();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        List<DepartmentResponse> departments = catalogService.getDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<CityResponse>> getCities(
            @RequestParam(required = false) UUID departmentId) {
        List<CityResponse> cities = catalogService.getCities(departmentId);
        return ResponseEntity.ok(cities);
    }
}
