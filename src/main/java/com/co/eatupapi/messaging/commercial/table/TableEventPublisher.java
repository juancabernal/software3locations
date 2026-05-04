package com.co.eatupapi.messaging.commercial.table;

import com.co.eatupapi.dto.commercial.table.TableDTO;
import com.co.eatupapi.dto.commercial.table.TableReservationDTO;
import com.co.eatupapi.dto.commercial.table.TableSessionDTO;

public interface TableEventPublisher {

    void publishTableCreated(TableDTO table);

    void publishTableUpdated(TableDTO table);

    void publishTableDeactivated(String tableId);

    void publishSessionOpened(TableSessionDTO session);

    void publishSessionClosed(TableSessionDTO session);

    void publishSessionUpdated(String tableId, String sessionId, Integer guestCount);

    void publishReservationCreated(TableReservationDTO reservation);

    void publishReservationUpdated(TableReservationDTO reservation);

    void publishReservationCancelled(String tableId, String reservationId);

    void publishReservationSeated(TableSessionDTO session);
}