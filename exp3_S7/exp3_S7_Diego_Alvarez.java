import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class exp3_S7_Diego_Alvarez {

  // Ubicaciones
  private static final String[] UBICACIONES = { "VIP", "Platea", "General" };
  private static final double[] PRECIOS_BASE = { 20000, 15000, 10000 };
  private static final int[] INICIALES = { 20, 30, 50 };

  // Descuentos
  private static final String[] TIPOS_DESCUENTO = { "ninguno", "estudiante", "Tercera Edad" };
  private static final double[] VALORES_DESCUENTO = { 0.0, 0.10, 0.15 };

  // control y estadisticas
  private int[] vendidosPorUbicacion = { 0, 0, 0 };
  private static double ingresosTotales = 0;
  private static int entradasVendidas = 0;

  // almacenar ventas
  private ArrayList<String> ventasUbicacion = new ArrayList<>();
  private ArrayList<Double> ventasCostoFinal = new ArrayList<>();
  private ArrayList<String> ventasDescuento = new ArrayList<>();

  private Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    exp3_S7_Diego_Alvarez teatro = new exp3_S7_Diego_Alvarez();
    teatro.mostrarMenu();
  }

  private void mostrarMenu() {
    int opcion;
    do {
      System.out.println("\n=== Teatro Moro ===");
      System.out.println("1. Vender entrada");
      System.out.println("2. Ver resumen de ventas");
      System.out.println("3. Generar boleta");
      System.out.println("4. Mostrar estadísticas");
      System.out.println("5. Salir");
      System.out.print("Seleccione una opción: ");
      opcion = leerOpcion();

      switch (opcion) {
        case 1:
          venderEntrada();
          break;
        case 2:
          mostrarResumen();
          break;
        case 3:
          generarBoleta();
          break;
        case 4:
          mostrarEstadisticas();
          break;
        case 5:
          System.out.println("Hasta luego!");
          break;
        default:
          System.out.println("Opcion no válida");
      }
    } while (opcion != 5);
  }

  private void venderEntrada() {
    System.out.println("\n=== Vender Entrada ===");

    // ubicaciones
    for (int i = 0; i < UBICACIONES.length; i++) {
      int disponibles = INICIALES[i] - vendidosPorUbicacion[i];
      System.out.println((i + 1) + ". " + UBICACIONES[i] + " - Precio: $" + PRECIOS_BASE[i] +
          " - Disponibles: " + disponibles);
    }
    System.out.print("Seleccione ubicacion (1-3): ");
    int opcion = leerOpcion() - 1;

    // validar selección
    if (opcion < 0 || opcion >= UBICACIONES.length) {
      System.out.println("ubicacion no válida");
      return;
    }

    // Verificar disponibilidad
    if (vendidosPorUbicacion[opcion] >= INICIALES[opcion]) {
      System.out.println("No hay entradas disponibles");
      return;
    }

    // sleccionar descuento
    System.out.println("\nDescuentos disponibles:");
    for (int i = 0; i < TIPOS_DESCUENTO.length; i++) {
      System.out.println((i + 1) + ". " + TIPOS_DESCUENTO[i] + " (" + (int) (VALORES_DESCUENTO[i] * 100) + "%)");
    }
    System.out.print("Seleccione descuento (1-3): ");
    int descuentoId = leerOpcion() - 1;

    // validar descuento
    if (descuentoId < 0 || descuentoId >= TIPOS_DESCUENTO.length) {
      descuentoId = 0;
    }

    double precioBase = PRECIOS_BASE[opcion];
    double descuento = VALORES_DESCUENTO[descuentoId];
    double total = precioBase * (1 - descuento);

    // rgistrar venta e imprimir boleta
    ventasUbicacion.add(UBICACIONES[opcion]);
    ventasCostoFinal.add(total);
    ventasDescuento.add(TIPOS_DESCUENTO[descuentoId]);
    vendidosPorUbicacion[opcion]++;
    ingresosTotales += total;
    entradasVendidas++;

    System.out.println("\nEntrada vendida! Total: $" + total);
    int ultimoId = ventasUbicacion.size() - 1;
    imprimirBoleta(ultimoId);
  }

  private void mostrarResumen() {
    if (ventasUbicacion.isEmpty()) {
      System.out.println("\nNo hay ventas registradas");
      return;
    }

    System.out.println("\n=== Resumen de Ventas ===");
    System.out.println("ID   Ubicación    Descuento           Total");
    for (int i = 0; i < ventasUbicacion.size(); i++) {
      String id = String.valueOf(i + 1);
      String ubicacion = ventasUbicacion.get(i);
      String descuento = ventasDescuento.get(i);
      String total = String.valueOf(ventasCostoFinal.get(i));
      System.out.println(id + " " + ubicacion + " " + descuento + " $" + total);
    }
  }

  private void generarBoleta() {
    if (ventasUbicacion.isEmpty()) {
      System.out.println("\nNo hay boletas");
      return;
    }

    System.out.print("\nIngrese el ID de la venta (1-" + ventasUbicacion.size() + "): ");
    int idInput = leerOpcion();
    int id = idInput - 1;

    if (id < 0 || id >= ventasUbicacion.size()) {
      System.out.println("ID no valido. Debe ser entre 1 y " + ventasUbicacion.size());
      return;
    }

    imprimirBoleta(id);
  }

  private void mostrarEstadisticas() {
    System.out.println("\n=== Estadisticas ===");
    System.out.println("Total entradas vendidas: " + entradasVendidas);
    System.out.println("Ingresos totales: $" + ingresosTotales);
    System.out.println("\nEntradas disponibles:");
    for (int i = 0; i < UBICACIONES.length; i++) {
      int disponibles = INICIALES[i] - vendidosPorUbicacion[i];
      System.out.println(UBICACIONES[i] + ": " + disponibles);
    }
  }

  // leer enteros de forma segura
  private int leerOpcion() {
    while (true) {
      try {
        return scanner.nextInt();
      } catch (InputMismatchException e) {
        scanner.nextLine();
        System.out.print("Entrada invalida. Ingrese un numero: ");
      }
    }
  }

  private void imprimirBoleta(int id) {
    System.out.println("\n=== BOLETA ===");
    System.out.println("Ubicacion: " + ventasUbicacion.get(id));
    System.out.println("Descuento: " + ventasDescuento.get(id));
    System.out.println("Total: $" + ventasCostoFinal.get(id));
    System.out.println("Gracias por su compra!");
  }

}
