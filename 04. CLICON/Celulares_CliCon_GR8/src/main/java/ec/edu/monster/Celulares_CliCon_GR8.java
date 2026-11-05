package ec.edu.monster;

import ec.edu.monster.proxy.Celular;
import ec.edu.monster.proxy.CuotaAmortizacion;
import ec.edu.monster.proxy.DetalleFactura;
import ec.edu.monster.proxy.Factura;
import ec.edu.monster.proxy.ItemVenta;
import ec.edu.monster.proxy.WSComercializadora;
import ec.edu.monster.proxy.WSComercializadora_Service;

import java.io.FileOutputStream;
import java.io.FileDescriptor;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Celulares_CliCon_GR8 {

    // Tildes y ñ: la ventana Output de NetBeans lee en UTF-8, pero sin una consola real Java escribe en la
    // codificación de Windows (Cp1252) y aparecían "◆?". En una consola real (cmd) se respeta su codificación.
    private static final Charset CODIFICACION = System.console() == null
            ? StandardCharsets.UTF_8 : System.console().charset();

    static {
        if (System.console() == null) {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
            System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err), true, StandardCharsets.UTF_8));
        }
    }

    private static final int MAX_CELULARES = 10;
    private static final int MAX_CANTIDAD = 99;

    private static WSComercializadora servicio;
    private static final Scanner scanner = new Scanner(System.in, CODIFICACION);

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
            List<ItemVenta> carrito = leerCarrito();
            if (carrito.isEmpty()) {
                System.out.println("Venta cancelada: no se agregó ningún celular.");
                return;
            }

            Factura f = servicio.facturarVentaEfectivo(cedula, nombre, carrito);
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
            List<ItemVenta> carrito = leerCarrito();
            if (carrito.isEmpty()) {
                System.out.println("Venta cancelada: no se agregó ningún celular.");
                return;
            }
            System.out.print("Plazo de financiamiento en meses (3 a 18): ");
            int cuotas = Integer.parseInt(scanner.nextLine().trim());

            System.out.println("Consultando evaluación crediticia en Core BanQuito (el crédito cubre el total de la venta)...");
            Factura f = servicio.facturarVentaCredito(cedula, nombre, carrito, cuotas);
            if (f != null) {
                imprimirFactura(f);
                if (f.getCodCredito() != null) {
                    System.out.println("\n>>> TABLA DE AMORTIZACIÓN GENERADA POR BANQUITO (Crédito #" + f.getCodCredito() + " - Factura #" + f.getNumFactura() + "):");
                    imprimirTablaAmortizacion(f.getNumFactura());
                }
            }
        } catch (Exception e) {
            System.err.println("\n>>> CRÉDITO NO APROBADO: " + e.getMessage());
        }
    }

    // Carrito de la venta: se agregan celulares hasta responder N; el mismo celular repetido suma su cantidad
    private static List<ItemVenta> leerCarrito() {
        Map<Integer, Integer> cantidades = new LinkedHashMap<>();
        Map<Integer, Celular> catalogo = new LinkedHashMap<>();
        try {
            for (Celular c : servicio.listarCelulares(true)) {
                catalogo.put(c.getCodCelular(), c);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el catálogo: " + e.getMessage());
        }

        while (true) {
            System.out.print("\nID del celular a agregar (Enter para terminar): ");
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                break;
            }
            int id;
            int cantidad;
            try {
                id = Integer.parseInt(linea);
                System.out.print("Cantidad (1 a " + MAX_CANTIDAD + "): ");
                cantidad = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese solo números enteros.");
                continue;
            }
            if (!catalogo.containsKey(id)) {
                System.out.println("El celular " + id + " no existe o no está disponible. Revise el catálogo (opción 1).");
                continue;
            }
            int nueva = cantidades.getOrDefault(id, 0) + cantidad;
            if (cantidad < 1 || nueva > MAX_CANTIDAD) {
                System.out.println("La cantidad de cada celular debe estar entre 1 y " + MAX_CANTIDAD + ".");
                continue;
            }
            if (!cantidades.containsKey(id) && cantidades.size() >= MAX_CELULARES) {
                System.out.println("Una factura admite máximo " + MAX_CELULARES + " celulares distintos.");
                continue;
            }
            cantidades.put(id, nueva);
            imprimirCarrito(cantidades, catalogo);

            System.out.print("¿Agregar otro celular? (S/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("S")) {
                break;
            }
        }

        List<ItemVenta> items = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : cantidades.entrySet()) {
            ItemVenta item = new ItemVenta();
            item.setCodCelular(e.getKey());
            item.setCantidad(e.getValue());
            items.add(item);
        }
        return items;
    }

    private static void imprimirCarrito(Map<Integer, Integer> cantidades, Map<Integer, Celular> catalogo) {
        BigDecimal total = BigDecimal.ZERO;
        System.out.println("+----+--------------------------------------+-------+--------------+");
        System.out.println("| ID | CELULAR                              | CANT. |     SUBTOTAL |");
        System.out.println("+----+--------------------------------------+-------+--------------+");
        for (Map.Entry<Integer, Integer> e : cantidades.entrySet()) {
            Celular c = catalogo.get(e.getKey());
            BigDecimal subtotal = c.getPrecio().multiply(BigDecimal.valueOf(e.getValue()));
            total = total.add(subtotal);
            System.out.printf("| %2d | %-36s | %5d | %12.2f |%n", e.getKey(),
                    truncar(c.getMarca() + " " + c.getModelo(), 36), e.getValue(), subtotal);
        }
        System.out.println("+----+--------------------------------------+-------+--------------+");
        System.out.printf("  Subtotal de la venta: $%.2f%n", total);
    }

    private static void consultarAmortizacion() {
        System.out.println("\n=== CONSULTA DE TABLA DE AMORTIZACIÓN DE CRÉDITO ===");
        try {
            System.out.print("Ingrese el número de factura (CU-08): ");
            int numFactura = Integer.parseInt(scanner.nextLine().trim());
            imprimirTablaAmortizacion(numFactura);
        } catch (Exception e) {
            System.err.println("Error al consultar amortización: " + e.getMessage());
        }
    }

    private static void imprimirTablaAmortizacion(int numFactura) {
        try {
            List<CuotaAmortizacion> tabla = servicio.consultarAmortizacion(numFactura);
            if (tabla == null || tabla.isEmpty()) {
                System.out.println("No se encontraron cuotas para la factura #" + numFactura);
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
        for (DetalleFactura d : f.getDetalles()) {
            System.out.printf(" %-30s %3d x %9.2f = %10.2f%n",
                    truncar(d.getMarcaCelular() + " " + d.getModeloCelular(), 30),
                    d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal());
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
