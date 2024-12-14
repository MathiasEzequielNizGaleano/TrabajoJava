package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Producto;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final List<Producto> listaProductos = new ArrayList<>();
    private long idCounter = 3;

    @GetMapping
    public List<Producto> obtenerProductos() {
        return listaProductos;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long id){
        try {
            Producto producto = listaProductos.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        if (producto.getprecio() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("El precio debe ser mayor a 0.");
        }
        if (producto.getstock() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("El stock no puede ser negativo.");
        }
    
        for (Producto item : listaProductos) {
            if (item.getId().equals(id)) {
                
                item.setNombre(producto.getNombre());
                item.setstock(producto.getstock());
                item.setprecio(producto.getprecio());
        
                String estado = producto.getstock() > 0 ? "Disponible" : "Agotado";
                item.setestado(estado);
    
                return ResponseEntity.ok(item); 
            }
        }
    
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("Producto con ID " + id + " no encontrado.");
    }
    

        
    @DeleteMapping("/{id}")
    public String elimnarProducto(@PathVariable Long id){
        for(Producto item : listaProductos) {
            if( item.getId().equals(id)){
                listaProductos.remove(item);
                return "Producto eliminado";
            }
        }
        return "Producto no encontrado";
    }
    
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        
        int cantidadTotal = listaProductos.size();

        double promedioPrecios = listaProductos.stream()
            .mapToInt(Producto::getprecio) 
            .average() 
            .orElse(0); 

        long productosDisponibles = listaProductos.stream()
            .filter(producto -> producto.getestado().equals("Disponible"))
            .count();

        long productosAgotados = listaProductos.stream()
            .filter(producto -> producto.getestado().equals("Agotado"))
            .count();

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("Cantidad Total", cantidadTotal);
        estadisticas.put("Promedio Precios", promedioPrecios);
        estadisticas.put("Productos Disponibles", productosDisponibles);
        estadisticas.put("Productos Agotados", productosAgotados);

        return ResponseEntity.ok(estadisticas);
    }


    @PostMapping
    public ResponseEntity<?> agregarProducto(@Valid @RequestBody Producto producto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errores = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errores.append(error.getDefaultMessage()).append(" ");
            }
            return ResponseEntity.badRequest().body(errores.toString());
        }

        producto.setId(idCounter++);
        
        listaProductos.add(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    
}


