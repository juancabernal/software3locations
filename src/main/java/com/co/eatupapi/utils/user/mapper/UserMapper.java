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
        summary.setFirstName(user.getFirstName());
        summary.setLastName(user.getLastName());
        summary.setDocumentNumber(maskDocumentNumber(user.getDocumentNumber()));
        summary.setEmail(maskEmail(user.getEmail()));
        summary.setPhone(maskPhone(user.getPhone()));
        summary.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
        return summary;
    }

    private String maskDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
            return null;
        }
        if (documentNumber.length() <= 4) {
            return "****";
        }
        return "*".repeat(documentNumber.length() - 4) + documentNumber.substring(documentNumber.length() - 4);
    }

    private String maskEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return "***";
        }

        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex);
        String visiblePrefix = localPart.substring(0, Math.min(2, localPart.length()));
        return visiblePrefix + "***" + domainPart;
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }
        if (phone.length() <= 4) {
            return "****";
        }
        return "*".repeat(phone.length() - 4) + phone.substring(phone.length() - 4);
    }
}
