import java.util.*;
import java.util.concurrent.TimeUnit;

enum TipoTicket {
  VIP(15000), PALCO(10000), GENERAL(5000);

  private final double precioBase;

  TipoTicket(double precioBase) {
    this.precioBase = precioBase;
  }

  public double getPrecioBase() {
    return precioBase;
  }
}

enum EstadoTicket {
  DISPONIBLE, RESERVADO, VENDIDO
}

enum TipoDescuento {
  ESTUDIANTE(0.2), TERCERA_EDAD(0.3), GENERAL(0.0);

  private final double descuento;

  TipoDescuento(double descuento) {
    this.descuento = descuento;
  }

  public double getDescuento() {
    return descuento;
  }
}

class Ticket {
  private String ubicacion;
  private TipoTicket tipo;
  private EstadoTicket estado;
  private long tiempoReserva;
  private double precioFinal;
  private TipoDescuento descuentoAplicado;

  public Ticket(String ubicacion, TipoTicket tipo) {
    this.ubicacion = ubicacion;
    this.tipo = tipo;
    this.precioFinal = 0.0;
    this.descuentoAplicado = null;
    this.estado = EstadoTicket.DISPONIBLE;
  }

  public String getUbicacion() {
    return ubicacion;
  }

  public TipoTicket getTipo() {
    return tipo;
  }

  public EstadoTicket getEstado() {
    return estado;
  }

  public void setEstado(EstadoTicket estado) {
    if (this.estado == EstadoTicket.VENDIDO && estado != EstadoTicket.VENDIDO) {
      this.precioFinal = 0.0;
      this.descuentoAplicado = null;
    }
    this.estado = estado;
  }

  public long getTiempoReserva() {
    return tiempoReserva;
  }

  public void setTiempoReserva(long tiempo) {
    this.tiempoReserva = tiempo;
  }

  public double getPrecioFinal() {
    if (this.estado == EstadoTicket.VENDIDO) {
      return precioFinal;
    } else {
      return 0.0;
    }
  }

  private void setPrecioFinal(double precioFinal) {
    this.precioFinal = precioFinal;
  }

  public TipoDescuento getDescuentoAplicado() {
    return descuentoAplicado;
  }

  public void setDescuentoAplicado(TipoDescuento descuentoAplicado) {
    this.descuentoAplicado = descuentoAplicado;
  }

  public void calcularYEstablecerPrecioFinal(TipoDescuento descuento) {
    this.descuentoAplicado = descuento;
    if (this.descuentoAplicado != null) {
      double precioCalculado = this.tipo.getPrecioBase() * (1 - this.descuentoAplicado.getDescuento());
      this.setPrecioFinal(precioCalculado);
    } else {

      this.setPrecioFinal(this.tipo.getPrecioBase());
      this.descuentoAplicado = TipoDescuento.GENERAL;
    }
  }// Obtener por índice
}

public class exp2_S6_Diego_Alvarez {

  private static final String NOMBRE_TEATRO = "Teatro Moro";
  private static double totalIngresos = 0;
  private static int totalEntradasVendidas = 0;

  private List<Ticket> asientos;

  public exp2_S6_Diego_Alvarez() {
    asientos = new ArrayList<>();
    inicializarAsientos();
  }

  private void inicializarAsientos() {
    for (int i = 1; i <= 100; i++) {
      TipoTicket tipo;
      if (i <= 20)
        tipo = TipoTicket.VIP;
      else if (i <= 50)
        tipo = TipoTicket.PALCO;
      else
        tipo = TipoTicket.GENERAL;
      String ubicacion = tipo.name() + "-" + i;
      asientos.add(new Ticket(ubicacion, tipo));
    }
  }

  public void mostrarMenu() {
    Scanner sc = new Scanner(System.in);
    int opcion;
    do {
      System.out.println("\n=== " + NOMBRE_TEATRO + " ===");
      System.out.println("1. Reservar entradas");
      System.out.println("2. Comprar entradas");
      System.out.println("3. Modificar estado de entrada");
      System.out.println("4. Imprimir detalle de entrada");
      System.out.println("5. Salir");
      System.out.print("Opción: ");
      opcion = sc.nextInt();
      sc.nextLine();

      switch (opcion) {
        case 1:
          reservarEntradas(sc);
          break;
        case 2:
          comprarEntradas(sc);
          break;
        case 3:
          modificarEstadoEntrada(sc);
          break;
        case 4:
          imprimirDetalleEntrada(sc);
          break;
      }
    } while (opcion != 5);
    System.out.println("Total ingresos: $" + totalIngresos);
    System.out.println("Entradas vendidas: " + totalEntradasVendidas);
    sc.close();
  }

  private void reservarEntradas(Scanner sc) {
    int tiempoReserva = 15;
    System.out.println("\nAsientos disponibles:");
    asientos.stream()
        .filter(t -> t.getEstado() == EstadoTicket.DISPONIBLE)
        .forEach(t -> System.out.print(t.getUbicacion() + " "));

    System.out.println("\nIngrese asientos a reservar (ej: VIP-1, PALCO-21):");
    List<Ticket> reservados = procesarSeleccion(sc, EstadoTicket.RESERVADO);
    reservados.forEach(t -> {
      t.setTiempoReserva(System.currentTimeMillis());
    });
    System.out.println("Reserva válida por " + tiempoReserva + " minutos");
  }

  private void comprarEntradas(Scanner sc) {
    System.out.println("\nAsientos disponibles o reservados (expirados):");
    asientos.stream()
        .filter(t -> t.getEstado() != EstadoTicket.VENDIDO)
        .forEach(t -> System.out.print(t.getUbicacion() + " "));

    System.out.println("\nIngrese asientos a comprar:");
    List<Ticket> comprados = procesarSeleccion(sc, EstadoTicket.VENDIDO);

    if (!comprados.isEmpty()) {
      System.out.println("Tipo de descuento (ESTUDIANTE, TERCERA_EDAD, GENERAL):");
      TipoDescuento descuento = TipoDescuento.valueOf(sc.nextLine().toUpperCase());

      double totalCompra = 0;
      System.out.println("\n--- Resumen Compra ---");
      for (Ticket t : comprados) {
        t.calcularYEstablecerPrecioFinal(descuento);
        totalCompra += t.getPrecioFinal();
        if (t.getEstado() == EstadoTicket.VENDIDO) {
          totalEntradasVendidas++;
          totalIngresos += t.getPrecioFinal();
        }
        System.out.println("Ticket: " + t.getUbicacion() +
            ", Precio: $" + t.getPrecioFinal() +
            ", Descuento: " + descuento.name());
      }
      System.out.println("Total Compra: $" + totalCompra);
      System.out.println("----------------------");
    }
  }

  private List<Ticket> procesarSeleccion(Scanner sc, EstadoTicket nuevoEstado) {
    String[] seleccion = sc.nextLine().split(",\\s*");
    List<Ticket> tickets = new ArrayList<>();

    for (String ubi : seleccion) {
      Optional<Ticket> ticket = asientos.stream()
          .filter(t -> t.getUbicacion().equalsIgnoreCase(ubi))
          .findFirst();

      if (ticket.isPresent()) {
        Ticket t = ticket.get();
        if (validarDisponibilidad(t)) {
          t.setEstado(nuevoEstado);
          tickets.add(t);
        } else
          System.out.println(ubi + " no disponible");
      } else
        System.out.println(ubi + " no existe");
    }
    return tickets;
  }

  private boolean validarDisponibilidad(Ticket t) {
    if (t.getEstado() == EstadoTicket.RESERVADO) {
      long tiempoTranscurrido = System.currentTimeMillis() - t.getTiempoReserva();
      long minutos = TimeUnit.MILLISECONDS.toMinutes(tiempoTranscurrido);
      if (minutos > 15) {
        System.out.println("Reserva para " + t.getUbicacion() + " expiró.");
        t.setEstado(EstadoTicket.DISPONIBLE);
        t.setDescuentoAplicado(null);
        return true;
      } else {
        return false;
      }
    }
    return t.getEstado() == EstadoTicket.DISPONIBLE;
  }

  private void modificarEstadoEntrada(Scanner sc) {
    System.out.println("\n--- Modificar Estado de Entrada ---");
    System.out.println("Lista de Entradas:");
    for (int i = 0; i < asientos.size(); i++) {
      Ticket t = asientos.get(i);
      System.out.printf("%d. %s (%s) - Estado: %s\n", (i + 1), t.getUbicacion(), t.getTipo().name(),
          t.getEstado().name());
    }

    System.out.print("Ingrese el número de la entrada a modificar (1-" + asientos.size() + ", 0 para cancelar): ");
    int numeroEntrada = sc.nextInt();
    sc.nextLine();

    if (numeroEntrada == 0) {
      System.out.println("Modificación cancelada.");
      return;
    }

    if (numeroEntrada < 1 || numeroEntrada > asientos.size()) {
      System.out.println("Número de entrada inválido.");
      return;
    }

    Ticket ticket = asientos.get(numeroEntrada - 1);
    EstadoTicket estadoActual = ticket.getEstado();
    System.out.println("\nEntrada seleccionada: " + ticket.getUbicacion() + ", Estado actual: " + estadoActual);

    System.out.println("\nSeleccione nuevo estado:");
    System.out.println("1. DISPONIBLE");
    System.out.println("2. RESERVADO");
    System.out.println("3. VENDIDO");
    System.out.println("0. Cancelar");
    System.out.print("Opción: ");
    int opcionEstado = sc.nextInt();
    sc.nextLine();

    EstadoTicket nuevoEstado = null;
    switch (opcionEstado) {
      case 1:
        nuevoEstado = EstadoTicket.DISPONIBLE;
        break;
      case 2:
        nuevoEstado = EstadoTicket.RESERVADO;
        break;
      case 3:
        nuevoEstado = EstadoTicket.VENDIDO;
        break;
      case 0:
        System.out.println("Modificación cancelada.");
        return;
      default:
        System.out.println("Opción inválida.");
        return;
    }

    double precioAntes = ticket.getPrecioFinal();
    boolean estabaVendido = (estadoActual == EstadoTicket.VENDIDO);

    ticket.setEstado(nuevoEstado);
    double precioDespues = 0.0;

    switch (nuevoEstado) {
      case VENDIDO:
        System.out.println("Tipo de descuento (ESTUDIANTE, TERCERA_EDAD, GENERAL):");
        TipoDescuento descuento = TipoDescuento.valueOf(sc.nextLine().toUpperCase());
        ticket.calcularYEstablecerPrecioFinal(descuento);
        precioDespues = ticket.getPrecioFinal();
        System.out
            .println("Entrada " + ticket.getUbicacion() + " marcada como VENDIDA. Precio final: $" + precioDespues);
        break;

      case RESERVADO:
        ticket.setTiempoReserva(System.currentTimeMillis());
        System.out.println("Entrada " + ticket.getUbicacion() + " marcada como RESERVADA.");

        break;

      case DISPONIBLE:
        System.out.println("Entrada " + ticket.getUbicacion() + " marcada como DISPONIBLE.");
        break;
    }

    boolean estaVendidoAhora = (nuevoEstado == EstadoTicket.VENDIDO);

    totalIngresos += (precioDespues - precioAntes);

    if (estabaVendido && !estaVendidoAhora) {
      totalEntradasVendidas--;
    } else if (!estabaVendido && estaVendidoAhora) {
      totalEntradasVendidas++;
    }

  }

  private void imprimirDetalleEntrada(Scanner sc) {
    System.out.println("\n--- Imprimir Detalle de Entrada ---");
    System.out.println("Lista de Entradas:");
    for (int i = 0; i < asientos.size(); i++) {
      Ticket t = asientos.get(i);
      System.out.printf("%d. %s (%s) - Estado: %s\n", (i + 1), t.getUbicacion(), t.getTipo().name(),
          t.getEstado().name());
    }

    System.out.print("Ingrese el número de la entrada para ver detalle (1-" + asientos.size() + ", 0 para cancelar): ");
    int numeroEntrada = sc.nextInt();
    sc.nextLine(); // Consumir newline

    if (numeroEntrada == 0) {
      System.out.println("Operación cancelada.");
      return;
    }

    if (numeroEntrada < 1 || numeroEntrada > asientos.size()) {
      System.out.println("Número de entrada inválido.");
      return;
    }

    Ticket ticket = asientos.get(numeroEntrada - 1);

    System.out.println("\n--- DETALLE ENTRADA " + ticket.getUbicacion() + " ---");
    System.out.println("Teatro: " + NOMBRE_TEATRO);
    System.out.println("Ubicación: " + ticket.getUbicacion());
    System.out.println("Tipo: " + ticket.getTipo().name());
    System.out.println("Estado: " + ticket.getEstado().name());

    if (ticket.getEstado() == EstadoTicket.VENDIDO) {
      System.out.println("Descuento Aplicado: "
          + (ticket.getDescuentoAplicado() != null ? ticket.getDescuentoAplicado().name() : "N/A"));
      System.out.println("Precio Final: $" + ticket.getPrecioFinal());
    } else if (ticket.getEstado() == EstadoTicket.RESERVADO) {
      long ahora = System.currentTimeMillis();
      long restante = ticket.getTiempoReserva() + TimeUnit.MINUTES.toMillis(15) - ahora;
      if (restante > 0) {
        System.out.println("Tiempo restante de reserva: " + TimeUnit.MILLISECONDS.toMinutes(restante) + " min");
      } else {
        System.out.println("Reserva Expirada");
      }
      System.out.println("Precio Final: $0.0");
    } else {
      System.out.println("Precio Final: $0.0");
    }
    System.out.println("-----------------------");
  }

  public static void main(String[] args) {
    new exp2_S6_Diego_Alvarez().mostrarMenu();
  }
}
