package com.co.eatupapi.services.commercial.table;

import com.co.eatupapi.dto.commercial.table.*;
import com.co.eatupapi.messaging.commercial.table.TableEventPublisher;
import com.co.eatupapi.repositories.commercial.table.TableRepository;
import com.co.eatupapi.repositories.commercial.table.TableReservationRepository;
import com.co.eatupapi.repositories.commercial.table.TableSessionRepository;
import com.co.eatupapi.utils.commercial.table.mapper.TableMapper;
import com.co.eatupapi.utils.commercial.table.exceptions.TableBusinessException;
import com.co.eatupapi.utils.commercial.table.exceptions.TableResourceNotFoundException;
import com.co.eatupapi.utils.commercial.table.exceptions.TableValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;
    private final TableSessionRepository sessionRepository;
    private final TableReservationRepository reservationRepository;
    private final TableMapper tableMapper;
    private final TableEventPublisher publisher;

    public TableServiceImpl(
            TableRepository tableRepository,
            TableSessionRepository sessionRepository,
            TableReservationRepository reservationRepository,
            TableMapper tableMapper,
            TableEventPublisher publisher
    ) {
        this.tableRepository = tableRepository;
        this.sessionRepository = sessionRepository;
        this.reservationRepository = reservationRepository;
        this.tableMapper = tableMapper;
        this.publisher = publisher;
    }

    // ── COMMANDS (ESCRITURA - SOLO EVENTOS A RABBITMQ) ───────────────────────────

    @Override
    public TableDTO createTable(TableDTO request) {
        validateTable(request);

        // 🛡️ REGLA 1: Evitar mesas duplicadas en la misma sede
        boolean exists = tableRepository.existsByVenueIdAndTableNumberAndActiveTrue(
                UUID.fromString(request.getVenueId()),
                request.getTableNumber()
        );

        if (exists) {
            throw new TableBusinessException("Ya existe una mesa activa con el número " + request.getTableNumber() + " en esta sede.");
        }

        // Normalización opcional (limpiar espacios en blanco)
        if (request.getLocation() != null) {
            request.setLocation(request.getLocation().trim());
        }

        publisher.publishTableCreated(request);
        return request;
    }

    @Override
    public TableDTO updateTable(String tableId, TableDTO request) {
        validateId(tableId, "tableId");
        validateTable(request);

        // 🛡️ REGLA 2: Para actualizar, verificar que no choque con OTRA mesa
        boolean existsAnother = tableRepository.existsByVenueIdAndTableNumberAndActiveTrueAndIdNot(
                UUID.fromString(request.getVenueId()),
                request.getTableNumber(),
                UUID.fromString(tableId)
        );

        if (existsAnother) {
            throw new TableBusinessException("El número de mesa " + request.getTableNumber() + " ya está siendo usado por otra mesa en esta sede.");
        }

        request.setId(tableId);

        if (request.getLocation() != null) {
            request.setLocation(request.getLocation().trim());
        }

        publisher.publishTableUpdated(request);
        return request;
    }

    @Override
    public void deactivateTable(String tableId) {
        validateId(tableId, "tableId");

        // 🛡️ REGLA 3: No desactivar si hay clientes comiendo
        boolean isOccupied = sessionRepository.findByTableIdAndClosedAtIsNull(UUID.fromString(tableId)).isPresent();
        if (isOccupied) {
            throw new TableBusinessException("No se puede desactivar la mesa porque tiene una sesión abierta (clientes en la mesa).");
        }

        publisher.publishTableDeactivated(tableId);
    }

    @Override
    public TableSessionDTO openSession(String tableId, TableSessionDTO request) {
        validateSession(request);

        boolean isAlreadyOccupied = sessionRepository.findByTableIdAndClosedAtIsNull(UUID.fromString(tableId)).isPresent();
        if (isAlreadyOccupied) {
            throw new TableBusinessException("Esta mesa ya está ocupada. Cierra la sesión actual antes de abrir una nueva.");
        }

        request.setTableId(tableId);
        publisher.publishSessionOpened(request);
        return request;
    }

    @Override
    public TableSessionDTO closeSession(String tableId, String sessionId) {
        validateId(tableId, "tableId");
        validateId(sessionId, "sessionId");

        TableSessionDTO dto = new TableSessionDTO();
        dto.setId(sessionId);
        dto.setTableId(tableId);

        publisher.publishSessionClosed(dto);
        return dto;
    }

    @Override
    public TableSessionDTO updateGuestCount(String tableId, String sessionId, Integer guestCount) {
        validateId(tableId, "tableId");
        validateId(sessionId, "sessionId");

        if (guestCount == null || guestCount < 1) {
            throw new TableValidationException("El número de invitados (guestCount) es inválido, debe ser mayor a 0");
        }

        // 1. Publicar el evento
        publisher.publishSessionUpdated(tableId, sessionId, guestCount);

        // 2. Retornar DTO de respuesta para el Frontend
        TableSessionDTO response = new TableSessionDTO();
        response.setId(sessionId);
        response.setTableId(tableId);
        response.setGuestCount(guestCount);
        return response;
    }

    @Override
    public TableReservationDTO createReservation(String tableId, TableReservationDTO request) {
        validateReservation(request);

        // 🛡️ REGLA 5: No permitir reservas en el pasado
        if (request.getReservationDate() != null && request.getReservationDate().isBefore(LocalDate.now())) {
            throw new TableBusinessException("La fecha de reserva no puede ser en el pasado.");
        }

        request.setTableId(tableId);
        publisher.publishReservationCreated(request);
        return request;
    }

    @Override
    public TableReservationDTO updateReservation(String tableId, String reservationId, TableReservationDTO request) {
        validateReservation(request);
        validateId(reservationId, "reservationId");

        // 🛡️ REGLA 5: Aplica también al actualizar
        if (request.getReservationDate() != null && request.getReservationDate().isBefore(LocalDate.now())) {
            throw new TableBusinessException("La fecha de reserva no puede ser en el pasado.");
        }

        request.setId(reservationId);
        request.setTableId(tableId);

        publisher.publishReservationUpdated(request);
        return request;
    }

    @Override
    public void cancelReservation(String tableId, String reservationId) {
        validateId(tableId, "tableId");
        validateId(reservationId, "reservationId");
        publisher.publishReservationCancelled(tableId, reservationId);
    }

    @Override
    public TableSessionDTO seatReservation(String reservationId, TableSessionDTO request) {
        validateId(reservationId, "reservationId");

        // 🛡️ CREAMOS UN DTO NUEVO DESDE CERO
        // Esto garantiza que no arrastramos basura del body original
        TableSessionDTO cleanRequest = new TableSessionDTO();
        cleanRequest.setGuestCount(request.getGuestCount());
        cleanRequest.setObservations(request.getObservations());

        // Asignamos el ID de la URL
        cleanRequest.setReservationId(reservationId);

        // PUBLICAMOS EL NUEVO OBJETO LIMPIO
        publisher.publishReservationSeated(cleanRequest);

        return cleanRequest;
    }

    // ── GETS (LECTURA - DB REAL) ────────────────────────────────────

    @Override
    public List<TableDTO> getTables(String status, String venueId, Boolean reserved, Boolean canOpenNow) {
        return tableRepository.findAll()
                .stream()
                .map(tableMapper::toDto)
                .toList();
    }

    @Override
    public TableDTO getTableById(String tableId) {
        return tableMapper.toDto(
                tableRepository.findById(UUID.fromString(tableId))
                        // Usamos NotFoundException aquí (Status 404)
                        .orElseThrow(() -> new TableResourceNotFoundException("Mesa no encontrada con el ID proporcionado."))
        );
    }

    @Override
    public List<TableDTO> getTablesByVenue(String venueId) {
        return tableRepository.findAll()
                .stream()
                .filter(t -> t.getVenueId().toString().equals(venueId))
                .map(tableMapper::toDto)
                .toList();
    }

    @Override
    public TableSessionDTO getActiveSession(String tableId) {
        return sessionRepository.findByTableIdAndClosedAtIsNull(UUID.fromString(tableId))
                .map(tableMapper::toSessionDto)
                // Usamos NotFoundException aquí (Status 404)
                .orElseThrow(() -> new TableResourceNotFoundException("No hay ninguna sesión activa en esta mesa."));
    }

    @Override
    public List<TableSessionDTO> getSessionHistory(String tableId) {
        return sessionRepository.findAllByTableId(UUID.fromString(tableId))
                .stream()
                .map(tableMapper::toSessionDto)
                .toList();
    }

    @Override
    public TableReservationDTO getActiveReservation(String tableId) {
        return reservationRepository.findAll()
                .stream()
                .filter(r -> r.getTableId().toString().equals(tableId))
                .findFirst()
                .map(tableMapper::toReservationDto)
                // Usamos NotFoundException aquí (Status 404)
                .orElseThrow(() -> new TableResourceNotFoundException("No se encontró ninguna reserva para esta mesa."));
    }

    @Override
    public List<TableReservationDTO> searchReservationsByGuestDocumentNumber(String doc) {
        return reservationRepository.findAll()
                .stream()
                .filter(r -> doc.equals(r.getGuestDocumentNumber()))
                .map(tableMapper::toReservationDto)
                .toList();
    }

    @Override
    public TableSummaryDTO getSummary() {
        TableSummaryDTO dto = new TableSummaryDTO();
        dto.setTotalRegistered((long) tableRepository.findAll().size());
        return dto;
    }

    @Override
    public TableSummaryDTO getSummaryByVenue(String venueId) {
        TableSummaryDTO dto = new TableSummaryDTO();
        dto.setVenueId(venueId);
        return dto;
    }

    // ── VALIDACIONES (GUARDIA DE SEGURIDAD) ──────────────────────────────────────

    private void validateTable(TableDTO r) {
        if (r == null) {
            throw new TableValidationException("El cuerpo de la petición (body) es requerido");
        }
        if (r.getTableNumber() == null || r.getTableNumber() <= 0) {
            throw new TableValidationException("El tableNumber es obligatorio y debe ser mayor a 0");
        }
        if (r.getLocation() == null || r.getLocation().isBlank()) {
            throw new TableValidationException("La ubicación (location) es obligatoria");
        }
        if (r.getVenueId() == null || r.getVenueId().isBlank()) {
            throw new TableValidationException("El venueId es obligatorio");
        }
    }

    private void validateSession(TableSessionDTO r) {
        if (r == null) {
            throw new TableValidationException("El cuerpo de la petición de sesión (body) es requerido");
        }
    }

    private void validateReservation(TableReservationDTO r) {
        if (r == null) {
            throw new TableValidationException("El cuerpo de la petición de reserva (body) es requerido");
        }
    }

    private void validateId(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new TableValidationException("El campo " + field + " es requerido en la ruta o petición");
        }
    }
}