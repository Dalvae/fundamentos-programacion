import java.util.Scanner;

public class Exp2_S4_Diego_Alvarez {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean activo = true;
        int entradas = 0;
        int total = 0;

        int precioA = 15000;
        int precioB = 10000;
        int precioC = 7000;

        while (activo) {
            System.out.println("TEATRO MORO");
            System.out.println("1. Comprar entradaa");
            System.out.println("2. salir");
            System.out.print("Elije: ");

            String opcion = scanner.nextLine();

            if (opcion.equals("1")) {
                entradas++;
                System.out.println("entrada #" + entradas);

                // seleccio de zona
                System.out.println("zonas Disponibles: A) $" + precioA + " B) $" + precioB + " C) $" + precioC);
                String zona = "";
                int precio = 0;

                while (true) {
                    System.out.print("Elije zona (A/B/C): ");
                    zona = scanner.nextLine().toUpperCase();

                    if (zona.equals("A")) {
                        precio = precioA;
                        break;
                    } else if (zona.equals("B")) {
                        precio = precioB;
                        break;
                    } else if (zona.equals("C")) {
                        precio = precioC;
                        break;
                    }
                    System.out.println("Opcion no valida!");
                }

                // Edad y descuentos
                System.out.print("Edad del cliente: ");
                int edad = Integer.parseInt(scanner.nextLine());
                String descuentoMsg = "";

                if (edad < 18) {
                    descuentoMsg = "10% dto (Menor de edad)";
                    precio = (int) (precio * 0.9);
                } else if (edad >= 60) {
                    descuentoMsg = "15% dto (3ra edad)";
                    precio = (int) (precio * 0.85);
                }

                // Mostrar descuento si aplica
                if (!descuentoMsg.isEmpty()) {
                    System.out.println("Descuento aplicado: " + descuentoMsg);
                }

                total += precio;
                System.out.println("Total a pagar:$" + precio);
                System.out.println("Total acumulado: $" + total);

            } else if (opcion.equals("2")) {
                System.out.println("Total entradas vendidas: " + entradas);
                System.out.println("Recaudacion total: $" + total);
                System.out.println("gracias!");
                activo = false;
            } else {
                System.out.println("Opcion no reconocida");
            }
        }
        scanner.close();
    }
}