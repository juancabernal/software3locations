package com.co.eatupapi.services.commercial.client;

import com.co.eatupapi.domain.commercial.client.ClientDomain;
import com.co.eatupapi.domain.commercial.client.ClientStatus;
import com.co.eatupapi.dto.commercial.client.ClientDTO;
import com.co.eatupapi.repositories.commercial.client.ClientRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final List<ClientDomain> clients = new ArrayList<>();

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostConstruct
    public void init() {
        clients.addAll(clientRepository.loadInitialClients());
    }

    public ClientDTO createClient(ClientDTO request) {

        ClientDomain client = new ClientDomain();

        client.setId(UUID.randomUUID().toString());
        client.setFirstName(request.getFirstName());
        client.setSecondName(request.getSecondName());
        client.setFirstLastName(request.getFirstLastName());
        client.setSecondLastName(request.getSecondLastName());
        client.setDocumentTypeId(request.getDocumentTypeId());
        client.setDocumentNumber(request.getDocumentNumber());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());
        client.setCityId(request.getCityId());
        client.setTaxRegimeId(request.getTaxRegimeId());
        client.setAssignedSellerId(request.getAssignedSellerId());
        client.setApplyDiscounts(request.getApplyDiscounts());
        client.setStatus(ClientStatus.ACTIVE);

        clients.add(client);

        return request;
    }


    public ClientDTO getClientById(String clientId) {
        ClientDomain client = findById(clientId);
        return mapToDTO(client);
    }


    public List<ClientDTO> getClients(String name, String email, String city,
                                      String documentNumber, Boolean active, Boolean applyDiscounts) {

        List<ClientDTO> result = new ArrayList<>();

        for (ClientDomain client : clients) {

            if (name != null && !client.getFirstName().toLowerCase().contains(name.toLowerCase())) continue;
            if (email != null && !client.getEmail().equalsIgnoreCase(email)) continue;
            if (documentNumber != null && !client.getDocumentNumber().equals(documentNumber)) continue;
            if (applyDiscounts != null && !client.getApplyDiscounts().equals(applyDiscounts)) continue;

            if (active != null) {
                if (active && client.getStatus() != ClientStatus.ACTIVE) continue;
                if (!active && client.getStatus() != ClientStatus.INACTIVE) continue;
            }

            result.add(mapToDTO(client));
        }

        return result;
    }


    public ClientDTO updateClient(String clientId, ClientDTO request) {

        ClientDomain client = findById(clientId);



        client.setFirstName(request.getFirstName());
        client.setSecondName(request.getSecondName());
        client.setFirstLastName(request.getFirstLastName());
        client.setSecondLastName(request.getSecondLastName());
        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());
        client.setCityId(request.getCityId());
        client.setTaxRegimeId(request.getTaxRegimeId());
        client.setAssignedSellerId(request.getAssignedSellerId());
        client.setApplyDiscounts(request.getApplyDiscounts());

        return mapToDTO(client);
    }


    public ClientDTO updateStatus(String clientId, Boolean active) {

        ClientDomain client = findById(clientId);

        if (active) {
            client.setStatus(ClientStatus.ACTIVE);
        } else {
            client.setStatus(ClientStatus.INACTIVE);
        }

        return mapToDTO(client);
    }

    private ClientDomain findById(String clientId) {
        for (ClientDomain client : clients) {
            if (client.getId().equals(clientId)) {
                return client;
            }
        }
        throw new RuntimeException("Client not found");
    }

    private ClientDTO mapToDTO(ClientDomain client) {

        ClientDTO dto = new ClientDTO();

        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setSecondName(client.getSecondName());
        dto.setFirstLastName(client.getFirstLastName());
        dto.setSecondLastName(client.getSecondLastName());
        dto.setDocumentTypeId(client.getDocumentTypeId());
        dto.setDocumentNumber(client.getDocumentNumber());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        dto.setAddress(client.getAddress());
        dto.setCityId(client.getCityId());
        dto.setTaxRegimeId(client.getTaxRegimeId());
        dto.setAssignedSellerId(client.getAssignedSellerId());
        dto.setApplyDiscounts(client.getApplyDiscounts());
        dto.setStatus(client.getStatus());

        return dto;
    }
}
