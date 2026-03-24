package com.co.eatupapi.utils.commercial.table.mapper;

import com.co.eatupapi.domain.commercial.table.TableDomain;
import com.co.eatupapi.domain.commercial.table.TableReservationDomain;
import com.co.eatupapi.domain.commercial.table.TableSessionDomain;
import com.co.eatupapi.dto.commercial.table.TableDTO;
import com.co.eatupapi.dto.commercial.table.TableReservationDTO;
import com.co.eatupapi.dto.commercial.table.TableSessionDTO;
import org.springframework.stereotype.Component;

@Component
public class TableMapper {

    // TABLE

    public TableDTO toDto(TableDomain domain) {
        TableDTO dto = new TableDTO();
        dto.setId(domain.getId());
        dto.setTableNumber(domain.getTableNumber());
        dto.setLocation(domain.getLocation());
        dto.setIsVip(domain.getIsVip());
        dto.setHasView(domain.getHasView());
        dto.setIsAccessible(domain.getIsAccessible());
        dto.setVenueId(domain.getVenueId());
        dto.setStatus(domain.getStatus());
        dto.setActive(domain.getActive());
        dto.setCreatedDate(domain.getCreatedDate());
        dto.setModifiedDate(domain.getModifiedDate());
        return dto;
    }

    public TableDomain toDomain(TableDTO dto) {
        TableDomain domain = new TableDomain();
        domain.setId(dto.getId());
        domain.setTableNumber(dto.getTableNumber());
        domain.setLocation(dto.getLocation());
        domain.setIsVip(dto.getIsVip());
        domain.setHasView(dto.getHasView());
        domain.setIsAccessible(dto.getIsAccessible());
        domain.setVenueId(dto.getVenueId());
        domain.setStatus(dto.getStatus());
        domain.setActive(dto.getActive());
        domain.setCreatedDate(dto.getCreatedDate());
        domain.setModifiedDate(dto.getModifiedDate());
        return domain;
    }

    // SESSION
    public TableSessionDTO toSessionDto(TableSessionDomain domain) {
        TableSessionDTO dto = new TableSessionDTO();
        dto.setId(domain.getId());
        dto.setTableId(domain.getTableId());
        dto.setGuestCount(domain.getGuestCount());
        dto.setWaiterId(domain.getWaiterId());
        dto.setOpenedAt(domain.getOpenedAt());
        dto.setClosedAt(domain.getClosedAt());
        dto.setDurationMinutes(domain.getDurationMinutes());
        dto.setObservations(domain.getObservations());
        return dto;
    }

    public TableSessionDomain toSessionDomain(TableSessionDTO dto) {
        TableSessionDomain domain = new TableSessionDomain();
        domain.setId(dto.getId());
        domain.setTableId(dto.getTableId());
        domain.setGuestCount(dto.getGuestCount());
        domain.setWaiterId(dto.getWaiterId());
        domain.setOpenedAt(dto.getOpenedAt());
        domain.setClosedAt(dto.getClosedAt());
        domain.setDurationMinutes(dto.getDurationMinutes());
        domain.setObservations(dto.getObservations());
        return domain;
    }

    // RESERVATION

    public TableReservationDTO toReservationDto(TableReservationDomain domain) {
        TableReservationDTO dto = new TableReservationDTO();
        dto.setId(domain.getId());
        dto.setTableId(domain.getTableId());
        dto.setReservationDate(domain.getReservationDate());
        dto.setReservationTime(domain.getReservationTime());
        dto.setGuestName(domain.getGuestName());
        dto.setGuestCount(domain.getGuestCount());
        dto.setStatus(domain.getStatus());
        dto.setCreatedDate(domain.getCreatedDate());
        return dto;
    }

    public TableReservationDomain toReservationDomain(TableReservationDTO dto) {
        TableReservationDomain domain = new TableReservationDomain();
        domain.setId(dto.getId());
        domain.setTableId(dto.getTableId());
        domain.setReservationDate(dto.getReservationDate());
        domain.setReservationTime(dto.getReservationTime());
        domain.setGuestName(dto.getGuestName());
        domain.setGuestCount(dto.getGuestCount());
        domain.setStatus(dto.getStatus());
        domain.setCreatedDate(dto.getCreatedDate());
        return domain;
    }
}