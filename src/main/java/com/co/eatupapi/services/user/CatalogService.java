package com.co.eatupapi.services.user;

import com.co.eatupapi.dto.user.CityResponse;
import com.co.eatupapi.dto.user.DepartmentResponse;
import com.co.eatupapi.dto.user.DocumentTypeResponse;

import java.util.List;
import java.util.UUID;

public interface CatalogService {

    List<DocumentTypeResponse> getDocumentTypes();

    List<DepartmentResponse> getDepartments();

    List<CityResponse> getCities(UUID departmentId);

    boolean documentTypeExists(UUID id);

    boolean departmentExists(UUID id);

    boolean cityExists(UUID id);

    String getDocumentTypeName(UUID id);

    String getDepartmentName(UUID id);

    String getCityName(UUID id);
}
