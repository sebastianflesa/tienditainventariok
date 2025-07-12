package com.tiendita.tienditainventario.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiendita.tienditainventario.dto.StockMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void enviarMensajeStock(Long productoId, Long stockActual) {
        try {
            StockMessage message = new StockMessage(productoId, stockActual);
            String messageJson = objectMapper.writeValueAsString(message);
            kafkaTemplate.send("stock", messageJson);
        } catch (Exception e) {
            log.error("Error al enviar mensaje de stock para producto {}: {}", productoId, e.getMessage());
        }
    }
}
