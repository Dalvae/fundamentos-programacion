import java.util.ArrayList;
import java.util.Scanner;
// import java.util.Collections; 

public class Exp2_S5_Diego_Alvarez {
  // Precios y ubicaciones
  private static final double[] PRECIOS = { 30000.0, 20000.0, 10000.0 };
  private static final String[] UBICACIONES = { "VIP", "PLATEA", "GENERAL" };

  // Promociones (cantidad minima, descuento)
  private static final double[][] PROMOCIONES = {
      { 2, 0.05 }, // 2 entradas: 5% descuento
      { 4, 0.10 } // 4+ entradas: 10% descuento
  };

  // Datos del sistema
  private static ArrayList<Entrada> entradas = new ArrayList<>();
  private static double totalIngresos = 0.0;
  private static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    menuPrincipal();
    scanner.close();
  }

  private static void menuPrincipal() {
    int opcion;
    do {
      System.out.println("\nTeatro Moro");
      System.out.println("1. vender entrada");
      System.out.println("2. ve promociones");
      System.out.println("3. buscar entradas");
      System.out.println("4. eliminar entrada");
      System.out.println("5. salir");
      System.out.print("Elige una opcion: ");

      opcion = leerEntero();

      switch (opcion) {
        case 1:
          venderEntrada();
          break;
        case 2:
          mostrarPromociones();
          break;
        case 3:
          buscarEntradas();
          break;
        case 4:
          eliminarEntrada();
          break;
        case 5:
          System.out.println("Adiós!");
          break;
        default:
          System.out.println("Opción incorrecta");
      }
    } while (opcion != 5);
  }

  private static int leerEntero() {
    while (true) {
      try {
        return Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.print("Entrada invalida. intenta nuevamente: ");
      }
    }
  }

  private static void venderEntrada() {
    System.out.println("\nuicaciones disponibles:");
    for (int i = 0; i < UBICACIONES.length; i++) {
      System.out.println((i + 1) + ". " + UBICACIONES[i] + " ($" + PRECIOS[i] + ")");
    }

    int ubicacion = seleccionarUbicacion();
    if (ubicacion == -1)
      return;

    int tipoCliente = seleccionarTipoCliente();
    if (tipoCliente == -1)
      return;

    int cantidad = seleccionarCantidad();
    if (cantidad == -1)
      return;

    double descuento = calcularDescuento(tipoCliente, cantidad);
    double precioFinal = calcularPrecioFinal(ubicacion, descuento, cantidad);

    registrarEntradas(ubicacion, tipoCliente, precioFinal, cantidad);

    System.out.println("\nEntradas vendidas (" + cantidad + " unidades):");
    System.out.println("Total a pagar: $" + precioFinal);
  }

  // Métodos nuevos para promociones
  private static double calcularDescuento(int tipoCliente, int cantidad) {
    double descuentoBase = 0.0;

    // descuento por tipo de cliente
    if (tipoCliente == 2)
      descuentoBase = 0.10;
    else if (tipoCliente == 3)
      descuentoBase = 0.15;

    // dscuento por promoción
    double descuentoPromo = 0.0;
    for (double[] promo : PROMOCIONES) {
      if (cantidad >= promo[0]) {
        descuentoPromo = promo[1];
        break; // la mejor promoción aplicable
      }
    }

    return descuentoBase + descuentoPromo;
  }

  private static void mostrarPromociones() {
    System.out.println("\nPromociones activas:");
    for (double[] promo : PROMOCIONES) {
      System.out.println("");
      System.out.println("- " + (int) promo[0] + "+ entradas: " + (int) (promo[1] * 100) + "% descuento adicional");
    }
  }

  // Métodos auxiliares
  private static int seleccionarUbicacion() {
    System.out.print("elige ubicación (1-3, 0 para cancelar): ");
    int seleccion = leerEntero();

    if (seleccion == 0)
      return -1;
    if (seleccion < 1 || seleccion > 3) {
      System.out.println("Ubicación inválida!");
      return -1;
    }
    return seleccion - 1;
  }

  private static int seleccionarTipoCliente() {
    System.out.println("\nTipo de cliente:");
    System.out.println("1. General");
    System.out.println("2. Estudiante");
    System.out.println("3. Tercera Edad");
    System.out.println("0. Cancelar");
    System.out.print("Elige opción: ");

    int seleccion = leerEntero();
    if (seleccion < 0 || seleccion > 3) {
      System.out.println("Opcion invalida");
      return -1;
    }
    return seleccion;
  }

  private static int seleccionarCantidad() {
    System.out.print("Cantidad de entradas (0 para cancelar): ");
    int cantidad = leerEntero();
    if (cantidad < 1) {
      System.out.println("Operacion cancelada");
      return -1;
    }
    return cantidad;
  }

  private static double calcularPrecioFinal(int ubicacion, double descuento, int cantidad) {
    return PRECIOS[ubicacion] * (1 - descuento) * cantidad;
  }

  private static void registrarEntradas(int ubicacion, int tipoCliente, double precioTotal, int cantidad) {
    String tipo = "General";
    if (tipoCliente == 2)
      tipo = "Estudiante";
    else if (tipoCliente == 3)
      tipo = "Tercera Edad";

    double precioUnitario = precioTotal / cantidad;

    for (int i = 0; i < cantidad; i++) {
      int numero = entradas.size() + 1;
      entradas.add(new Entrada(numero, UBICACIONES[ubicacion], tipo, precioUnitario));
    }

    totalIngresos += precioTotal;
  }

  private static void eliminarEntrada() {
    while (true) {
      System.out.println("\n--- Entradas Registradas ---");
      if (entradas.isEmpty()) {
        System.out.println("No hay entradas para mostrar!");
        return;
      }

      // Mostrar todas las entradas
      for (Entrada e : entradas) {
        System.out.println(e);
      }

      System.out.print("\nIngrese número de entrada a eliminar (0 para cancelar): ");
      int numero = leerEntero();

      if (numero == 0)
        return;

      boolean encontrada = false;
      for (int i = 0; i < entradas.size(); i++) {
        if (entradas.get(i).getNumero() == numero) {
          totalIngresos -= entradas.get(i).getPrecio();
          entradas.remove(i);
          System.out.println("Entrada #" + numero + " eliminada!");
          encontrada = true;
          break;
        }
      }

      if (!encontrada) {
        System.out.println("¡Número no válido! Intente nuevamente.");
      } else {
        return;
      }
    }
  }

  private static void buscarEntradas() {
    int opcion;
    do {
      System.out.println("\n--- Buscar Entradas ---");
      System.out.println("1. Por número");
      System.out.println("2. Por ubicación");
      System.out.println("3. Por tipo de cliente");
      System.out.println("4. Volver al menú principal");
      System.out.print("Seleccione opción: ");

      opcion = leerEntero();

      switch (opcion) {
        case 1:
          buscarPorNumero();
          break;
        case 2:
          buscarPorUbicacion();
          break;
        case 3:
          buscarPorTipoCliente();
          break;
        case 4:
          return;
        default:
          System.out.println("Opción inválida!");
      }
    } while (true);
  }

  private static void buscarPorNumero() {
    while (true) {
      System.out.print("Ingrese número (0 para cancelar): ");
      int numero = leerEntero();

      if (numero == 0)
        return;

      boolean encontrado = false;
      for (Entrada e : entradas) {
        if (e.getNumero() == numero) {
          System.out.println(e);
          encontrado = true;
          break;
        }
      }

      if (!encontrado) {
        System.out.println("No se encontró. Intente nuevamente!");
      } else {
        return;
      }
    }
  }

  private static void buscarPorUbicacion() {
    while (true) {
      System.out.println("\nUbicaciones disponibles:");
      System.out.println("1. VIP");
      System.out.println("2. PLATEA");
      System.out.println("3. GENERAL");
      System.out.print("Seleccione (0 para cancelar): ");

      int opcion = leerEntero();
      if (opcion == 0)
        return;

      String ubi = "";
      if (opcion >= 1 && opcion <= 3) {
        ubi = UBICACIONES[opcion - 1];

        boolean encontrado = false;
        for (Entrada e : entradas) {
          if (e.getUbicacion().equals(ubi)) {
            System.out.println(e);
            encontrado = true;
          }
        }

        if (!encontrado) {
          System.out.println("No hay entradas para esta ubicación");
        }
        return;
      }
      System.out.println("Opción inválida!");
    }
  }

  private static void buscarPorTipoCliente() {
    while (true) {
      System.out.println("\nTipos de cliente:");
      System.out.println("1. General");
      System.out.println("2. Estudiante");
      System.out.println("3. Tercera Edad");
      System.out.print("Seleccione (0 para cancelar): ");

      int opcion = leerEntero();
      if (opcion == 0)
        return;

      String tipo = "";
      if (opcion == 1)
        tipo = "General";
      else if (opcion == 2)
        tipo = "Estudiante";
      else if (opcion == 3)
        tipo = "Tercera Edad";
      else {
        System.out.println("Opción inválida!");
        continue;
      }

      boolean encontrado = false;
      for (Entrada e : entradas) {
        if (e.getTipoCliente().equals(tipo)) {
          System.out.println(e);
          encontrado = true;
        }
      }

      if (!encontrado) {
        System.out.println("No hay entradas de este tipo");
      }
      return;
    }
  }

  static class Entrada {
    private int numero;
    private String ubicacion;
    private String tipoCliente;
    private double precio;

    public Entrada(int numero, String ubicacion, String tipoCliente, double precio) {
      this.numero = numero;
      this.ubicacion = ubicacion;
      this.tipoCliente = tipoCliente;
      this.precio = precio;
    }

    public int getNumero() {
      return numero;
    }

    public String getUbicacion() {
      return ubicacion;
    }

    public String getTipoCliente() {
      return tipoCliente;
    }

    public double getPrecio() {
      return precio;
    }

    public String toString() {
      return "Entrada #" + numero +
          " | Ubicación: " + ubicacion +
          " | Tipo: " + tipoCliente +
          " | Precio: $" + precio;
    }
  }
}
