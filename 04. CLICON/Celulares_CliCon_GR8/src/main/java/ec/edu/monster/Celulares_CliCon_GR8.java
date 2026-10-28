package ec.edu.monster;

import ec.edu.monster.proxy.Celular;
import ec.edu.monster.proxy.CuotaAmortizacion;
import ec.edu.monster.proxy.Factura;
import ec.edu.monster.proxy.WSComercializadora;
import ec.edu.monster.proxy.WSComercializadora_Service;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class Celulares_CliCon_GR8 {

    private static WSComercializadora servicio;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("  UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE - NRC 36883 - GRUPO 08");
        System.out.println("  COMERCIALIZADORA DE CELULARES - CLIENTE CONSOLA (SOAP JAX-WS)");
        System.out.println("========================================================================");
        System.out.println("Conectando con el servicio web de la Comercializadora...");

        try {
            URL wsdlUrl = new URL("http://localhost:8080/WS_Comercializadora_SOAP/WSComercializadora?wsdl");
            WSComercializadora_Service factory = new WSComercializadora_Service(wsdlUrl);
            servicio = factory.getWSComercializadoraPort();
            System.out.println("¡Conexión SOAP establecida exitosamente con el Servidor!");
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo conectar al servidor SOAP: " + e.getMessage());
            System.err.println("Verifique que Payara Server esté activo en el puerto 8080.");
            return;
        }

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            System.out.print("\nSeleccione una opción: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    listarCatalogo(true);
                    break;
                case "2":
                    listarCatalogo(false);
                    break;
                case "3":
                    registrarCelular();
                    break;
                case "4":
                    modificarCelular();
                    break;
                case "5":
                    eliminarCelular();
                    break;
                case "6":
                    facturarEfectivo();
                    break;
                case "7":
                    facturarCredito();
                    break;
                case "8":
                    consultarAmortizacion();
                    break;
                case "9":
                    salir = true;
                    System.out.println("\n¡Gracias por utilizar el sistema de la Comercializadora! Hasta pronto.");
                    break;
                default:
                    System.out.println("Opción inválida. Ingrese un número entre 1 y 9.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n------------------------------------------------------------------------");
        System.out.println("                     MENÚ PRINCIPAL DE OPERACIONES");
        System.out.println("------------------------------------------------------------------------");
        System.out.println(" [1] Ver catálogo disponible para la venta (Solo Activos)");
        System.out.println(" [2] Ver catálogo general completo (Activos e Inactivos)");
        System.out.println(" [3] Registrar nuevo celular en el catálogo (Create)");
        System.out.println(" [4] Modificar datos de un celular (Update)");
        System.out.println(" [5] Eliminar celular (Delete - Borrado Lógico Referencial)");
        System.out.println(" [6] Facturar venta en EFECTIVO (42% de descuento automático)");
        System.out.println(" [7] Facturar venta a CRÉDITO (Evaluación BanQuito y cuotas)");
        System.out.println(" [8] Consultar tabla de amortización de un crédito");
        System.out.println(" [9] Salir");
        System.out.println("------------------------------------------------------------------------");
    }

    private static void listarCatalogo(boolean soloActivos) {
        System.out.println("\n=== CATÁLOGO DE TELÉFONOS CELULARES " + (soloActivos ? "(ACTIVOS PARA VENTA)" : "(COMPLETO)") + " ===");
        try {
            List<Celular> lista = servicio.listarCelulares(soloActivos);
            if (lista == null || lista.isEmpty()) {
                System.out.println("No hay celulares registrados.");
                return;
            }

            imprimirTablaCelulares(lista);
        } catch (Exception e) {
            System.err.println("Error al listar catálogo: " + e.getMessage());
        }
    }

    private static void imprimirTablaCelulares(List<Celular> lista) {
        System.out.println("+----+------------+--------------------------------+------------+------------+");
        System.out.println("| ID | MARCA      | MODELO                         | PRECIO ($) | ESTADO     |");
        System.out.println("+----+------------+--------------------------------+------------+------------+");
        for (Celular c : lista) {
            String estado = c.isActivo() ? "ACTIVO" : "INACTIVO";
            System.out.printf("| %-2d | %-10s | %-30s | %10.2f | %-10s |\n",
                    c.getCodCelular(),
                    truncar(c.getMarca(), 10),
                    truncar(c.getModelo(), 30),
                    c.getPrecio(),
                    estado);
        }
        System.out.println("+----+------------+--------------------------------+------------+------------+");
    }

    private static void registrarCelular() {
        System.out.println("\n=== REGISTRAR NUEVO CELULAR ===");
        try {
            System.out.print("Marca: ");
            String marca = scanner.nextLine().trim();
            System.out.print("Modelo: ");
            String modelo = scanner.nextLine().trim();
            System.out.print("Precio de venta ($): ");
            BigDecimal precio = new BigDecimal(scanner.nextLine().trim());

            Celular nuevo = servicio.crearCelular(marca, modelo, precio);
            if (nuevo != null) {
                System.out.println(">>> ¡Celular registrado exitosamente con ID " + nuevo.getCodCelular() + "!");
            } else {
                System.out.println("No se pudo registrar el celular.");
            }
        } catch (Exception e) {
            System.err.println("Error al registrar celular: " + e.getMessage());
        }
    }

    private static void modificarCelular() {
        System.out.println("\n=== MODIFICAR CELULAR EXISTENTE ===");
        try {
            System.out.print("Ingrese ID del celular a modificar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Celular cel = servicio.obtenerCelular(id);
            if (cel == null) {
                System.out.println("El celular con ID " + id + " no existe.");
                return;
            }

            System.out.println("Datos actuales -> Marca: " + cel.getMarca() + ", Modelo: " + cel.getModelo() +
                    ", Precio: $" + cel.getPrecio() + ", Activo: " + (cel.isActivo() ? "1" : "0"));

            System.out.print("Nueva Marca (dejar vacío para mantener): ");
            String marca = scanner.nextLine().trim();
            if (marca.isEmpty()) marca = cel.getMarca();

            System.out.print("Nuevo Modelo (dejar vacío para mantener): ");
            String modelo = scanner.nextLine().trim();
            if (modelo.isEmpty()) modelo = cel.getModelo();

            System.out.print("Nuevo Precio (dejar vacío para mantener): ");
            String precioStr = scanner.nextLine().trim();
            BigDecimal precio = precioStr.isEmpty() ? cel.getPrecio() : new BigDecimal(precioStr);

            System.out.print("Estado (1 = Activo, 0 = Inactivo, vacío para mantener): ");
            String activoStr = scanner.nextLine().trim();
            boolean activo = activoStr.isEmpty() ? cel.isActivo() : "1".equals(activoStr);

            Celular actualizado = servicio.actualizarCelular(id, marca, modelo, precio, activo);
            if (actualizado != null) {
                System.out.println(">>> ¡Celular modificado correctamente!");
            } else {
                System.out.println("No se pudo modificar el celular.");
            }
        } catch (Exception e) {
            System.err.println("Error al modificar celular: " + e.getMessage());
        }
    }

    private static void eliminarCelular() {
        System.out.println("\n=== ELIMINAR CELULAR (BORRADO LÓGICO REFERENCIAL) ===");
        try {
            System.out.print("Ingrese ID del celular a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Celular cel = servicio.obtenerCelular(id);
            if (cel == null) {
                System.out.println("El celular con ID " + id + " no existe.");
                return;
            }

            System.out.println("Equipo a dar de baja: " + cel.getMarca() + " " + cel.getModelo() + " ($" + cel.getPrecio() + ")");
            System.out.print("¿Está seguro de continuar? (S/N): ");
            String conf = scanner.nextLine().trim();
            if (!conf.equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada por el usuario.");
                return;
            }

            boolean ok = servicio.eliminarCelular(id);
            if (ok) {
                System.out.println(">>> ¡Operación completada con éxito!");
                System.out.println("    (Nota: Si tenía facturas históricas se aplicó borrado lógico [activo=0]; de lo contrario se eliminó físicamente).");
            } else {
                System.out.println("No se pudo eliminar el celular.");
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar celular: " + e.getMessage());
        }
    }

    private static void facturarEfectivo() {
        System.out.println("\n=== FACTURACIÓN EN EFECTIVO (DESCUENTO DEL 42%) ===");
        try {
            System.out.print("Cédula del cliente: ");
            String cedula = scanner.nextLine().trim();
            System.out.print("Nombre completo: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("ID del celular a comprar: ");
            int idCelular = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Cantidad: ");
            int cantidad = Integer.parseInt(scanner.nextLine().trim());

            Factura f = servicio.facturarEfectivo(cedula, nombre, idCelular, cantidad);
            if (f != null) {
                imprimirFactura(f);
            }
        } catch (Exception e) {
            System.err.println("\nERROR EN FACTURACIÓN: " + e.getMessage());
        }
    }

    private static void facturarCredito() {
        System.out.println("\n=== FACTURACIÓN A CRÉDITO DIRECTO (BANQUITO) ===");
        try {
            System.out.print("Cédula del cliente: ");
            String cedula = scanner.nextLine().trim();
            System.out.print("Nombre completo: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("ID del celular a financiar: ");
            int idCelular = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Plazo de financiamiento en meses (3 a 18): ");
            int cuotas = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Consultando evaluación crediticia en Core BanQuito...");
            Factura f = servicio.facturarCredito(cedula, nombre, idCelular, cuotas);
            if (f != null) {
                imprimirFactura(f);
                if (f.getCodCredito() != null) {
                    System.out.println("\n>>> TABLA DE AMORTIZACIÓN GENERADA POR BANQUITO (Crédito #" + f.getCodCredito() + "):");
                    imprimirTablaAmortizacion(f.getCodCredito());
                }
            }
        } catch (Exception e) {
            System.err.println("\n>>> CRÉDITO NO APROBADO: " + e.getMessage());
        }
    }

    private static void consultarAmortizacion() {
        System.out.println("\n=== CONSULTA DE TABLA DE AMORTIZACIÓN DE CRÉDITO ===");
        try {
            System.out.print("Ingrese el código de crédito BanQuito: ");
            int codCredito = Integer.parseInt(scanner.nextLine().trim());
            imprimirTablaAmortizacion(codCredito);
        } catch (Exception e) {
            System.err.println("Error al consultar amortización: " + e.getMessage());
        }
    }

    private static void imprimirTablaAmortizacion(int codCredito) {
        try {
            List<CuotaAmortizacion> tabla = servicio.consultarAmortizacion(codCredito);
            if (tabla == null || tabla.isEmpty()) {
                System.out.println("No se encontraron cuotas para el crédito #" + codCredito);
                return;
            }

            System.out.println("+-------+--------------+--------------+--------------+--------------+");
            System.out.println("| CUOTA |  VALOR CUOTA |      INTERÉS |      CAPITAL |        SALDO |");
            System.out.println("+-------+--------------+--------------+--------------+--------------+");
            for (CuotaAmortizacion c : tabla) {
                System.out.printf("|  %3d  | %12.2f | %12.2f | %12.2f | %12.2f |\n",
                        c.getNumCuota(),
                        c.getValorCuota(),
                        c.getInteres(),
                        c.getCapital(),
                        c.getSaldo());
            }
            System.out.println("+-------+--------------+--------------+--------------+--------------+");
        } catch (Exception e) {
            System.err.println("Error al desplegar amortización: " + e.getMessage());
        }
    }

    private static void imprimirFactura(Factura f) {
        System.out.println("\n========================================================");
        System.out.println("                   COMPROBANTE DE FACTURA               ");
        System.out.println("========================================================");
        System.out.println(" Factura N°     : " + f.getNumFactura());
        System.out.println(" Cliente        : " + f.getNombre());
        System.out.println(" Cédula         : " + f.getCedula());
        System.out.println(" Forma de Pago  : " + f.getFormaPago());
        if (f.getCodCredito() != null) {
            System.out.println(" Código Crédito : #" + f.getCodCredito() + " (Aprobado BanQuito)");
        }
        System.out.println("--------------------------------------------------------");
        System.out.printf(" Subtotal       : $%10.2f\n", f.getSubtotal());
        if ("EFECTIVO".equalsIgnoreCase(f.getFormaPago())) {
            System.out.printf(" Descuento (42%%): -$%9.2f\n", f.getDescuento());
        }
        System.out.printf(" TOTAL A PAGAR  : $%10.2f\n", f.getTotal());
        System.out.println("========================================================");
    }

    private static String truncar(String texto, int longitud) {
        if (texto == null) return "";
        return (texto.length() > longitud) ? texto.substring(0, longitud - 2) + ".." : texto;
    }
}
