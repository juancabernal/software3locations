package com.co.eatupapi.messaging.commercial.table;

import com.co.eatupapi.dto.commercial.table.TableDTO;
import com.co.eatupapi.dto.commercial.table.TableReservationDTO;
import com.co.eatupapi.dto.commercial.table.TableSessionDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class TableEventPublisherBroker implements TableEventPublisher {

    private final RabbitTemplate rabbitTemplate; // CORRECCIÓN: Sin ObjectMapper

    @Value("${rabbitmq.exchange.commercial}")
    private String exchange;

    @Value("${rabbitmq.routing-key.table}")
    private String routingKey;

    public TableEventPublisherBroker(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // ── Table ─────────────────────────────────────────────────────────────────

    @Override
    public void publishTableCreated(TableDTO table) {
        sendEvent("TABLE_CREATED", null, null, null, table);
    }

    @Override
    public void publishTableUpdated(TableDTO table) {
        sendEvent("TABLE_UPDATED", table.getId(), null, null, table);
    }

    @Override
    public void publishTableDeactivated(String tableId) {
        sendEvent("TABLE_DEACTIVATED", tableId, null, null, null);
    }

    // ── Session ───────────────────────────────────────────────────────────────

    @Override
    public void publishSessionOpened(TableSessionDTO session) {
        sendEvent("TABLE_SESSION_OPENED", session.getTableId(), null, null, session);
    }

    @Override
    public void publishSessionClosed(TableSessionDTO session) {
        sendEvent("TABLE_SESSION_CLOSED", session.getTableId(), session.getId(), null, null);
    }

    @Override
    public void publishSessionUpdated(String tableId, String sessionId, Integer guestCount) {
        Map<String, Object> payload = Map.of("guestCount", guestCount);
        sendEvent("TABLE_SESSION_UPDATED", tableId, sessionId, null, payload);
    }

    // ── Reservation ───────────────────────────────────────────────────────────

    @Override
    public void publishReservationCreated(TableReservationDTO reservation) {
        sendEvent("TABLE_RESERVATION_CREATED", reservation.getTableId(), null, null, reservation);
    }

    @Override
    public void publishReservationUpdated(TableReservationDTO reservation) {
        sendEvent("TABLE_RESERVATION_UPDATED", reservation.getTableId(), null, reservation.getId(), reservation);
    }

    @Override
    public void publishReservationCancelled(String tableId, String reservationId) {
        sendEvent("TABLE_RESERVATION_CANCELLED", tableId, null, reservationId, null);
    }

    @Override
    public void publishReservationSeated(TableSessionDTO session) {
        sendEvent("TABLE_RESERVATION_SEATED", null, null, session.getReservationId(), session);
    }

    // ── Core ──────────────────────────────────────────────────────────────────

    private void sendEvent(String eventType,
                           String tableId,
                           String sessionId,
                           String reservationId,
                           Object payloadObj) {

        TableCommandEvent event = new TableCommandEvent();
        event.setEventType(eventType);
        event.setTableId(tableId);
        event.setSessionId(sessionId);
        event.setReservationId(reservationId);
        event.setOccurredAt(LocalDateTime.now());

        if (payloadObj != null) {
            event.setPayload(payloadObj); // CORRECCIÓN: Asignación directa
        }

        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}