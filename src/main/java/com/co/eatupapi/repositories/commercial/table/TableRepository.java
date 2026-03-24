package com.co.eatupapi.repositories.commercial.table;

import com.co.eatupapi.domain.commercial.table.ReservationStatus;
import com.co.eatupapi.domain.commercial.table.TableDomain;
import com.co.eatupapi.domain.commercial.table.TableReservationDomain;
import com.co.eatupapi.domain.commercial.table.TableSessionDomain;
import com.co.eatupapi.domain.commercial.table.TableStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TableRepository {

    public List<TableDomain> loadInitialTables() {
        LocalDateTime now = LocalDateTime.now();

        TableDomain table1 = new TableDomain();
        table1.setId("table-001");
        table1.setTableNumber(1);
        table1.setLocation("Main Hall");
        table1.setIsVip(false);
        table1.setHasView(false);
        table1.setIsAccessible(true);
        table1.setVenueId("venue-001");
        table1.setStatus(TableStatus.AVAILABLE);
        table1.setActive(true);
        table1.setCreatedDate(now.minusDays(30));
        table1.setModifiedDate(now.minusDays(1));

        TableDomain table2 = new TableDomain();
        table2.setId("table-002");
        table2.setTableNumber(2);
        table2.setLocation("Main Hall");
        table2.setIsVip(false);
        table2.setHasView(false);
        table2.setIsAccessible(false);
        table2.setVenueId("venue-001");
        table2.setStatus(TableStatus.OCCUPIED);
        table2.setActive(true);
        table2.setCreatedDate(now.minusDays(30));
        table2.setModifiedDate(now.minusHours(2));

        TableDomain table3 = new TableDomain();
        table3.setId("table-003");
        table3.setTableNumber(3);
        table3.setLocation("North Terrace");
        table3.setIsVip(false);
        table3.setHasView(true);
        table3.setIsAccessible(false);
        table3.setVenueId("venue-001");
        table3.setStatus(TableStatus.AVAILABLE);
        table3.setActive(true);
        table3.setCreatedDate(now.minusDays(30));
        table3.setModifiedDate(now.minusDays(1));

        TableDomain table4 = new TableDomain();
        table4.setId("table-004");
        table4.setTableNumber(4);
        table4.setLocation("North Terrace");
        table4.setIsVip(false);
        table4.setHasView(true);
        table4.setIsAccessible(false);
        table4.setVenueId("venue-001");
        table4.setStatus(TableStatus.RESERVED);
        table4.setActive(true);
        table4.setCreatedDate(now.minusDays(30));
        table4.setModifiedDate(now.minusHours(1));

        TableDomain table5 = new TableDomain();
        table5.setId("table-005");
        table5.setTableNumber(5);
        table5.setLocation("VIP Room");
        table5.setIsVip(true);
        table5.setHasView(true);
        table5.setIsAccessible(false);
        table5.setVenueId("venue-001");
        table5.setStatus(TableStatus.OCCUPIED);
        table5.setActive(true);
        table5.setCreatedDate(now.minusDays(30));
        table5.setModifiedDate(now.minusHours(3));

        TableDomain table6 = new TableDomain();
        table6.setId("table-006");
        table6.setTableNumber(6);
        table6.setLocation("VIP Room");
        table6.setIsVip(true);
        table6.setHasView(true);
        table6.setIsAccessible(true);
        table6.setVenueId("venue-002");
        table6.setStatus(TableStatus.AVAILABLE);
        table6.setActive(true);
        table6.setCreatedDate(now.minusDays(15));
        table6.setModifiedDate(now.minusDays(1));

        return List.of(table1, table2, table3, table4, table5, table6);
    }

    public List<TableSessionDomain> loadInitialSessions() {
        LocalDateTime now = LocalDateTime.now();

        // Active session for table-002
        TableSessionDomain session1 = new TableSessionDomain();
        session1.setId("session-001");
        session1.setTableId("table-002");
        session1.setGuestCount(3);
        session1.setWaiterId("waiter-001");
        session1.setOpenedAt(now.minusHours(2));
        session1.setClosedAt(null);
        session1.setDurationMinutes(null);
        session1.setObservations("");

        // Active session for table-005
        TableSessionDomain session2 = new TableSessionDomain();
        session2.setId("session-002");
        session2.setTableId("table-005");
        session2.setGuestCount(6);
        session2.setWaiterId("waiter-002");
        session2.setOpenedAt(now.minusHours(3));
        session2.setClosedAt(null);
        session2.setDurationMinutes(null);
        session2.setObservations("VIP guests, priority service");

        // Closed session (historical) for table-001
        TableSessionDomain session3 = new TableSessionDomain();
        session3.setId("session-003");
        session3.setTableId("table-001");
        session3.setGuestCount(4);
        session3.setWaiterId("waiter-001");
        session3.setOpenedAt(now.minusDays(1).withHour(12).withMinute(30));
        session3.setClosedAt(now.minusDays(1).withHour(14).withMinute(15));
        session3.setDurationMinutes(105L);
        session3.setObservations("");

        return List.of(session1, session2, session3);
    }

    public List<TableReservationDomain> loadInitialReservations() {
        LocalDateTime now = LocalDateTime.now();

        // Active reservation for table-004
        TableReservationDomain reservation1 = new TableReservationDomain();
        reservation1.setId("reservation-001");
        reservation1.setTableId("table-004");
        reservation1.setReservationDate(LocalDate.now());
        reservation1.setReservationTime(LocalTime.of(20, 0));
        reservation1.setGuestName("Carlos Gomez");
        reservation1.setGuestCount(3);
        reservation1.setStatus(ReservationStatus.CONFIRMED);
        reservation1.setCreatedDate(now.minusHours(5));

        // Future reservation
        TableReservationDomain reservation2 = new TableReservationDomain();
        reservation2.setId("reservation-002");
        reservation2.setTableId("table-003");
        reservation2.setReservationDate(LocalDate.now().plusDays(2));
        reservation2.setReservationTime(LocalTime.of(19, 30));
        reservation2.setGuestName("Maria Torres");
        reservation2.setGuestCount(5);
        reservation2.setStatus(ReservationStatus.PENDING);
        reservation2.setCreatedDate(now.minusHours(1));

        return List.of(reservation1, reservation2);
    }
}