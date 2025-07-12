package com.tiendita.tienditainventario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCTOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    
    @Id
    @Column(name = "ID", precision = 19, scale = 0)
    private Long id;
    
    @Column(name = "NOMBRE", length = 255)
    private String nombre;
    
    @Column(name = "STOCK", precision = 38, scale = 0)
    private Long stock;
    
    @Column(name = "VALOR", precision = 10, scale = 0)
    private Long valor;

    public boolean tieneStockSuficiente(int cantidadRequerida) {
        return this.stock != null && this.stock >= cantidadRequerida;
    }

    public void reducirStock(int cantidad) {
        if (this.stock != null && this.stock >= cantidad) {
            this.stock -= cantidad;
        } else {
            throw new IllegalArgumentException("Stock insuficiente para reducir la cantidad solicitada");
        }
    }

    public void aumentarStock(int cantidad) {
        if (this.stock == null) {
            this.stock = (long) cantidad;
        } else {
            this.stock += cantidad;
        }
    }
}
