/**
 * La clase operador toma los tableros que se validaron el la clase Tablero, y
 * efectúa operaciones sobre los mismos.
 */
public class Operador {

  /**
   * Contador de iteraciones, esto lo uso al hacer tests para probar que el
   * proceso de lectura sea el adecuado.
   */
  private int contadorLlamadas;

  /**
   * Constructor de la clase. Lo usaba para poder tener un contador de
   * iteraciones.
   */
  public Operador() {
    this.contadorLlamadas = 0;
  }

  /**
   * Se procesa el tablero aplicando las operaciones en orden hasta que ninguna
   * sea posible, luego se retorna el resultado para que se muestre a traves del
   * metodo que se ubica en la clase tablero.
   *
   * @param matriz el tablero.
   * @param filas  filas del tablero.
   * @param cols   columnas del tablero.
   * @return la matriz resultante para ser imprimida desde tablero.
   */
  public String[][] procesarMatriz(final String[][] matriz, final int filas,
      final int cols) {
    boolean seRealizoOperacion;
    contadorLlamadas = 0;

    do { // hay 4 tipos de operaciones posibles en el candy crush del enunciado.
      seRealizoOperacion = false;
      contadorLlamadas++; /*
                           * llevo un recuento de iteraciones,
                           * esto es una prueba, no se mantendrá en la
                           * vesion final.
                           */

      // Intenta aplicar gravedad.
      if (aplicarGravedad(matriz, filas, cols)) {
        seRealizoOperacion = true;
        continue; // Vuelve al inicio del ciclo.
      }

      // Intenta Operación 1.
      if (realizarOperacion1(matriz, filas, cols)) {
        seRealizoOperacion = true;
        continue; // Vuelve al inicio del ciclo.
      }

      // Intenta Operación 2.
      if (realizarOperacion2(matriz, filas, cols)) {
        seRealizoOperacion = true;
        continue;
      }

      // Intenta Operación 3.
      if (realizarOperacion3(matriz, filas, cols)) {
        seRealizoOperacion = true;
        continue;
      }

      // Intenta Operación 4.
      if (realizarOperacion4(matriz, filas, cols)) {
        seRealizoOperacion = true;
        continue;
      }

    } while (seRealizoOperacion); /*
                                   * Continúa mientras se pueda realizar
                                   * alguna operación.
                                   */
    // prueba:
    // System.out.println("Total de iteraciones: " + contadorLlamadas);
    return matriz;
  }

  /**
   * Operación de primer orden de prioridad: filas y columnas de 5 o más
   * elementos.
   *
   * @param matriz el tablero.
   * @param filas  filas del tablero.
   * @param cols   columnas del tablero.
   * @return true si se realizó la operacion.
   */
  private boolean realizarOperacion1(final String[][] matriz, final int filas,
      final int cols) {
    // Recorrer la matriz
    for (int i = 0; i < filas; i++) {
      for (int j = 0; j < cols; j++) {
        // En cada posición, verificar secuencia horizontal y vertical
        int longitudHorizontal = 1;
        int longitudVertical = 1;

        // Contar secuencia horizontal.
        if (j <= cols - 5) { // Solo si hay espacio para 5 elementos.
          while (j + longitudHorizontal < cols
              && tienenMismoNumero(matriz[i][j],
                  matriz[i][j + longitudHorizontal])) {
            longitudHorizontal++;
          }
        }

        // Contar secuencia vertical
        if (i <= filas - 5) { // Solo si hay espacio para 5 elementos.
          while (i + longitudVertical < filas
              && tienenMismoNumero(matriz[i][j],
                  matriz[i + longitudVertical][j])) {
            longitudVertical++;
          }
        }

        // Procesar la secuencia más larga si alguna es válida.
        if (longitudHorizontal >= 5 || longitudVertical >= 5) {
          char numero = matriz[i][j].charAt(1);

          // Si hay empate, procesar vertical.
          if (longitudVertical >= 5) {
            // Caso 1: procesar vertical:
            for (int k = 0; k < longitudVertical; k++) {
              revisarEfecto(matriz, i + k, j, filas, cols);
            }

            matriz[i][j] = "B" + numero;

            for (int k = 1; k < longitudVertical; k++) {
              matriz[i + k][j] = "--";
            }

            return true;
          } else {
            // Caso 2: procesar horizontal:
            for (int k = 0; k < longitudHorizontal; k++) {
              revisarEfecto(matriz, i, j + k, filas, cols);
            }

            matriz[i][j] = "B" + numero;

            for (int k = 1; k < longitudHorizontal; k++) {
              matriz[i][j + k] = "--";
            }

            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Realizo la operación de segundo orden de prioridad, que es hacer recorridos
   * en forma de L y T.
   *
   * @param matriz el tablero.
   * @param filas  filas del tablero.
   * @param cols   columnas del tablero.
   * @return true si se realizó la operación.
   */
  private boolean realizarOperacion2(final String[][] matriz, final int filas,
      final int cols) {
    // Patrones dados en el enunciado de Jeisson.
    int[][][] patrones = { /*
                            * Nota: esto causa que haya muchos errores de
                            * checkstyle por los espacios entre
                            * las llaves. Mi compu no deja quitarlos.
                            */
        { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 2, 1 }, { 2, 2 } }, // L1
        { { 0, 0 }, { 1, 0 }, { 2, -2 }, { 2, -1 }, { 2, 0 } }, // L2
        { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 0 }, { 2, 0 } }, // L3
        { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 2 }, { 2, 2 } }, // L4
        { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 1 }, { 2, 1 } }, // T1
        { { 0, 0 }, { 1, 0 }, { 2, -1 }, { 2, 0 }, { 2, 1 } }, // T2
        { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 0 } }, // T3
        { { 0, 0 }, { 1, -2 }, { 1, -1 }, { 1, 0 }, { 2, 0 } } // T4
    };

    for (int i = 0; i < filas; i++) {
      for (int j = 0; j < cols; j++) {
        // Para cada patrón en el orden especificado...
        for (int p = 0; p < patrones.length; p++) {
          /*
           * Verificar que todas las posiciones del patrón estén dentro de
           * la matriz.
           */
          boolean valido = true;
          String[] elementos = new String[5];

          for (int k = 0; k < 5; k++) {
            int newFila = i + patrones[p][k][0];
            int newCol = j + patrones[p][k][1];

            if (newFila < 0 || newFila >= filas || newCol < 0
                || newCol >= cols) {
              valido = false;
              break;
            }
            elementos[k] = matriz[newFila][newCol];
          }

          /*
           * Si el patrón está dentro de la matriz y todos los elementos
           * son iguales.
           */
          if (valido && tienenMismoNumero(elementos)) {
            char numero = matriz[i][j].charAt(1);

            // Revisar efectos de todos los elementos
            for (int[] coord : patrones[p]) {
              revisarEfecto(matriz, i + coord[0], j + coord[1], filas, cols);
            }

            // coloco resultado.
            matriz[i][j] = "W" + numero;
            for (int k = 1; k < 5; k++) {
              matriz[i + patrones[p][k][0]][j + patrones[p][k][1]] = "--";
            }

            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Verificaciones de vertical y horizontal de 4 elementos.
   *
   * @param matriz el tablero.
   * @param filas  filas del tablero.
   * @param cols   columnas del tablero.
   * @return true si se realizó la operacion.
   */
  private boolean realizarOperacion3(final String[][] matriz, final int filas,
      final int cols) {
    // Verificar verticalmente:
    for (int i = 0; i < filas - 3; i++) {
      for (int j = 0; j < cols; j++) {
        if (tienenMismoNumero(matriz[i][j], matriz[i + 1][j],
            matriz[i + 2][j], matriz[i + 3][j])) {
          char numero = matriz[i][j].charAt(1);

          revisarEfecto(matriz, i, j, filas, cols);
          revisarEfecto(matriz, i + 1, j, filas, cols);
          revisarEfecto(matriz, i + 2, j, filas, cols);
          revisarEfecto(matriz, i + 3, j, filas, cols);
          matriz[i][j] = "V" + numero;
          matriz[i + 1][j] = "--";
          matriz[i + 2][j] = "--";
          matriz[i + 3][j] = "--";
          return true;
        }
      }
    }
    // Verificar horizontalmente:
    for (int i = 0; i < filas; i++) {
      for (int j = 0; j < cols - 3; j++) {
        if (tienenMismoNumero(matriz[i][j], matriz[i][j + 1],
            matriz[i][j + 2], matriz[i][j + 3])) {
          char numero = matriz[i][j].charAt(1);
          revisarEfecto(matriz, i, j, filas, cols); // Para primer elemento.
          revisarEfecto(matriz, i, j + 1, filas, cols); // Para segundo elemento
          revisarEfecto(matriz, i, j + 2, filas, cols); // Para tercer elemento.
          revisarEfecto(matriz, i, j + 3, filas, cols); // Para tercer elemento.

          matriz[i][j] = "H" + numero;
          matriz[i][j + 1] = "--";
          matriz[i][j + 2] = "--";
          matriz[i][j + 3] = "--";
          return true;
        }
      }
    }
    return false;
  }

  /**
   * La mas sencilla: Vertical u horizontal de 3 elementos, no genera ningun
   * elemento.
   *
   * @param matriz el tablero.
   * @param filas  filas del tablero.
   * @param cols   columnas del tablero.
   * @return true si se realizó la operacion.
   */
  private boolean realizarOperacion4(final String[][] matriz, final int filas,
      final int cols) {

    for (int i = 0; i < filas; i++) {
      for (int j = 0; j < cols; j++) {
        boolean haySecuenciaVertical = false;
        boolean haySecuenciaHorizontal = false;

        // Verificar si hay secuencia vertical desde esta posición
        if (i <= filas - 3) {
          haySecuenciaVertical = tienenMismoNumero(matriz[i][j],
              matriz[i + 1][j],
              matriz[i + 2][j]);
        }

        // Verificar si hay secuencia horizontal desde esta posición
        if (j <= cols - 3) {
          haySecuenciaHorizontal = tienenMismoNumero(matriz[i][j],
              matriz[i][j + 1],
              matriz[i][j + 2]);
        }

        // Si hay alguna secuencia válida, procesarla
        if (haySecuenciaVertical) {
          // Revisar efectos
          revisarEfecto(matriz, i, j, filas, cols);
          revisarEfecto(matriz, i + 1, j, filas, cols);
          revisarEfecto(matriz, i + 2, j, filas, cols);

          // Eliminar elementos
          matriz[i][j] = "--";
          matriz[i + 1][j] = "--";
          matriz[i + 2][j] = "--";

          return true;
        } else if (haySecuenciaHorizontal) {
          // Revisar efectos
          revisarEfecto(matriz, i, j, filas, cols);
          revisarEfecto(matriz, i, j + 1, filas, cols);
          revisarEfecto(matriz, i, j + 2, filas, cols);

          // Eliminar elementos
          matriz[i][j] = "--";
          matriz[i][j + 1] = "--";
          matriz[i][j + 2] = "--";

          return true;
        }
      }
    }

    return false;
  }

  private boolean aplicarGravedad(final String[][] matriz, final int filas,
      final int cols) {
    // Para cada columna
    for (int j = 0; j < cols; j++) {
      // Para cada fila desde abajo hacia arriba (excepto la primera fila)
      for (int i = filas - 1; i > 0; i--) {
        if (matriz[i][j].equals("--")) { /*
                                          * Si encontramos un
                                          * espacio vacío.
                                          */
          // Revisar todas las filas arriba buscando un elemento para caer.
          for (int k = i - 1; k >= 0; k--) {
            if (!matriz[k][j].equals("--")) {
              // Intercambiar el elemento con el espacio vacío.
              String temp = matriz[i][j];
              matriz[i][j] = matriz[k][j];
              matriz[k][j] = temp;
              return true; // esto hace muchas iteraciones...

            }
          }
        }
      }
    }
    return false;
  }

  /**
   * Se verifica si tres elementos tienen el mismo color.
   * Esto se lo pedí a la IA para no tener 3 métodos iguales para los
   * casos de 5, 4 y 3 elementos.
   *
   * @param elementos elementos del tablero.
   * @return false si la casilla está vacía, "--".
   */
  private boolean tienenMismoNumero(final String... elementos) {
    // Verificar que ningún elemento sea "--"
    for (String elem : elementos) {
      if (elem.equals("--")) {
        return false;
      }
    }

    // Obtener el número del primer elemento.
    char numeroBase = elementos[0].charAt(1);

    // Comparar todos los demás con el primero.
    for (int i = 1; i < elementos.length; i++) {
      if (elementos[i].charAt(1) != numeroBase) {
        return false;
      }
    }

    return true;
  }

  private void revisarEfecto(final String[][] matriz, final int fila,
      final int columna, final int filas, final int cols) {
    char letra = matriz[fila][columna].charAt(0);
    char numero = matriz[fila][columna].charAt(1);

    // Marcar el elemento actual como procesado
    matriz[fila][columna] = "--";

    if (letra == 'V') {
      // Elimina toda la columna
      for (int i = 0; i < filas; i++) {
        if (matriz[i][columna].charAt(0) == 'V'
            || matriz[i][columna].charAt(0) == 'H'
            || matriz[i][columna].charAt(0) == 'B'
            || matriz[i][columna].charAt(0) == 'W') {
          revisarEfecto(matriz, i, columna, filas, cols);
        }
        matriz[i][columna] = "--";
      }
    } else if (letra == 'H') {
      // Elimina toda la fila
      for (int j = 0; j < cols; j++) {
        if (matriz[fila][j].charAt(0) == 'V'
            || matriz[fila][j].charAt(0) == 'H'
            || matriz[fila][j].charAt(0) == 'B'
            || matriz[fila][j].charAt(0) == 'W') {
          revisarEfecto(matriz, fila, j, filas, cols);
        }
        matriz[fila][j] = "--";
      }
    } else if (letra == 'B') {
      // Elimina todos los elementos con el mismo número
      for (int i = 0; i < filas; i++) {
        for (int j = 0; j < cols; j++) {
          if (matriz[i][j].charAt(1) == numero) {
            if (matriz[i][j].charAt(0) == 'V'
                || matriz[i][j].charAt(0) == 'H'
                || matriz[i][j].charAt(0) == 'B'
                || matriz[i][j].charAt(0) == 'W') {
              revisarEfecto(matriz, i, j, filas, cols);
            }
            matriz[i][j] = "--";
          }
        }
      }
    } else if (letra == 'W') {
      // Elimina los 8 elementos adyacentes
      for (int i = fila - 1; i <= fila + 1; i++) {
        for (int j = columna - 1; j <= columna + 1; j++) {
          if (i >= 0 && i < filas && j >= 0
              && j < cols && !(i == fila && j == columna)) {
            if (matriz[i][j].charAt(0) == 'V'
                || matriz[i][j].charAt(0) == 'H'
                || matriz[i][j].charAt(0) == 'B'
                || matriz[i][j].charAt(0) == 'W') {
              revisarEfecto(matriz, i, j, filas, cols);
            }
            matriz[i][j] = "--";
          }
        }
      }
    }
  }
}
