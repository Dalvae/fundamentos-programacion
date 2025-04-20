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
    private TipoTicket tipo;
    private EstadoTicket estado;
    private long tiempoReserva;
    private double precioFinal;

    public Ticket(TipoTicket tipo) {
        this.tipo = tipo;
        this.estado = EstadoTicket.DISPONIBLE;
    }

    public TipoTicket getTipo() {
        return tipo;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public void setEstado(EstadoTicket estado) {
        this.estado = estado;
        if (estado != EstadoTicket.VENDIDO) {
            this.precioFinal = 0.0;
        }
    }

    public long getTiempoReserva() {
        return tiempoReserva;
    }

    public void setTiempoReserva(long tiempo) {
        this.tiempoReserva = tiempo;
    }

    public double getPrecioFinal() {
        return estado == EstadoTicket.VENDIDO ? precioFinal : 0.0;
    }

    public void calcularPrecioFinal(TipoDescuento descuento) {
        this.precioFinal = tipo.getPrecioBase() * (1 - descuento.getDescuento());
    }
}

public class exp2_S6_Diego_Alvarez {
    private static final String NOMBRE_TEATRO = "Teatro Moro";
    private static double totalIngresos = 0;
    private static int totalEntradasVendidas = 0;

    private List<Ticket> tickets;

    public exp2_S6_Diego_Alvarez() {
        tickets = new ArrayList<>();
        inicializarTickets();
    }

    private void inicializarTickets() {
        for (int i = 1; i <= 100; i++) {
            TipoTicket tipo = i <= 20 ? TipoTicket.VIP : 
                              i <= 50 ? TipoTicket.PALCO : 
                              TipoTicket.GENERAL;
            tickets.add(new Ticket(tipo));
        }
    }

    public void mostrarMenu() {
        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n=== " + NOMBRE_TEATRO + " ===");
            System.out.println("1. Reservar entradas\n2. Comprar entradas\n3. Modificar estado\n4. Imprimir detalle\n5. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt();
            sc.nextLine();
            
            switch (opcion) {
                case 1: reservarEntradas(sc); break;
                case 2: comprarEntradas(sc); break;
                case 3: modificarEstadoEntrada(sc); break;
                case 4: imprimirDetalleEntrada(sc); break;
            }
        } while (opcion != 5);
        
        System.out.println("Total ingresos: $" + totalIngresos + "\nEntradas vendidas: " + totalEntradasVendidas);
        sc.close();
    }

    private void mostrarTicketsPorEstados(EstadoTicket... estados) {
        boolean hayTickets = false;
        
        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            if (estados.length == 0 || estadoEnLista(t.getEstado(), estados)) {
                System.out.print((i + 1) + ". " + t.getTipo() + " (" + t.getEstado() + ")  ");
                if ((i + 1) % 5 == 0) System.out.println();
                hayTickets = true;
            }
        }
        
        if (!hayTickets) {
            System.out.println("No hay tickets disponibles.");
        }
        System.out.println();
    }

    private boolean estadoEnLista(EstadoTicket estado, EstadoTicket... estados) {
        for (EstadoTicket e : estados) {
            if (e == estado) return true;
        }
        return false;
    }

    private void reservarEntradas(Scanner sc) {
        System.out.println("\nTickets disponibles para reserva:");
        mostrarTicketsPorEstados(EstadoTicket.DISPONIBLE);

        System.out.println("\nIngrese números de tickets a reservar (ej: 1,2,3):");
        List<Ticket> reservados = procesarSeleccion(sc, EstadoTicket.RESERVADO);
        
        for (Ticket t : reservados) {
            t.setTiempoReserva(System.currentTimeMillis());
        }
        
        System.out.println("Reserva válida por 15 minutos");
    }

    private void comprarEntradas(Scanner sc) {
        System.out.println("\nTickets disponibles para compra:");
        mostrarTicketsPorEstados(EstadoTicket.DISPONIBLE);

        System.out.println("\nIngrese números de tickets a comprar (ej: 1,2,3):");
        List<Ticket> comprados = procesarSeleccion(sc, EstadoTicket.VENDIDO);

        if (!comprados.isEmpty()) {
            System.out.println("Tipo de descuento (ESTUDIANTE, TERCERA_EDAD, GENERAL):");
            TipoDescuento descuento = TipoDescuento.valueOf(sc.nextLine().toUpperCase());

            double totalCompra = 0;
            System.out.println("\n--- Resumen Compra ---");
            
            for (Ticket t : comprados) {
                t.calcularPrecioFinal(descuento);
                totalEntradasVendidas++;
                totalIngresos += t.getPrecioFinal();
                totalCompra += t.getPrecioFinal();
                
                System.out.println("Ticket #" + (tickets.indexOf(t) + 1) + 
                                 " (" + t.getTipo() + ")" + 
                                 " - Precio: $" + t.getPrecioFinal());
            }
            
            System.out.println("Total Compra: $" + totalCompra);
            System.out.println("----------------------");
        }
    }

    private List<Ticket> procesarSeleccion(Scanner sc, EstadoTicket nuevoEstado) {
        String[] seleccion = sc.nextLine().split(",\\s*");
        List<Ticket> ticketsSeleccionados = new ArrayList<>();

        for (String numStr : seleccion) {
            try {
                int num = Integer.parseInt(numStr.trim());
                if (num < 1 || num > tickets.size()) {
                    System.out.println("Ticket #" + num + " no existe");
                    continue;
                }
                
                Ticket t = tickets.get(num - 1);
                if (validarDisponibilidad(t)) {
                    t.setEstado(nuevoEstado);
                    ticketsSeleccionados.add(t);
                } else {
                    System.out.println("Ticket #" + num + " no disponible");
                }
            } catch (NumberFormatException e) {
                System.out.println("Número inválido: " + numStr);
            }
        }
        return ticketsSeleccionados;
    }

    private boolean validarDisponibilidad(Ticket t) {
        if (t.getEstado() == EstadoTicket.RESERVADO) {
            long tiempoTranscurrido = System.currentTimeMillis() - t.getTiempoReserva();
            long minutos = TimeUnit.MILLISECONDS.toMinutes(tiempoTranscurrido);
            if (minutos > 15) {
                t.setEstado(EstadoTicket.DISPONIBLE);
                return true;
            }
            return false;
        }
        return t.getEstado() == EstadoTicket.DISPONIBLE;
    }

    private void modificarEstadoEntrada(Scanner sc) {
        System.out.println("\n--- Modificar Estado de Ticket ---");
        System.out.println("Tickets modificables:");
        mostrarTicketsPorEstados(EstadoTicket.RESERVADO, EstadoTicket.VENDIDO);

        System.out.print("Ingrese el número del ticket a modificar (0 para cancelar): ");
        int numero = sc.nextInt();
        sc.nextLine();

        if (numero == 0 || numero < 1 || numero > tickets.size()) {
            System.out.println("Operación cancelada o número inválido.");
            return;
        }

        Ticket ticket = tickets.get(numero - 1);
        EstadoTicket estadoActual = ticket.getEstado();
        
        if (estadoActual == EstadoTicket.DISPONIBLE) {
            System.out.println("No se puede modificar un ticket disponible.");
            return;
        }

        System.out.println("\nSeleccione nuevo estado:");
        System.out.println("1. DISPONIBLE\n2. RESERVADO\n3. VENDIDO\n0. Cancelar");
        System.out.print("Opción: ");
        int opcionEstado = sc.nextInt();
        sc.nextLine();

        EstadoTicket nuevoEstado = null;
        switch (opcionEstado) {
            case 1: nuevoEstado = EstadoTicket.DISPONIBLE; break;
            case 2: nuevoEstado = EstadoTicket.RESERVADO; break;
            case 3: nuevoEstado = EstadoTicket.VENDIDO; break;
            default: 
                System.out.println("Operación cancelada.");
                return;
        }

        double precioAntes = ticket.getPrecioFinal();
        boolean estabaVendido = (estadoActual == EstadoTicket.VENDIDO);

        ticket.setEstado(nuevoEstado);
        
        if (nuevoEstado == EstadoTicket.VENDIDO) {
            System.out.println("Tipo de descuento (ESTUDIANTE, TERCERA_EDAD, GENERAL):");
            TipoDescuento descuento = TipoDescuento.valueOf(sc.nextLine().toUpperCase());
            ticket.calcularPrecioFinal(descuento);
        } else if (nuevoEstado == EstadoTicket.RESERVADO) {
            ticket.setTiempoReserva(System.currentTimeMillis());
        }

        double precioDespues = ticket.getPrecioFinal();
        totalIngresos += (precioDespues - precioAntes);

        if (estabaVendido && nuevoEstado != EstadoTicket.VENDIDO) {
            totalEntradasVendidas--;
        } else if (!estabaVendido && nuevoEstado == EstadoTicket.VENDIDO) {
            totalEntradasVendidas++;
        }

        System.out.println("Estado modificado exitosamente.");
    }

    private void imprimirDetalleEntrada(Scanner sc) {
        System.out.println("\n--- Imprimir Detalle de Ticket ---");
        System.out.println("Tickets reservados y vendidos:");
        mostrarTicketsPorEstados(EstadoTicket.RESERVADO, EstadoTicket.VENDIDO);

        System.out.print("Ingrese el número del ticket para ver detalle (0 para cancelar): ");
        int numero = sc.nextInt();
        sc.nextLine();

        if (numero == 0 || numero < 1 || numero > tickets.size()) {
            System.out.println("Operación cancelada o número inválido.");
            return;
        }

        Ticket ticket = tickets.get(numero - 1);
        
        System.out.println("\n--- DETALLE TICKET #" + numero + " ---");
        System.out.println("Teatro: " + NOMBRE_TEATRO);
        System.out.println("Tipo: " + ticket.getTipo());
        System.out.println("Estado: " + ticket.getEstado());
        
        if (ticket.getEstado() == EstadoTicket.VENDIDO) {
            System.out.println("Precio Final: $" + ticket.getPrecioFinal());
        } else if (ticket.getEstado() == EstadoTicket.RESERVADO) {
            long tiempoRestante = ticket.getTiempoReserva() + TimeUnit.MINUTES.toMillis(15) - System.currentTimeMillis();
            if (tiempoRestante > 0) {
                System.out.println("Tiempo restante de reserva: " + TimeUnit.MILLISECONDS.toMinutes(tiempoRestante) + " min");
            } else {
                System.out.println("Reserva Expirada");
            }
        }
        System.out.println("-----------------------");
    }

    public static void main(String[] args) {
        new exp2_S6_Diego_Alvarez().mostrarMenu();
    }
}
