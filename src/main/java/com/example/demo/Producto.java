package com.example.demo;

public class Producto {
    
    private Long id;
    private String nombre;
    private int stock;
    private int precio;
    private String estado;


    public Producto(Long id, String nombre, int stock, int precio, String estado){
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
        this.estado = estado;
    }


    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    } 

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    } 

    public int getstock(){
        return stock;
    }

    public void setstock(int stock){
        this.stock = stock;
    }

    public int getprecio(){
        return precio;
    }

    public void setprecio(int precio){
        this.precio = precio;
    }  

    public String getestado(){
        return estado;
    }

    public void setestado(String estado){
        this.estado = estado;
    }
}
