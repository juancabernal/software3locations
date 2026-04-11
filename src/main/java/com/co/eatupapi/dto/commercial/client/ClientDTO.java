package com.co.eatupapi.dto.commercial.client;

import com.co.eatupapi.domain.commercial.client.ClientStatus;
import java.util.UUID;

public class ClientDTO {

    private String id;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private UUID documentTypeId;
    private String documentNumber;
    private String email;
    private String phone;
    private String address;
    private UUID cityId;
    private UUID taxRegimeId;
    private Long assignedSellerId;
    private Boolean applyDiscounts;
    private ClientStatus status;

    public ClientDTO() {
        // Required for JSON deserialization
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSecondName() { return secondName; }
    public void setSecondName(String secondName) { this.secondName = secondName; }

    public String getFirstLastName() { return firstLastName; }
    public void setFirstLastName(String firstLastName) { this.firstLastName = firstLastName; }

    public String getSecondLastName() { return secondLastName; }
    public void setSecondLastName(String secondLastName) { this.secondLastName = secondLastName; }

    public UUID getDocumentTypeId() { return documentTypeId; }
    public void setDocumentTypeId(UUID documentTypeId) { this.documentTypeId = documentTypeId; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public UUID getCityId() { return cityId; }
    public void setCityId(UUID cityId) { this.cityId = cityId; }

    public UUID getTaxRegimeId() { return taxRegimeId; }
    public void setTaxRegimeId(UUID taxRegimeId) { this.taxRegimeId = taxRegimeId; }

    public Long getAssignedSellerId() { return assignedSellerId; }
    public void setAssignedSellerId(Long assignedSellerId) { this.assignedSellerId = assignedSellerId; }

    public Boolean getApplyDiscounts() { return applyDiscounts; }
    public void setApplyDiscounts(Boolean applyDiscounts) { this.applyDiscounts = applyDiscounts; }

    public ClientStatus getStatus() { return status; }
    public void setStatus(ClientStatus status) { this.status = status; }
}