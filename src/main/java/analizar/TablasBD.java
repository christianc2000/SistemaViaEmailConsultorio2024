/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizar;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Christian
 */
public class TablasBD {

    public ArrayList<Tabla> tablas;

    public TablasBD() {
        this.tablas = new ArrayList<>();
        //User
        ArrayList<Atributo> atributosUser = new ArrayList<>();
        atributosUser.add(new Atributo("id", "int", true,"PK"));
        atributosUser.add(new Atributo("ci", "string", false,null));
        atributosUser.add(new Atributo("name", "string", false,null));
        atributosUser.add(new Atributo("lastname", "string", false,null));
        atributosUser.add(new Atributo("birth_date", "date", false,null));
        atributosUser.add(new Atributo("celular", "int", false,null));
        atributosUser.add(new Atributo("tipo", "string", false,null));
        atributosUser.add(new Atributo("genero", "string", false,null));
        atributosUser.add(new Atributo("residencia_actual", "string", false,null));
        atributosUser.add(new Atributo("email", "email", false,null));
        atributosUser.add(new Atributo("password", "string", false,null));
        atributosUser.add(new Atributo("url_foto", "string", false,null));
        atributosUser.add(new Atributo("sueldo", "float", true,null));
        atributosUser.add(new Atributo("formacion", "string", true,null));
        atributosUser.add(new Atributo("nit", "string", true,null));
        atributosUser.add(new Atributo("razon_social", "string", true,null));

        this.tablas.add(new Tabla("users", atributosUser,11));

        //Dia
        ArrayList<Atributo> atributosDia = new ArrayList<>();
        atributosDia.add(new Atributo("id", "int", true,"PK"));
        atributosDia.add(new Atributo("nombre", "string", false,null));

        this.tablas.add(new Tabla("dias", atributosDia,1));

        //Horario
        ArrayList<Atributo> atributosHorario = new ArrayList<>();
        atributosHorario.add(new Atributo("id", "int", true,"PK"));
        atributosHorario.add(new Atributo("hora_inicio", "time", false,null));
        atributosHorario.add(new Atributo("hora_fin", "time", false,null));

        this.tablas.add(new Tabla("horarios", atributosHorario,2));

        //Turno
        ArrayList<Atributo> atributosTurno = new ArrayList<>();
        atributosTurno.add(new Atributo("id", "int", true,"PK"));
        atributosTurno.add(new Atributo("estado", "int", false,null));
        atributosTurno.add(new Atributo("dia_id", "int", false,"FK"));
        atributosTurno.add(new Atributo("horario_id", "int", false,"FK"));

        this.tablas.add(new Tabla("turnos", atributosTurno,3));

        //Servicio
        ArrayList<Atributo> atributosServicio = new ArrayList<>();
        atributosServicio.add(new Atributo("id", "int", true,"PK"));
        atributosServicio.add(new Atributo("nombre", "string", false,null));
        atributosServicio.add(new Atributo("costo", "float", false,null));
        atributosServicio.add(new Atributo("forma_compra", "string", false,null));
        atributosServicio.add(new Atributo("url_imagen", "string", false,null));
        atributosServicio.add(new Atributo("atencion", "string", false,null));

        this.tablas.add(new Tabla("servicios", atributosServicio,5));

        //Sala
        ArrayList<Atributo> atributosSala = new ArrayList<>();
        atributosSala.add(new Atributo("id", "int", true,"PK"));
        atributosSala.add(new Atributo("nro", "string", false,null));

        this.tablas.add(new Tabla("salas", atributosSala,1));

        //Atencion
        ArrayList<Atributo> atributosAtencion = new ArrayList<>();
        atributosAtencion.add(new Atributo("id", "int", true,"PK"));
        atributosAtencion.add(new Atributo("estado", "boolean", false,null));
        atributosAtencion.add(new Atributo("user_id", "int", false,"FK"));
        atributosAtencion.add(new Atributo("servicio_id", "int", false,"FK"));
        atributosAtencion.add(new Atributo("turno_id", "int", false,"FK"));
        atributosAtencion.add(new Atributo("sala_id", "int", false,"FK"));

        this.tablas.add(new Tabla("atencions", atributosAtencion,5));

        //Cita
        ArrayList<Atributo> atributosCita = new ArrayList<>();
        atributosCita.add(new Atributo("id", "int", true,"PK"));
        atributosCita.add(new Atributo("fecha", "datetime", false,null));
        atributosCita.add(new Atributo("estado", "boolean", false,null));
        atributosCita.add(new Atributo("costo", "float", false,null));
        atributosCita.add(new Atributo("paciente_id", "int", false,"FK"));
        atributosCita.add(new Atributo("medico_id", "int", false,"FK"));

        this.tablas.add(new Tabla("citas", atributosCita,5));

        //Ficha
        ArrayList<Atributo> atributosFicha = new ArrayList<>();
        atributosFicha.add(new Atributo("id", "int", true,"PK"));
        atributosFicha.add(new Atributo("fecha", "datetime", false,null));
        atributosFicha.add(new Atributo("cantidad_ficha", "int", false,null));
        atributosFicha.add(new Atributo("stock", "int", true,null));
        atributosFicha.add(new Atributo("hora_inicio", "time", true,null));
        atributosFicha.add(new Atributo("hora_fin", "time", true,null));
        atributosFicha.add(new Atributo("costo", "float", true,null));
        atributosFicha.add(new Atributo("atencion_id", "int", false,"FK"));

        this.tablas.add(new Tabla("fichas", atributosFicha,3));

        //FormaPago
        ArrayList<Atributo> atributosFormaPago = new ArrayList<>();
        atributosFormaPago.add(new Atributo("id", "int", true,"PK"));
        atributosFormaPago.add(new Atributo("nombre", "string", false,null));

        this.tablas.add(new Tabla("forma_pagos", atributosFormaPago,1));

        //Orden
        ArrayList<Atributo> atributosOrden = new ArrayList<>();
        atributosOrden.add(new Atributo("id", "int", true,"PK"));
        atributosOrden.add(new Atributo("servicio", "string", true,null));
        atributosOrden.add(new Atributo("medico", "string", true,null));
        atributosOrden.add(new Atributo("sala", "string", true,null));
        atributosOrden.add(new Atributo("monto_total", "float", true,null));
        atributosOrden.add(new Atributo("fecha_pago", "date", true,null));
        atributosOrden.add(new Atributo("nit", "string", false,null));
        atributosOrden.add(new Atributo("razon_social", "string", false,null));
        atributosOrden.add(new Atributo("email", "email", false,null));
        atributosOrden.add(new Atributo("celular", "int", false,null));
        atributosOrden.add(new Atributo("ficha_id", "int", false,"FK"));
        atributosOrden.add(new Atributo("forma_pago_id", "int", false,"FK"));
        atributosOrden.add(new Atributo("paciente_id", "int", false,"FK"));

        this.tablas.add(new Tabla("ordens", atributosOrden,7));

        //Historial
        ArrayList<Atributo> atributosHistorial = new ArrayList<>();
        atributosHistorial.add(new Atributo("id", "int", true,"PK"));
        atributosHistorial.add(new Atributo("nombre_paciente", "string", true,null));
        atributosHistorial.add(new Atributo("user_id", "int", false,"FK"));

        this.tablas.add(new Tabla("historials", atributosHistorial,1));

        //DatoMedico
        ArrayList<Atributo> atributosDatoMedico = new ArrayList<>();
        atributosDatoMedico.add(new Atributo("id", "int", true,"PK"));
        atributosDatoMedico.add(new Atributo("tipo", "string", false,null));
        atributosDatoMedico.add(new Atributo("titulo", "string", false,null));
        atributosDatoMedico.add(new Atributo("detalle", "string", false,null));
        atributosDatoMedico.add(new Atributo("historial_id", "int", false,"FK"));

        this.tablas.add(new Tabla("dato_medicos", atributosDatoMedico,4));

        //Consulta
        ArrayList<Atributo> atributosConsulta = new ArrayList<>();
        atributosConsulta.add(new Atributo("id", "int", true,"PK"));
        atributosConsulta.add(new Atributo("fecha", "date", false,null));
        atributosConsulta.add(new Atributo("motivo", "string", false,null));
        atributosConsulta.add(new Atributo("examen_fisico", "string", false,null));
        atributosConsulta.add(new Atributo("observaciones", "string", false,null));
        atributosConsulta.add(new Atributo("diagnostico", "string", false,null));
        atributosConsulta.add(new Atributo("historial_id", "int", false,"FK"));

        this.tablas.add(new Tabla("consultas", atributosConsulta,6));

        //ExamenFisico
        ArrayList<Atributo> atributosExamenFisico = new ArrayList<>();
        atributosExamenFisico.add(new Atributo("id", "int", true,"PK"));
        atributosExamenFisico.add(new Atributo("tipo", "string", false,null));
        atributosExamenFisico.add(new Atributo("valor", "string", false,null));
        atributosExamenFisico.add(new Atributo("consulta_id", "int", false,"FK"));

        this.tablas.add(new Tabla("examen_fisicos", atributosExamenFisico,3));
        
        //Tratamiento
        ArrayList<Atributo> atributosTratamiento = new ArrayList<>();
        atributosTratamiento.add(new Atributo("id", "int", true,"PK"));
        atributosTratamiento.add(new Atributo("detalle", "string", false,null));
        atributosTratamiento.add(new Atributo("consulta_id", "int", false,"FK"));
        
        this.tablas.add(new Tabla("examen_fisicos", atributosTratamiento,2));
    }

}
