package com.tiendita.tienditainventario.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tiendita.tienditainventario.model.Venta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@Slf4j
public class VentasKafkaConsumerService {

    @Autowired
    private InventarioService inventarioService;
    
    @Autowired
    private VentasService ventasService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Consume mensajes del topic de ventas y procesa la reducción de stock
     */
    @KafkaListener(topics = "${kafka.topic.ventas}", groupId = "${spring.kafka.consumer.group-id}")
    public void procesarVenta(@Payload String message,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                             @Header(KafkaHeaders.OFFSET) long offset,
                             Acknowledgment acknowledgment) {
        try {
            Venta venta = objectMapper.readValue(message, Venta.class);
            boolean ventaGuardada = guardarVentaEnBD(venta);
            
            boolean stockReducido = procesarReduccionStock(venta);
            boolean exitoso = ventaGuardada && stockReducido;
            
            if (exitoso) {
                acknowledgment.acknowledge();
                //log.info("Venta procesada exitosamente: {}", venta.getId());
            } else {
                //log.error("Error al procesar la venta: {}. El mensaje no será confirmado.", venta.getId());
            }
            
        } catch (JsonProcessingException e) {
            acknowledgment.acknowledge();
        } catch (Exception e) {
            acknowledgment.acknowledge();
        }
    }

    private boolean guardarVentaEnBD(Venta venta) {
        try {
            ventasService.guardarVenta(venta);
            return true;
        } catch (Exception e) {
            log.error("Error al guardar venta en BD: {}", e.getMessage());
            return false;
        }
    }


    private boolean procesarReduccionStock(Venta venta) {
        try {
            if (venta.getProductoId() == null || venta.getProductoId().isEmpty()) {
                return false;
            }
            
            if (venta.getCantidad() <= 0) {
                return false;
            }
            
            Long productoId;
            try {
                productoId = Long.parseLong(venta.getProductoId());
            } catch (NumberFormatException e) {
                return false;
            }
            
            boolean resultado = inventarioService.procesarVenta(productoId, venta.getCantidad());
            
            if (resultado) {
                System.out.println("STOCK MODIFICADO");
            }
            
            return resultado;
            
        } catch (Exception e) {
            log.error("Error al procesar reducción de stock para venta {}: {}", venta.getId(), e.getMessage());
            return false;
        }
    }
}
