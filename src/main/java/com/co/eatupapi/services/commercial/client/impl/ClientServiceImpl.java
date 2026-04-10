package com.co.eatupapi.services.commercial.client.impl;

import com.co.eatupapi.domain.commercial.client.ClientDomain;
import com.co.eatupapi.domain.commercial.client.ClientStatus;
import com.co.eatupapi.dto.commercial.client.ClientDTO;
import com.co.eatupapi.repositories.commercial.client.ClientRepository;
import com.co.eatupapi.services.commercial.client.ClientService;
import com.co.eatupapi.utils.commercial.client.exceptions.ClientNotFoundException;
import com.co.eatupapi.utils.commercial.client.mapper.ClientMapper;
import com.co.eatupapi.utils.commercial.client.validation.ClientValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(
            ClientRepository clientRepository,
            ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    @Transactional
    public ClientDTO createClient(ClientDTO request) {
        validateClientRequest(request);
        validateForeignKeys(request);

        ClientDomain client = clientMapper.toDomain(request);
        client.setStatus(ClientStatus.ACTIVE);
        clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO getClientById(String clientId) {
        ClientDomain client = findById(clientId);
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> getClients(String name, String email, String city, String documentNumber,
                                       Boolean active, Boolean applyDiscounts) {
        List<ClientDomain> clients = clientRepository.findAll();

        return clients.stream()
                .filter(c -> name == null || c.getFirstName().toLowerCase().contains(name.toLowerCase()))
                .filter(c -> email == null || c.getEmail().equalsIgnoreCase(email))
                .filter(c -> documentNumber == null || c.getDocumentNumber().equals(documentNumber))
                .filter(c -> applyDiscounts == null || (c.getApplyDiscounts() != null && c.getApplyDiscounts().equals(applyDiscounts)))
                .filter(c -> active == null ||
                        (c.getStatus() != null &&
                        ((active && c.getStatus() == ClientStatus.ACTIVE) ||
                        (!active && c.getStatus() == ClientStatus.INACTIVE))))
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClientDTO updateClient(String clientId, ClientDTO request) {
        validateClientRequest(request);
        validateForeignKeys(request);

        ClientDomain client = findById(clientId);
        clientMapper.updateDomain(client, request);
        clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional
    public ClientDTO updateStatus(String clientId, Boolean active) {
        ClientDomain client = findById(clientId);
        client.setStatus(active ? ClientStatus.ACTIVE : ClientStatus.INACTIVE);
        clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    private void validateClientRequest(ClientDTO request) {
        ClientValidationUtils.requireObject(request, "Client request cannot be null");
        ClientValidationUtils.requireText(request.getFirstName(), "firstName");
        ClientValidationUtils.requireText(request.getFirstLastName(), "firstLastName");
        ClientValidationUtils.requireText(request.getDocumentNumber(), "documentNumber");
        ClientValidationUtils.requireObject(request.getDocumentTypeId(), "documentTypeId");
        ClientValidationUtils.requireObject(request.getCityId(), "cityId");

        if (request.getEmail() != null) {
            ClientValidationUtils.validateEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            ClientValidationUtils.validatePhone(request.getPhone());
        }
    }

    private void validateForeignKeys(ClientDTO request) {
        // Foreign key validation skipped - other team members use UUID for IDs
        // Validation can be added later using code/name queries or ID standardization
    }

    private ClientDomain findById(String clientId) {
        UUID uuid = UUID.fromString(clientId);
        return clientRepository.findById(uuid)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));
    }
}