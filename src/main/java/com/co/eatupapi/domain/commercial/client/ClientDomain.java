package com.co.eatupapi.domain.commercial.client;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "clients")
public class ClientDomain {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "second_name", length = 100)
    private String secondName;

    @Column(name = "first_lastname", length = 100, nullable = false)
    private String firstLastName;

    @Column(name = "second_lastname", length = 100, nullable = false)
    private String secondLastName;

    @Column(name = "document_type_id", nullable = false)
    private UUID documentTypeId;

    @Column(name = "document_number", length = 30, nullable = false, unique = true)
    private String documentNumber;

    @Column(length = 150, nullable = false, unique = true)
    private String email;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(name = "city_id", nullable = false)
    private UUID cityId;

    @Column(name = "tax_regime_id", nullable = false)
    private UUID taxRegimeId;

    @Column(name = "assigned_seller_id", nullable = false)
    private Long assignedSellerId;

    @Column(name = "apply_discounts", nullable = false)
    private Boolean applyDiscounts;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status;

    public ClientDomain() {
        // Required by JPA
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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
