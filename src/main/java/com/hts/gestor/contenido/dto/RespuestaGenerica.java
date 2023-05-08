/**
 * Response.java
 * Fecha de creación: 23/05/2016, 17:54:41
 *
 * Copyright (c) 2016 Instituto Federal Electoral. Dirección
 * Ejecutiva del Registro Federal de Electores.
 * Periférico Sur 239, México, D.F., C.P. 01010.
 * Todos los derechos reservados.
 *
 * Este software es información confidencial, propiedad del
 * Instituto Federal Electoral. Esta información confidencial
 * no deberá ser divulgada y solo se podrá utilizar de acuerdo
 * a los términos que determine el propio Instituto.
 */

package com.hts.gestor.contenido.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Objeto de respuesta para los servicios web. <br>
 * <p>
 * La infomación es almacenada en una estructura de datos tipo Mapa<String,Object> [{@link Map}], donde
 * la llave del mapa es el nombre de la variable y el objeto es el valor de la variable.
 * </p>
 * <p>
 * El valor de la variable puede ser de cualquier tipo, desde un
 * <code>String,Integer,Boolean,long </code> hasta una estructura compuesta como
 * <code>List,Set incluso otros Mapas</code>
 * </p>
 * <strong>ejemplo:</strong> Suponga que un servicio retorna tramiteId,folio y idTarea.<br>
 * La representación de la informacion en notación JSON : <br>
 * <code>{<br>
 * "estatus": 1
    "mensaje": "tarea liberada "
     "informacion":{<br>
           "tramiteId":4,<br>
           "folio":"555555",<br>
           "idTarea":2<br>
     }<br>
   }<br></code> <br>
 * La forma para obtener esos valores por medio del objeto Response es la siguiente:<br>
 * <strong> Long tramiteId = null; <br>
 * tramiteId = response.getInformacion.get("tramiteId");<br>
 * ... </strong><br>
 */
public class RespuestaGenerica implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 16767768878898987L;
    private int estatus;
    private String mensaje;
    private Map<String, Object> informacion;

    /**
     * Constructor
     * @author
     */
    public RespuestaGenerica() {
        // TODO [codificar el cuerpo del método]
        setInformacion(new HashMap<String, Object>());
    }

    /**
     * Constructor
     * @author
     * @param estatus
     * @param mensaje
     * @param informacion
     */
    public RespuestaGenerica(int estatus, String mensaje, Map<String, Object> informacion) {
        this.estatus = estatus;
        this.mensaje = mensaje;
        this.setInformacion(informacion);
    }

    /**
     * Constructor
     * @author
     * @param estatus
     * @param mensaje
     */
    public RespuestaGenerica(int estatus, String mensaje) {
        this.estatus = estatus;
        this.mensaje = mensaje;
    }

    /**
     * Constructor
     * @author
     * @param estatus
     * @param mensaje
     * @param llave
     * @param valor
     */
    public RespuestaGenerica(int estatus, String mensaje, String llave, Object valor) {
        this.estatus = estatus;
        this.mensaje = mensaje;
        this.getInformacion().put(llave, valor);
    }



    /**
     * @return el atributo estatus
     */
    public int getEstatus() {
        return estatus;
    }

    /**
     * @param estatus parametro estatus a actualizar
     */
    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    /**
     * @return el atributo mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @param mensaje parametro mensaje a actualizar
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * @return el atributo informacion
     */
    public Map<String, Object> getInformacion() {
    	if(null == informacion){
    		informacion = new HashMap<String,Object>();
    	}
        return informacion;
    }

    /**
     * @param informacion parametro informacion a actualizar
     */
    public void setInformacion(Map<String, Object> informacion) {
        this.informacion = informacion;
    }

}
