package com.tiendita.tienditainventario.repository;

import com.tiendita.tienditainventario.model.VentasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentasRepository extends JpaRepository<VentasEntity, Long> {

    List<VentasEntity> findByProductoId(Long productoId);
    
    List<VentasEntity> findByUsuarioId(Long usuarioId);
    
    @Query("SELECT COALESCE(SUM(v.cantidad), 0) FROM VentasEntity v WHERE v.productoId = :productoId")
    Long getTotalCantidadVendidaPorProducto(@Param("productoId") Long productoId);
}
