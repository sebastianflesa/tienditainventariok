package com.tiendita.tienditainventario.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    private String id;
    private String productoId;
    private String clienteId;
    private int cantidad;
    private double precio;
    private double total;
    private LocalDateTime fechaVenta;
    private String estado;
}
