package com.co.eatupapi.repositories.commercial.client;

import java.util.List;

import com.co.eatupapi.domain.commercial.client.ClientDomain;
import com.co.eatupapi.domain.commercial.client.ClientStatus;
import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository {

    public List<ClientDomain> loadInitialClients() {

        ClientDomain client1 = new ClientDomain();
        client1.setId("client-001");
        client1.setFirstName("Juan");
        client1.setSecondName("Carlos");
        client1.setFirstLastName("Perez");
        client1.setSecondLastName("Gomez");
        client1.setDocumentTypeId(1L);
        client1.setDocumentNumber("123456789");
        client1.setEmail("juan@email.com");
        client1.setPhone("3001234567");
        client1.setAddress("Calle 10 #20-30");
        client1.setCityId(11001L);
        client1.setTaxRegimeId(10L);
        client1.setAssignedSellerId(1L);
        client1.setApplyDiscounts(true);
        client1.setStatus(ClientStatus.ACTIVE);

        ClientDomain client2 = new ClientDomain();
        client2.setId("client-002");
        client2.setFirstName("Maria");
        client2.setSecondName("Luisa");
        client2.setFirstLastName("Rodriguez");
        client2.setSecondLastName("Lopez");
        client2.setDocumentTypeId(1L);
        client2.setDocumentNumber("987654321");
        client2.setEmail("maria@email.com");
        client2.setPhone("3109876543");
        client2.setAddress("Carrera 50 #40-20");
        client2.setCityId(5001L);
        client2.setTaxRegimeId(20L);
        client2.setAssignedSellerId(2L);
        client2.setApplyDiscounts(false);
        client2.setStatus(ClientStatus.INACTIVE);

        return List.of(client1, client2);
    }
}