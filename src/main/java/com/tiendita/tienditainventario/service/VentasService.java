package com.tiendita.tienditainventario.service;

import com.tiendita.tienditainventario.model.Venta;
import com.tiendita.tienditainventario.model.VentasEntity;
import com.tiendita.tienditainventario.repository.VentasRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class VentasService {
    
    @Autowired
    private VentasRepository ventasRepository;
    
    @Transactional
    public VentasEntity guardarVenta(Venta venta) {
        try {
            VentasEntity ventasEntity = new VentasEntity();
            ventasEntity.setProductoId(Long.parseLong(venta.getProductoId()));
            ventasEntity.setCantidad((long) venta.getCantidad());
            ventasEntity.setUsuarioId(Long.parseLong(venta.getClienteId()));
            VentasEntity savedEntity = ventasRepository.save(ventasEntity);
            System.out.println("VENTA GUARDADA: " + savedEntity.getId());
            return savedEntity;
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save venta", e);
        }
    }
    
    public Long getTotalCantidadVendida(Long productoId) {
        return ventasRepository.getTotalCantidadVendidaPorProducto(productoId);
    }
}
