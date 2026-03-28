package com.co.eatupapi.services.user;

import com.co.eatupapi.domain.user.City;
import com.co.eatupapi.domain.user.Department;
import com.co.eatupapi.domain.user.DocumentType;
import com.co.eatupapi.dto.user.CityResponse;
import com.co.eatupapi.dto.user.DepartmentResponse;
import com.co.eatupapi.dto.user.DocumentTypeResponse;
import com.co.eatupapi.repositories.user.CityRepository;
import com.co.eatupapi.repositories.user.DepartmentRepository;
import com.co.eatupapi.repositories.user.DocumentTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final DocumentTypeRepository documentTypeRepository;
    private final DepartmentRepository departmentRepository;
    private final CityRepository cityRepository;

    public CatalogServiceImpl(DocumentTypeRepository documentTypeRepository,
                              DepartmentRepository departmentRepository,
                              CityRepository cityRepository) {
        this.documentTypeRepository = documentTypeRepository;
        this.departmentRepository = departmentRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<DocumentTypeResponse> getDocumentTypes() {
        return documentTypeRepository.findAll().stream()
                .map(this::toDocumentTypeResponse)
                .toList();
    }

    @Override
    public List<DepartmentResponse> getDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::toDepartmentResponse)
                .toList();
    }

    @Override
    public List<CityResponse> getCities(UUID departmentId) {
        List<City> cities;
        if (departmentId != null) {
            cities = cityRepository.findByDepartmentId(departmentId);
        } else {
            cities = cityRepository.findAll();
        }
        return cities.stream()
                .map(this::toCityResponse)
                .toList();
    }

    @Override
    public boolean documentTypeExists(UUID id) {
        return documentTypeRepository.existsById(id);
    }

    @Override
    public boolean departmentExists(UUID id) {
        return departmentRepository.existsById(id);
    }

    @Override
    public boolean cityExists(UUID id) {
        return cityRepository.existsById(id);
    }

    @Override
    public boolean cityBelongsToDepartment(UUID cityId, UUID departmentId) {
        return cityRepository.existsByIdAndDepartmentId(cityId, departmentId);
    }

    @Override
    public String getDocumentTypeName(UUID id) {
        return documentTypeRepository.findById(id)
                .map(DocumentType::getName)
                .orElse(null);
    }

    @Override
    public String getDepartmentName(UUID id) {
        return departmentRepository.findById(id)
                .map(Department::getName)
                .orElse(null);
    }

    @Override
    public String getCityName(UUID id) {
        return cityRepository.findById(id)
                .map(City::getName)
                .orElse(null);
    }

    // ── Internal mappers ──────────────────────────────────────────

    private DocumentTypeResponse toDocumentTypeResponse(DocumentType entity) {
        return new DocumentTypeResponse(entity.getId(), entity.getCode(), entity.getName());
    }

    private DepartmentResponse toDepartmentResponse(Department entity) {
        return new DepartmentResponse(entity.getId(), entity.getName());
    }

    private CityResponse toCityResponse(City entity) {
        return new CityResponse(entity.getId(), entity.getDepartmentId(), entity.getName());
    }
}
