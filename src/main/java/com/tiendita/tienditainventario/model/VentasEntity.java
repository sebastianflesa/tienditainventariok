package com.tiendita.tienditainventario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VENTAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentasEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ventas_seq_gen")
    @SequenceGenerator(name = "ventas_seq_gen", sequenceName = "VENTAS_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "PRODUCTO_ID", nullable = false)
    private Long productoId;
    
    @Column(name = "CANTIDAD", nullable = false)
    private Long cantidad;
    
    @Column(name = "USUARIO_ID", nullable = false)
    private Long usuarioId;
}
