package com.co.eatupapi.repositories.user;

import com.co.eatupapi.domain.user.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {
    List<City> findByDepartmentId(UUID departmentId);
    boolean existsByIdAndDepartmentId(UUID id, UUID departmentId);
}
