import java.util.ArrayList;
import java.util.Scanner;

public class exp3_S8_Diego_Alvarez {

  // Configuración de categorías
  private static final String[] UBICACIONES = { "VIP", "Platea", "General" };
  private static final double[] PRECIOS = { 20000, 15000, 10000 };
  private static final int[][] RANGOS = { { 1, 20 }, { 21, 50 }, { 51, 100 } };

  // Configuración de descuentos
  private static final String[] TIPOS_DESCUENTO = { "Ninguno", "Estudiante", "Tercera Edad" };
  private static final double[] VALORES_DESCUENTO = { 0.0, 0.10, 0.15 };

  // Estructuras de datos
  private static boolean[] asientos = new boolean[100];
  private static ArrayList<Integer> ventasAsiento = new ArrayList<>();
  private static ArrayList<String> ventasCliente = new ArrayList<>();
  private static ArrayList<String> ventasDescuento = new ArrayList<>();
  private static ArrayList<Double> ventasTotal = new ArrayList<>();
  private static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    inicializarAsientos();
    mostrarMenu();
  }

  private static void inicializarAsientos() {
    for (int i = 0; i < asientos.length; i++) {
      asientos[i] = true;
    }
  }

  private static void mostrarMenu() {
    int opcion;
    do {
      System.out.println("\n=== Teatro Moro ===");
      System.out.println("1. Vender entrada");
      System.out.println("2. Ver ventas");
      System.out.println("3. Eliminar venta");
      System.out.println("4. Estadísticas");
      System.out.println("5. Salir");
      System.out.print("Seleccione: ");
      opcion = leerEnteroEnRango(1, 5);

      switch (opcion) {
        case 1:
          venderEntrada();
          break;
        case 2:
          mostrarVentas();
          break;
        case 3:
          eliminarVenta();
          break;
        case 4:
          mostrarEstadisticas();
          break;
        case 5:
          System.out.println("¡Hasta luego!");
          break;
        default:
          System.out.println("Opción inválida");
      }
    } while (opcion != 5);
  }

  private static void venderEntrada() {
    System.out.println("\n=== Vender Entrada ===");
    mostrarUbicaciones();

    System.out.print("Seleccione ubicación (1-3): ");
    int categoria = leerEnteroEnRango(1, 3) - 1;

    int minAsiento = RANGOS[categoria][0];
    int maxAsiento = RANGOS[categoria][1];

    if (!hayAsientosDisponibles(minAsiento, maxAsiento)) {
      System.out.println("No hay asientos disponibles en " + UBICACIONES[categoria]);
      return;
    }

    mostrarAsientosDisponibles(minAsiento, maxAsiento);
    System.out.print("Seleccione asiento: ");
    int numAsiento = leerEnteroEnRango(minAsiento, maxAsiento);

    if (!asientos[numAsiento - 1]) {
      System.out.println("Asiento ocupado. Intente otro.");
      return;
    }

    registrarVenta(categoria, numAsiento);
    asientos[numAsiento - 1] = false;
    imprimirBoleta(ventasAsiento.size() - 1);
  }

  private static void registrarVenta(int categoria, int numAsiento) {
    System.out.print("Nombre cliente: ");
    String cliente = scanner.next();

    System.out.print("Tipo descuento (1. Estudiante, 2. Tercera Edad, 3. Ninguno): ");
    int tipoDesc = leerEnteroEnRango(1, 3) - 1;

    double precio = PRECIOS[categoria] * (1 - VALORES_DESCUENTO[tipoDesc]);

    ventasAsiento.add(numAsiento);
    ventasCliente.add(cliente);
    ventasDescuento.add(TIPOS_DESCUENTO[tipoDesc]);
    ventasTotal.add(precio);
  }

  private static void imprimirBoleta(int id) {
    System.out.println("\n=== BOLETA ===");
    System.out.println("Asiento: " + ventasAsiento.get(id));
    System.out.println("Cliente: " + ventasCliente.get(id));
    System.out.println("Descuento: " + ventasDescuento.get(id));
    System.out.println("Total: $" + ventasTotal.get(id));
    System.out.println("Gracias por su compra!");
  }

  private static int leerEnteroEnRango(int min, int max) {
    while (true) {
      try {
        int input = Integer.parseInt(scanner.next());
        if (input >= min && input <= max)
          return input;
        System.out.print("Error: Ingrese entre " + min + " y " + max + ": ");
      } catch (NumberFormatException e) {
        System.out.print("Entrada inválida. Ingrese número: ");
      }
    }
  }

  private static boolean hayAsientosDisponibles(int minAsiento, int maxAsiento) {
    long inicio = System.nanoTime();
    boolean encontrado = false;
    for (int i = minAsiento - 1; i < maxAsiento; i++) {
      if (asientos[i]) {
        encontrado = true;
        break;
      }
    }
    long fin = System.nanoTime();
    System.out.println("[TEST] hayAsientosDisponibles: " + (fin - inicio) / 1_000_000.0);
    return encontrado;
  }

  private static void mostrarAsientosDisponibles(int minAsiento, int maxAsiento) {
    System.out.print("Asientos disponibles: ");
    long inicio = System.nanoTime();
    for (int i = minAsiento - 1; i < maxAsiento; i++) {
      if (asientos[i]) {
        System.out.print((i + 1) + " ,");
      }
    }
    long fin = System.nanoTime();
    System.out.println();
    System.out.println("[TEST] mostrarAsientosDisponibles: " + (fin - inicio) / 1_000_000.0);
  }

  private static void mostrarUbicaciones() {
    System.out.println("Ubicaciones disponibles:");
    for (int i = 0; i < UBICACIONES.length; i++) {
      System.out.println((i + 1) + ". " + UBICACIONES[i] +
          " (Asientos " + RANGOS[i][0] + "-" + RANGOS[i][1] +
          ") - Precio: $" + PRECIOS[i]);
    }
  }

  private static void mostrarVentas() {
    if (ventasAsiento.isEmpty()) {
      System.out.println("\nNo hay ventas registradas");
      return;
    }

    System.out.println("\n=== Ventas ===");
    System.out.println("ID | Asiento | Cliente | Descuento | Total");

    long inicioBucle = System.nanoTime();
    for (int i = 0; i < ventasAsiento.size(); i++) {
      System.out.println(
          (i + 1) + " | " +
              ventasAsiento.get(i) + " | " +
              ventasCliente.get(i) + " | " +
              ventasDescuento.get(i) + " | $" +
              ventasTotal.get(i));
    }
    long finBucle = System.nanoTime();
    System.out.println("[TEST] Bucle mostrarVentas: " + (finBucle - inicioBucle) / 1_000_000.0);
  }

  private static void eliminarVenta() {
    if (ventasAsiento.isEmpty()) {
      System.out.println("\nNo hay ventas para eliminar");
      return;
    }
    mostrarVentas();
    System.out.print("\nIngrese ID de la venta a eliminar: ");
    int id = leerEnteroEnRango(1, ventasAsiento.size()) - 1;

    long inicioEliminacion = System.nanoTime();
    int asientoALiberar = ventasAsiento.get(id);
    ventasAsiento.remove(id);
    ventasCliente.remove(id);
    ventasDescuento.remove(id);
    ventasTotal.remove(id);
    liberarAsiento(asientoALiberar);
    long finEliminacion = System.nanoTime();

    System.out.println("Venta eliminada exitosamente!");
    System.out.println("[TEST] Operaciones eliminarVenta: " + (finEliminacion - inicioEliminacion) / 1_000_000.0);
  }

  private static void liberarAsiento(int numAsiento) {
    if (numAsiento >= 1 && numAsiento <= 100) {
      asientos[numAsiento - 1] = true;
    }
  }

  private static void mostrarEstadisticas() {
    System.out.println("\n=== Estadísticas ===");
    System.out.println("Total de ventas: " + ventasAsiento.size());
    System.out.println("Ingresos totales:" + calcularIngresos());
    System.out.println("\nAsientos disponibles:");
    System.out.println("VIP: " + contarDisponibles(0));
    System.out.println("Platea: " + contarDisponibles(1));
    System.out.println("General: " + contarDisponibles(2));
  }

  private static double calcularIngresos() {
    long inicio = System.nanoTime();
    double total = 0;
    for (double t : ventasTotal) {
      total += t;
    }
    long fin = System.nanoTime();
    System.out.println("[TEST] calcularIngresos: " + (fin - inicio) / 1_000_000.0);
    return total;
  }

  private static int contarDisponibles(int categoria) {
    long inicio = System.nanoTime();
    int count = 0;
    int inicioRango = RANGOS[categoria][0] - 1;
    int finRango = RANGOS[categoria][1];
    for (int i = inicioRango; i < finRango; i++) {
      if (asientos[i]) {
        count++;
      }
    }
    long fin = System.nanoTime();
    System.out.println("[TEST] contarDisponibles: " + (fin - inicio) / 1_000_000.0);
    return count;
  }
}
