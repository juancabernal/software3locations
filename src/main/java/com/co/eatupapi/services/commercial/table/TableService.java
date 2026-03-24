package com.co.eatupapi.services.commercial.table;

import com.co.eatupapi.domain.commercial.table.ReservationStatus;
import com.co.eatupapi.domain.commercial.table.TableDomain;
import com.co.eatupapi.domain.commercial.table.TableReservationDomain;
import com.co.eatupapi.domain.commercial.table.TableSessionDomain;
import com.co.eatupapi.domain.commercial.table.TableStatus;
import com.co.eatupapi.dto.commercial.table.TableDTO;
import com.co.eatupapi.dto.commercial.table.TableReservationDTO;
import com.co.eatupapi.dto.commercial.table.TableSessionDTO;
import com.co.eatupapi.dto.commercial.table.TableSummaryDTO;
import com.co.eatupapi.repositories.commercial.table.TableRepository;
import com.co.eatupapi.utils.commercial.table.exceptions.TableBusinessException;
import com.co.eatupapi.utils.commercial.table.exceptions.TableResourceNotFoundException;
import com.co.eatupapi.utils.commercial.table.exceptions.TableValidationException;
import com.co.eatupapi.utils.commercial.table.mapper.TableMapper;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TableService {

    private final TableRepository tableRepository;
    private final TableMapper tableMapper;
    private final List<TableDomain> tables = new ArrayList<>();
    private final List<TableSessionDomain> sessions = new ArrayList<>();
    private final List<TableReservationDomain> reservations = new ArrayList<>();

    public TableService(TableRepository tableRepository, TableMapper tableMapper) {
        this.tableRepository = tableRepository;
        this.tableMapper = tableMapper;
    }

    @PostConstruct
    public void initData() {
        tables.clear();
        sessions.clear();
        reservations.clear();
        tables.addAll(tableRepository.loadInitialTables());
        sessions.addAll(tableRepository.loadInitialSessions());
        reservations.addAll(tableRepository.loadInitialReservations());
    }

    // ── TABLE CRUD ────────────────────────────────────────────────────────────

    public TableDTO createTable(TableDTO request) {
        validateTablePayload(request);
        validateTableNumberNotDuplicated(request.getVenueId(), request.getTableNumber(), null);

        TableDomain domain = tableMapper.toDomain(request);
        domain.setId(UUID.randomUUID().toString());
        domain.setStatus(TableStatus.AVAILABLE);
        domain.setActive(true);
        domain.setCreatedDate(LocalDateTime.now());
        domain.setModifiedDate(LocalDateTime.now());

        tables.add(domain);
        return tableMapper.toDto(domain);
    }

    public List<TableDTO> getTables(String status, String venueId, Boolean isVip, Boolean isAccessible) {
        TableStatus parsedStatus = parseStatus(status);

        List<TableDTO> result = new ArrayList<>();
        for (TableDomain table : tables) {
            if (Boolean.FALSE.equals(table.getActive())) continue;
            if (parsedStatus != null && table.getStatus() != parsedStatus) continue;
            if (venueId != null && !venueId.isBlank() && !table.getVenueId().equals(venueId)) continue;
            if (isVip != null && !table.getIsVip().equals(isVip)) continue;
            if (isAccessible != null && !table.getIsAccessible().equals(isAccessible)) continue;

            result.add(tableMapper.toDto(table));
        }
        return result;
    }

    public TableDTO getTableById(String tableId) {
        TableDomain table = findTableById(tableId);
        TableDTO dto = tableMapper.toDto(table);
        dto.setActiveSession(findActiveSessionByTableId(tableId));
        return dto;
    }

    public List<TableDTO> getTablesByVenue(String venueId) {
        if (venueId == null || venueId.isBlank()) {
            throw new TableValidationException("Field 'venueId' is required and cannot be empty");
        }

        List<TableDTO> result = new ArrayList<>();
        for (TableDomain table : tables) {
            if (Boolean.TRUE.equals(table.getActive()) && table.getVenueId().equals(venueId)) {
                result.add(tableMapper.toDto(table));
            }
        }
        return result;
    }

    public TableDTO updateTable(String tableId, TableDTO request) {
        validateTablePayload(request);

        TableDomain existing = findTableById(tableId);
        validateTableNumberNotDuplicated(request.getVenueId(), request.getTableNumber(), tableId);

        existing.setTableNumber(request.getTableNumber());
        existing.setLocation(request.getLocation());
        existing.setIsVip(request.getIsVip());
        existing.setHasView(request.getHasView());
        existing.setIsAccessible(request.getIsAccessible());
        existing.setVenueId(request.getVenueId());
        existing.setModifiedDate(LocalDateTime.now());

        return tableMapper.toDto(existing);
    }

    public TableDTO updateTableStatus(String tableId, String status) {
        TableStatus newStatus = parseRequiredStatus(status);
        TableDomain existing = findTableById(tableId);
        existing.setStatus(newStatus);
        existing.setModifiedDate(LocalDateTime.now());
        return tableMapper.toDto(existing);
    }

    public void deactivateTable(String tableId) {
        TableDomain existing = findTableById(tableId);

        if (existing.getStatus() == TableStatus.OCCUPIED) {
            throw new TableBusinessException("Cannot deactivate a table that is currently occupied");
        }

        existing.setActive(false);
        existing.setModifiedDate(LocalDateTime.now());
    }

    // ── SESSIONS ──────────────────────────────────────────────────────────────

    public TableSessionDTO openSession(String tableId, TableSessionDTO request) {
        validateSessionPayload(request);

        TableDomain table = findTableById(tableId);

        if (table.getStatus() == TableStatus.OCCUPIED) {
            throw new TableBusinessException("Table is already occupied. Close the current session before opening a new one");
        }

        TableSessionDomain session = tableMapper.toSessionDomain(request);
        session.setId(UUID.randomUUID().toString());
        session.setTableId(tableId);
        session.setOpenedAt(LocalDateTime.now());
        session.setClosedAt(null);
        session.setDurationMinutes(null);

        sessions.add(session);

        table.setStatus(TableStatus.OCCUPIED);
        table.setModifiedDate(LocalDateTime.now());

        return tableMapper.toSessionDto(session);
    }

    public TableSessionDTO getActiveSession(String tableId) {
        findTableById(tableId);
        TableSessionDTO activeSession = findActiveSessionByTableId(tableId);
        if (activeSession == null) {
            throw new TableResourceNotFoundException("No active session found for table: " + tableId);
        }
        return activeSession;
    }

    public TableSessionDTO updateGuestCount(String tableId, String sessionId, Integer guestCount) {
        findTableById(tableId);

        if (guestCount == null || guestCount < 1) {
            throw new TableValidationException("Field 'guestCount' must be a positive number");
        }

        TableSessionDomain session = findSessionById(sessionId);

        if (!session.getTableId().equals(tableId)) {
            throw new TableBusinessException("Session does not belong to the specified table");
        }
        if (session.getClosedAt() != null) {
            throw new TableBusinessException("Cannot update a closed session");
        }

        session.setGuestCount(guestCount);
        return tableMapper.toSessionDto(session);
    }

    public TableSessionDTO closeSession(String tableId, String sessionId) {
        TableDomain table = findTableById(tableId);
        TableSessionDomain session = findSessionById(sessionId);

        if (!session.getTableId().equals(tableId)) {
            throw new TableBusinessException("Session does not belong to the specified table");
        }
        if (session.getClosedAt() != null) {
            throw new TableBusinessException("Session is already closed");
        }

        LocalDateTime closedAt = LocalDateTime.now();
        long durationMinutes = ChronoUnit.MINUTES.between(session.getOpenedAt(), closedAt);

        session.setClosedAt(closedAt);
        session.setDurationMinutes(durationMinutes);

        table.setStatus(TableStatus.AVAILABLE);
        table.setModifiedDate(LocalDateTime.now());

        TableSessionDTO dto = tableMapper.toSessionDto(session);
        dto.setDurationText(formatDuration(durationMinutes));
        return dto;
    }

    public List<TableSessionDTO> getSessionHistory(String tableId) {
        findTableById(tableId);

        List<TableSessionDTO> result = new ArrayList<>();
        for (TableSessionDomain session : sessions) {
            if (session.getTableId().equals(tableId)) {
                result.add(tableMapper.toSessionDto(session));
            }
        }
        return result;
    }

    // ── RESERVATIONS ──────────────────────────────────────────────────────────

    public TableReservationDTO createReservation(String tableId, TableReservationDTO request) {
        validateReservationPayload(request);

        TableDomain table = findTableById(tableId);

        if (table.getStatus() == TableStatus.OCCUPIED) {
            throw new TableBusinessException("Cannot reserve a table that is currently occupied");
        }
        if (table.getStatus() == TableStatus.RESERVED) {
            throw new TableBusinessException("Table already has an active reservation");
        }

        TableReservationDomain domain = tableMapper.toReservationDomain(request);
        domain.setId(UUID.randomUUID().toString());
        domain.setTableId(tableId);
        domain.setStatus(ReservationStatus.PENDING);
        domain.setCreatedDate(LocalDateTime.now());

        reservations.add(domain);

        table.setStatus(TableStatus.RESERVED);
        table.setModifiedDate(LocalDateTime.now());

        return tableMapper.toReservationDto(domain);
    }

    public TableReservationDTO getActiveReservation(String tableId) {
        findTableById(tableId);

        for (TableReservationDomain reservation : reservations) {
            if (reservation.getTableId().equals(tableId)
                    && reservation.getStatus() != ReservationStatus.CANCELLED
                    && reservation.getStatus() != ReservationStatus.COMPLETED) {
                return tableMapper.toReservationDto(reservation);
            }
        }
        throw new TableResourceNotFoundException("No active reservation found for table: " + tableId);
    }

    public TableReservationDTO updateReservation(String tableId, String reservationId, TableReservationDTO request) {
        validateReservationPayload(request);
        findTableById(tableId);

        TableReservationDomain existing = findReservationById(reservationId);

        if (!existing.getTableId().equals(tableId)) {
            throw new TableBusinessException("Reservation does not belong to the specified table");
        }
        if (existing.getStatus() == ReservationStatus.CANCELLED
                || existing.getStatus() == ReservationStatus.COMPLETED) {
            throw new TableBusinessException("Cannot update a cancelled or completed reservation");
        }

        existing.setReservationDate(request.getReservationDate());
        existing.setReservationTime(request.getReservationTime());
        existing.setGuestName(request.getGuestName());
        existing.setGuestCount(request.getGuestCount());

        return tableMapper.toReservationDto(existing);
    }

    public void cancelReservation(String tableId, String reservationId) {
        TableDomain table = findTableById(tableId);
        TableReservationDomain existing = findReservationById(reservationId);

        if (!existing.getTableId().equals(tableId)) {
            throw new TableBusinessException("Reservation does not belong to the specified table");
        }
        if (existing.getStatus() == ReservationStatus.CANCELLED) {
            throw new TableBusinessException("Reservation is already cancelled");
        }

        existing.setStatus(ReservationStatus.CANCELLED);

        if (table.getStatus() == TableStatus.RESERVED) {
            table.setStatus(TableStatus.AVAILABLE);
            table.setModifiedDate(LocalDateTime.now());
        }
    }

    // ── SUMMARY ───────────────────────────────────────────────────────────────

    public TableSummaryDTO getSummary() {
        return buildSummary(null);
    }

    public TableSummaryDTO getSummaryByVenue(String venueId) {
        if (venueId == null || venueId.isBlank()) {
            throw new TableValidationException("Field 'venueId' is required and cannot be empty");
        }
        return buildSummary(venueId);
    }

    // ── PRIVATE HELPERS ───────────────────────────────────────────────────────

    private TableDomain findTableById(String tableId) {
        for (TableDomain table : tables) {
            if (table.getId().equals(tableId) && Boolean.TRUE.equals(table.getActive())) {
                return table;
            }
        }
        throw new TableResourceNotFoundException("Table not found with id: " + tableId);
    }

    private TableSessionDomain findSessionById(String sessionId) {
        for (TableSessionDomain session : sessions) {
            if (session.getId().equals(sessionId)) {
                return session;
            }
        }
        throw new TableResourceNotFoundException("Session not found with id: " + sessionId);
    }

    private TableReservationDomain findReservationById(String reservationId) {
        for (TableReservationDomain reservation : reservations) {
            if (reservation.getId().equals(reservationId)) {
                return reservation;
            }
        }
        throw new TableResourceNotFoundException("Reservation not found with id: " + reservationId);
    }

    private TableSessionDTO findActiveSessionByTableId(String tableId) {
        for (TableSessionDomain session : sessions) {
            if (session.getTableId().equals(tableId) && session.getClosedAt() == null) {
                TableSessionDTO dto = tableMapper.toSessionDto(session);
                long elapsed = ChronoUnit.MINUTES.between(session.getOpenedAt(), LocalDateTime.now());
                dto.setDurationMinutes(elapsed);
                dto.setDurationText(formatDuration(elapsed));
                return dto;
            }
        }
        return null;
    }

    private TableSummaryDTO buildSummary(String venueId) {
        long total = 0, available = 0, occupied = 0, reserved = 0;

        for (TableDomain table : tables) {
            if (Boolean.FALSE.equals(table.getActive())) continue;
            if (venueId != null && !table.getVenueId().equals(venueId)) continue;

            total++;
            switch (table.getStatus()) {
                case AVAILABLE -> available++;
                case OCCUPIED  -> occupied++;
                case RESERVED  -> reserved++;
            }
        }

        TableSummaryDTO summary = new TableSummaryDTO();
        summary.setTotalRegistered(total);
        summary.setAvailable(available);
        summary.setOccupied(occupied);
        summary.setReserved(reserved);
        summary.setVenueId(venueId);
        return summary;
    }

    private TableStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return TableStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new TableValidationException("Invalid table status value. Accepted: AVAILABLE, OCCUPIED, RESERVED");
        }
    }

    private TableStatus parseRequiredStatus(String status) {
        TableStatus parsed = parseStatus(status);
        if (parsed == null) {
            throw new TableValidationException("Field 'status' is required and cannot be empty");
        }
        return parsed;
    }

    private void validateTablePayload(TableDTO request) {
        validateRequiredObject(request.getTableNumber(), "tableNumber");
        validateRequiredText(request.getLocation(), "location");
        validateRequiredObject(request.getIsVip(), "isVip");
        validateRequiredObject(request.getHasView(), "hasView");
        validateRequiredObject(request.getIsAccessible(), "isAccessible");
        validateRequiredText(request.getVenueId(), "venueId");

        if (request.getTableNumber() < 1) {
            throw new TableValidationException("Field 'tableNumber' must be a positive number");
        }
    }

    private void validateSessionPayload(TableSessionDTO request) {
        validateRequiredObject(request.getGuestCount(), "guestCount");
        if (request.getGuestCount() < 1) {
            throw new TableValidationException("Field 'guestCount' must be a positive number");
        }
    }

    private void validateReservationPayload(TableReservationDTO request) {
        validateRequiredObject(request.getReservationDate(), "reservationDate");
        validateRequiredObject(request.getReservationTime(), "reservationTime");

        if (request.getReservationDate().isBefore(java.time.LocalDate.now())) {
            throw new TableValidationException("Field 'reservationDate' cannot be in the past");
        }
    }

    private void validateTableNumberNotDuplicated(String venueId, Integer tableNumber, String excludeId) {
        for (TableDomain table : tables) {
            if (Boolean.FALSE.equals(table.getActive())) continue;
            if (excludeId != null && table.getId().equals(excludeId)) continue;
            if (table.getVenueId().equals(venueId) && table.getTableNumber().equals(tableNumber)) {
                throw new TableBusinessException("Table number " + tableNumber + " already exists in this venue");
            }
        }
    }

    private void validateRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new TableValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateRequiredObject(Object value, String fieldName) {
        if (value == null) {
            throw new TableValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private String formatDuration(long totalMinutes) {
        if (totalMinutes < 60) {
            return totalMinutes + " minute" + (totalMinutes != 1 ? "s" : "");
        }
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        if (minutes == 0) {
            return hours + " hour" + (hours != 1 ? "s" : "");
        }
        return hours + " hour" + (hours != 1 ? "s" : "") + " " + minutes + " minute" + (minutes != 1 ? "s" : "");
    }
}