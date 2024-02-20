/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Date;
import java.lang.reflect.Field;

/**
 *
 * @author Christian
 */
public class User {

    private int id;
    private String ci;
    private String name;
    private String lastname;
    private Date birth_date;
    private int celular;
    private String tipo;
    private String genero;
    private String residencia_actual;
    private String email;
    private String password;
    private String url_foto;
    private float sueldo;
    private String formacion;
    private String nit;
    private String razon_social;

    public User() {
    }

    public User(int id, String ci, String name, String lastname, Date birth_date, int celular, String tipo, String genero, String residencia, String email, String password, String url_foto, float sueldo, String formacion, String nit, String razon_social) {
        this.id = id;
        this.ci = ci;
        this.name = name;
        this.lastname = lastname;
        this.birth_date = birth_date;
        this.celular = celular;
        this.tipo = tipo;
        this.genero = genero;
        this.residencia_actual = residencia;
        this.email = email;
        this.password = password;
        this.url_foto = url_foto;
        this.sueldo = sueldo;
        this.formacion = formacion;
        this.nit = nit;
        this.razon_social = razon_social;
    }

    public User(String ci, String name, String lastname, Date birth_date, int celular, String tipo, String genero, String residencia, String email, String password, String url_foto, float sueldo, String formacion, String nit, String razon_social) {
        this.id = id;
        this.ci = ci;
        this.name = name;
        this.lastname = lastname;
        this.birth_date = birth_date;
        this.celular = celular;
        this.tipo = tipo;
        this.genero = genero;
        this.residencia_actual = residencia;
        this.email = email;
        this.password = password;
        this.url_foto = url_foto;
        this.sueldo = sueldo;
        this.formacion = formacion;
        this.nit = nit;
        this.razon_social = razon_social;
    }

    public void setAtributo(String atributo, Object valor) throws NoSuchFieldException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(atributo);
        field.setAccessible(true);
        field.set(this, valor);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public int getCelular() {
        return celular;
    }

    public void setCelular(int celular) {
        this.celular = celular;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getResidencia() {
        return residencia_actual;
    }

    public void setResidencia(String residencia) {
        this.residencia_actual = residencia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    public float getSueldo() {
        return sueldo;
    }

    public void setSueldo(float sueldo) {
        this.sueldo = sueldo;
    }

    public String getFormacion() {
        return formacion;
    }

    public void setFormacion(String formacion) {
        this.formacion = formacion;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

}
