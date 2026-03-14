package com.co.eatupapi.repositories.commercial.provider;

import java.time.LocalDateTime;
import java.util.List;

import com.co.eatupapi.domain.commercial.provider.ProviderDomain;
import com.co.eatupapi.domain.commercial.provider.ProviderStatus;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderRepository {

    public List<ProviderDomain> loadInitialProviders() {
        LocalDateTime now = LocalDateTime.now();

        ProviderDomain providerDomain1 = new ProviderDomain();
        providerDomain1.setId("provider-001");
        providerDomain1.setBusinessName("Distribuidora Alimentos SAS");
        providerDomain1.setDocumentTypeId(1L);
        providerDomain1.setDocumentNumber("900123456");
        providerDomain1.setTaxRegimeId(10L);
        providerDomain1.setResponsibleFirstName("Carlos");
        providerDomain1.setResponsibleLastName("Ramirez");
        providerDomain1.setPhone("3004567890");
        providerDomain1.setEmail("contacto@empresa.com");
        providerDomain1.setDepartmentId(11L);
        providerDomain1.setCityId(11001L);
        providerDomain1.setAddress("Calle 10 #45-23");
        providerDomain1.setBranchId(1L);
        providerDomain1.setStatus(ProviderStatus.ACTIVE);
        providerDomain1.setCreatedDate(now.minusDays(5));
        providerDomain1.setModifiedDate(now.minusDays(1));

        ProviderDomain providerDomain2 = new ProviderDomain();
        providerDomain2.setId("provider-002");
        providerDomain2.setBusinessName("Logistica y Suministros LTDA");
        providerDomain2.setDocumentTypeId(1L);
        providerDomain2.setDocumentNumber("901987654");
        providerDomain2.setTaxRegimeId(10L);
        providerDomain2.setResponsibleFirstName("Laura");
        providerDomain2.setResponsibleLastName("Gomez");
        providerDomain2.setPhone("3206541230");
        providerDomain2.setEmail("ventas@logistica.com");
        providerDomain2.setDepartmentId(5L);
        providerDomain2.setCityId(5001L);
        providerDomain2.setAddress("Carrera 50 #12-99");
        providerDomain2.setBranchId(2L);
        providerDomain2.setStatus(ProviderStatus.ACTIVE);
        providerDomain2.setCreatedDate(now.minusDays(10));
        providerDomain2.setModifiedDate(now.minusDays(2));

        ProviderDomain providerDomain3 = new ProviderDomain();
        providerDomain3.setId("provider-003");
        providerDomain3.setBusinessName("Tecnologia Integral S.A.");
        providerDomain3.setDocumentTypeId(1L);
        providerDomain3.setDocumentNumber("900765432");
        providerDomain3.setTaxRegimeId(20L);
        providerDomain3.setResponsibleFirstName("Javier");
        providerDomain3.setResponsibleLastName("Mendoza");
        providerDomain3.setPhone("3101239876");
        providerDomain3.setEmail("info@tecintegral.co");
        providerDomain3.setDepartmentId(76L);
        providerDomain3.setCityId(76001L);
        providerDomain3.setAddress("Avenida 6N #34-10");
        providerDomain3.setBranchId(3L);
        providerDomain3.setStatus(ProviderStatus.INACTIVE);
        providerDomain3.setCreatedDate(now.minusDays(15));
        providerDomain3.setModifiedDate(now.minusDays(3));

        return List.of(providerDomain1, providerDomain2, providerDomain3);
    }
}
