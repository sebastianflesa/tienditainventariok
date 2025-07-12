package com.tiendita.tienditainventario.repository;

import com.tiendita.tienditainventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    @Modifying
    @Transactional
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad WHERE p.id = :productoId AND p.stock >= :cantidad")
    int reducirStock(@Param("productoId") Long productoId, @Param("cantidad") Long cantidad);
    
    @Query("SELECT p.stock FROM Producto p WHERE p.id = :productoId")
    Optional<Long> obtenerStock(@Param("productoId") Long productoId);
}
