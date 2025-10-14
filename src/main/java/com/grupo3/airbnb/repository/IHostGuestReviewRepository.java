package com.grupo3.airbnb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo3.airbnb.entity.HostGuestReview;

public interface IHostGuestReviewRepository extends JpaRepository<HostGuestReview, Long> {
    List<HostGuestReview> findByHuespedOrderByFechaCreacionDesc(String huesped);
}

