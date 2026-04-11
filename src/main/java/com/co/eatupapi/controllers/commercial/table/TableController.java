package com.co.eatupapi.controllers.commercial.table;

import com.co.eatupapi.dto.commercial.table.TableDTO;
import com.co.eatupapi.dto.commercial.table.TableReservationDTO;
import com.co.eatupapi.dto.commercial.table.TableSessionDTO;
import com.co.eatupapi.dto.commercial.table.TableSessionUpdateDTO;
import com.co.eatupapi.dto.commercial.table.TableSummaryDTO;
import com.co.eatupapi.services.commercial.table.TableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/commercial/api/v1/tables")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<TableDTO> createTable(@Valid @RequestBody TableDTO request) {
        TableDTO saved = tableService.createTable(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<TableDTO>> getTables(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String venueId,
            @RequestParam(required = false) Boolean reserved,
            @RequestParam(required = false) Boolean canOpenNow) {
        List<TableDTO> tables = tableService.getTables(status, venueId, reserved, canOpenNow);
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<TableDTO> getTableById(@PathVariable String tableId) {
        TableDTO table = tableService.getTableById(tableId);
        return ResponseEntity.ok(table);
    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<TableDTO>> getTablesByVenue(@PathVariable String venueId) {
        List<TableDTO> tables = tableService.getTablesByVenue(venueId);
        return ResponseEntity.ok(tables);
    }

    @PutMapping("/{tableId}")
    public ResponseEntity<TableDTO> updateTable(@PathVariable String tableId,
                                                @Valid @RequestBody TableDTO request) {
        TableDTO updated = tableService.updateTable(tableId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{tableId}")
    public ResponseEntity<Map<String, Object>> deactivateTable(@PathVariable String tableId) {
        tableService.deactivateTable(tableId);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "La mesa fue desactivada correctamente");
        body.put("status", HttpStatus.OK.value());
        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/{tableId}/session")
    public ResponseEntity<TableSessionDTO> openSession(@PathVariable String tableId,
                                                       @Valid @RequestBody TableSessionDTO request) {
        TableSessionDTO session = tableService.openSession(tableId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @GetMapping("/{tableId}/session/active")
    public ResponseEntity<TableSessionDTO> getActiveSession(@PathVariable String tableId) {
        TableSessionDTO session = tableService.getActiveSession(tableId);
        return ResponseEntity.ok(session);
    }

    @PatchMapping("/{tableId}/session/{sessionId}")
    public ResponseEntity<TableSessionDTO> updateGuestCount(@PathVariable String tableId,
                                                            @PathVariable String sessionId,
                                                            @Valid @RequestBody TableSessionUpdateDTO request) {
        TableSessionDTO updated = tableService.updateGuestCount(tableId, sessionId, request.getGuestCount());
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{tableId}/session/{sessionId}/close")
    public ResponseEntity<TableSessionDTO> closeSession(@PathVariable String tableId,
                                                        @PathVariable String sessionId) {
        TableSessionDTO closed = tableService.closeSession(tableId, sessionId);
        return ResponseEntity.ok(closed);
    }

    @GetMapping("/{tableId}/history")
    public ResponseEntity<List<TableSessionDTO>> getSessionHistory(@PathVariable String tableId) {
        List<TableSessionDTO> history = tableService.getSessionHistory(tableId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/{tableId}/reservation")
    public ResponseEntity<TableReservationDTO> createReservation(@PathVariable String tableId,
                                                                 @Valid @RequestBody TableReservationDTO request) {
        TableReservationDTO reservation = tableService.createReservation(tableId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/{tableId}/reservation")
    public ResponseEntity<TableReservationDTO> getActiveReservation(@PathVariable String tableId) {
        TableReservationDTO reservation = tableService.getActiveReservation(tableId);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/{tableId}/reservation/{reservationId}")
    public ResponseEntity<TableReservationDTO> updateReservation(@PathVariable String tableId,
                                                                 @PathVariable String reservationId,
                                                                 @Valid @RequestBody TableReservationDTO request) {
        TableReservationDTO updated = tableService.updateReservation(tableId, reservationId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{tableId}/reservation/{reservationId}")
    public ResponseEntity<Map<String, Object>> cancelReservation(@PathVariable String tableId,
                                                                 @PathVariable String reservationId) {
        tableService.cancelReservation(tableId, reservationId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "La reserva fue cancelada correctamente");
        body.put("status", HttpStatus.OK.value());
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(body);
    }

    @GetMapping("/reservations/search")
    public ResponseEntity<List<TableReservationDTO>> searchReservationsByGuestDocumentNumber(
            @RequestParam String guestDocumentNumber) {
        List<TableReservationDTO> reservations = tableService.searchReservationsByGuestDocumentNumber(guestDocumentNumber);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/reservations/{reservationId}/seat")
    public ResponseEntity<TableSessionDTO> seatReservation(@PathVariable String reservationId,
                                                           @Valid @RequestBody TableSessionDTO request) {
        TableSessionDTO session = tableService.seatReservation(reservationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @GetMapping("/summary")
    public ResponseEntity<TableSummaryDTO> getSummary() {
        TableSummaryDTO summary = tableService.getSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/venue/{venueId}")
    public ResponseEntity<TableSummaryDTO> getSummaryByVenue(@PathVariable String venueId) {
        TableSummaryDTO summary = tableService.getSummaryByVenue(venueId);
        return ResponseEntity.ok(summary);
    }
}
