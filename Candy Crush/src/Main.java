/**
 * Clase principal del analizador de tableros de Candy Crush.
 *
 * @author Enrique Ramírez.
 *         correo: yeral.ramirez@ucr.ac.cr
 *         Carnet: c16272.
 *
 */
public class Main {
  /**
   * Se inicia la ejecución del programa.
   *
   * @param args Argumentos del programa.
   */
  public static void main(final String[] args) {

    /*
     * Creo una instancia de la clase Tablero y llamo al método ejecutar.
     */
    Tablero tablero = new Tablero();
    tablero.ejecutar();
  }
}
