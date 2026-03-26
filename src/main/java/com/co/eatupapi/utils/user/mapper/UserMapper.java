package com.co.eatupapi.utils.user.mapper;

import com.co.eatupapi.domain.user.UserDomain;
import com.co.eatupapi.dto.user.CreateUserRequest;
import com.co.eatupapi.dto.user.UserResponse;
import com.co.eatupapi.dto.user.UserSummaryResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDomain toDomain(CreateUserRequest request) {
        UserDomain user = new UserDomain();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDocumentTypeId(request.getDocumentTypeId());
        user.setDocumentNumber(request.getDocumentNumber());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setBirthDate(request.getBirthDate());
        user.setDepartmentId(request.getDepartmentId());
        user.setCityId(request.getCityId());
        user.setAddress(request.getAddress());
        user.setBranchId(request.getBranchId());
        return user;
    }

    public UserResponse toResponse(UserDomain user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setDocumentNumber(user.getDocumentNumber());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setBirthDate(user.getBirthDate());
        response.setAddress(user.getAddress());
        response.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
        return response;
    }

    public UserSummaryResponse toSummaryResponse(UserDomain user) {
        UserSummaryResponse summary = new UserSummaryResponse();
        summary.setId(user.getId());
        summary.setFirstName(user.getFirstName());
        summary.setLastName(user.getLastName());
        summary.setDocumentNumber(user.getDocumentNumber());
        summary.setEmail(user.getEmail());
        summary.setPhone(user.getPhone());
        summary.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
        return summary;
    }
}
