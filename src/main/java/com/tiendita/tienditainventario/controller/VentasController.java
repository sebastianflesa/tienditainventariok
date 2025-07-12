package com.tiendita.tienditainventario.controller;

import com.tiendita.tienditainventario.model.VentasEntity;
import com.tiendita.tienditainventario.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentasController {
    
    @Autowired
    private VentasRepository ventasRepository;

    @GetMapping
    public ResponseEntity<List<VentasEntity>> getAllVentas() {
        List<VentasEntity> ventas = ventasRepository.findAll();
        return ResponseEntity.ok(ventas);
    }
 
}
