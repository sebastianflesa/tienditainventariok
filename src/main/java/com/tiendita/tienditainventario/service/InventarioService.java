package com.tiendita.tienditainventario.service;

import com.tiendita.tienditainventario.model.Producto;
import com.tiendita.tienditainventario.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InventarioService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Transactional
    public boolean procesarVenta(Long productoId, int cantidad) {
        try {
            System.out.println("VENTA RECIBIDA: " + productoId + ", CANTIDAD: " + cantidad);
            Optional<Producto> productoOpt = productoRepository.findById(productoId);
            if (productoOpt.isEmpty()) {
                System.out.println("PRODUCTO NO ENCONTRADO: " + productoId);
                return false;
            }
            
            Producto producto = productoOpt.get();
            
            if (producto.getStock() < cantidad) {
                System.out.println("STOCK INSUFICIENTE PARA PRODUCTO ID: " + productoId + ". Stock actual: " + producto.getStock() + ", Cantidad solicitada: " + cantidad);
                return false;
            }
            
            int filasActualizadas = productoRepository.reducirStock(productoId, (long) cantidad);
            
            if (filasActualizadas > 0) {
                Long stockActual = obtenerStock(productoId);
                kafkaProducerService.enviarMensajeStock(productoId, stockActual);
                return true;
            } else {
                //log.error("No se pudo reducir el stock del producto ID: {}", productoId);
                return false;
            }
            
        } catch (Exception e) {
            //log.error("Error al procesar venta para producto ID: {} - {}", productoId, e.getMessage());
            return false;
        }
    }

    public Long obtenerStock(Long productoId) {
        return productoRepository.obtenerStock(productoId).orElse(0L);
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }


    
}
