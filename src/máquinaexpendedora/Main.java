/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package máquinaexpendedora;

/**
 *
 * @author efech
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
              MaquinaExpendedoraMunozBarriosMarta2223 snack;
       
        
        snack=new MaquinaExpendedoraMunozBarriosMarta2223(13,2,5);
        sube1_PrecioMunozBarriosMarta2223(snack);
        
        sube2_PrecioMunozBarriosMarta2223(snack);
        
        obtener1_UnidadesMunozBarriosMarta2223(snack);
        
        obtener2_UnidadesMunozBarriosMarta2223(snack);
        
        obtener3_UnidadesMunozBarriosMarta2223(snack);
        
    }

    public static void obtener3_UnidadesMunozBarriosMarta2223(MaquinaExpendedoraMunozBarriosMarta2223 snack) {
        /*Vamos a intentar sacar 12 snack, y tengo 50 euros.
        Como no hay unidades suficientes en la máquina saltará
        una excepción y no se modificará el nº de unidades de la tienda*/
        try {
            System.out.println("3º.- Vamos a sacar snacks (queremos 12 y tenemos 50 €)");
            snack.sacarsnack(12, 50, "pegatina");
            System.out.println("Operación realizada, ahora tiene "+snack.obtenerUnidades()+" unidades en la máquina");
        } catch (Exception e) {
            System.out.println("Error al sacar snack,porque no hay suficientes unidades");
            System.out.println("El nº de unidades sigue siendo: "+snack.obtenerUnidades());
            
        }
    }

    public static void obtener2_UnidadesMunozBarriosMarta2223(MaquinaExpendedoraMunozBarriosMarta2223 snack) {
        /*Vamos a intentar sacar 2 snack, y tengo 9 euros.
        Ahora se actualizará el número de snack que tenemos en
        la máquina*/
        try {
            System.out.println("2º.- Vamos a sacar snacks (queremos 2 y tenemos 9 €)");
            snack.sacarsnack(2, 9, "pegatina");
            System.out.println("Operación realizada, ahora tiene "+snack.obtenerUnidades()+" unidades en la máquina");
        } catch (Exception e) {
            System.out.println("Error al sacar snack,porque no hay suficientes dinero");
            System.out.println("El nº de unidades sigue siendo: "+snack.obtenerUnidades());
            
        }
    }

    public static void obtener1_UnidadesMunozBarriosMarta2223(MaquinaExpendedoraMunozBarriosMarta2223 snack) {
        /*Vamos a intentar sacar 2 snack, y tengo 5 euros.
        Como no hay dinero suficiente saltará
        una excepción y no se modificará el nº de unidades de la tienda*/
        try {
            System.out.println("1º.- Vamos a sacar snacks (queremos 2 y tenemos 5 €)");
            snack.sacarsnack(2, 5, "pegatina");
            System.out.println("Operación realizada, ahora tiene "+snack.obtenerUnidades()+" unidades en la máquina");
        } catch (Exception e) {
            System.out.println("Error al sacar snack,porque no hay suficientes dinero");
            System.out.println("El nº de unidades sigue siendo: "+snack.obtenerUnidades());
            
        }
    }

    public static void sube2_PrecioMunozBarriosMarta2223(MaquinaExpendedoraMunozBarriosMarta2223 snack) {
        /*Vamos a aumentar el precio 3€, como supera el precio máximo establecido la operación
        no tendrá éxito y se mantendrá el precio anterior*/
        try {
            System.out.println("Vamos a aumentar el precio de venta de un snack por encima del precio máximo");
            snack.aumentarPrecio(3);
            System.out.println("El precio actual es "+snack.obtenerPrecio()+" €");
        } catch (Exception e) {
            System.out.println("Error al aumentar el precio porque se supera el precio máximo de 5 €");
        }
    }

    public static void sube1_PrecioMunozBarriosMarta2223(MaquinaExpendedoraMunozBarriosMarta2223 snack) {
        /*Vamos a aumentar el precio 1€, como no supera el precio máximo establecido la operación
        tendrá éxito y el nuevo precio será de 3€*/
        try {
            System.out.println("Vamos a aumentar el precio de venta de un snack por debajos del precio máximo");
            snack.aumentarPrecio(1);
            System.out.println("El precio actual es "+snack.obtenerPrecio()+" €");
        } catch (Exception e) {
            System.out.println("Error al aumentar el precio");
        }
    }
    
}
