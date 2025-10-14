package com.grupo3.airbnb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo3.airbnb.entity.Anfitrion;
import com.grupo3.airbnb.entity.HostGuestReview;
import com.grupo3.airbnb.entity.Reserva;
import com.grupo3.airbnb.repository.IHostGuestReviewRepository;

@Service
public class HostGuestReviewService {

    @Autowired
    private IHostGuestReviewRepository repo;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private AnfitrionService anfitrionService;

    public HostGuestReview crear(Long anfitrionDni, Long reservaId, String comentario) {
        Reserva reserva = reservaService.getReserva(reservaId);
        Anfitrion anfitrion = anfitrionService.getAnfitrionByDni(anfitrionDni);

        HostGuestReview hgr = new HostGuestReview();
        hgr.setAnfitrion(anfitrion);
        hgr.setReserva(reserva);
        hgr.setHuesped(reserva.getHuesped());
        hgr.setComentario(comentario);
        return repo.save(hgr);
    }

    public List<HostGuestReview> listarPorHuesped(String usuario) {
        return repo.findByHuespedOrderByFechaCreacionDesc(usuario);
    }
}

