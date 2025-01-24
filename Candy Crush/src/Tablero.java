import java.util.Scanner;

/**
 * La clase tablero tiene como propósito recibir el input del usuario y
 * verificar si los tableros contenidos en dicho input son válidos o no.
 *
 * Nota: el funcionar de esta clase es un poco extraño. Sé que habiendo usado
 * listas, podría haber tenido mejores resultados. Sin embargo, en materia de
 * tiempo, me resultaba más eficiente no usar listas que sacar el rato para
 * aprenderlas, dado el poco tiempo que me queda para hacer el trabajo.
 *
 * También, hay otras formas más sencillas e intuitivas de resolverlo de manera
 * que, igual que este, pase los tests al usar el makefile. Sin embargo, al
 * menos para mi terminal, esas formas de estructurar la clase no funcionaban
 * bien, porque si ingresaba un input largo, daba problemas.
 *
 * Yo estoy haciendo el supuesto de que es posible darle entrada directo desde
 * la terminal al programa, lo cual no seria lo mas sensato para este tipo de
 * programa, que lee matrices con muchos elementos.
 *
 * Si solo hiciera el supuesto de que tiene que pasar los tests con el makefile,
 * la solución (esta clase en particular) sería más sencilla. Como en los
 * Hackerrank.
 *
 * @author Enrique Ramírez.
 *         correo: yeral.ramirez@ucr.ac.cr
 *         Carnet: c16272.
 */
public class Tablero {

  /**
   * La matriz donde almacenaremos los tableros.
   */
  private String[][] matriz;

  /**
   * La clase que hace operaciones sobre los tableros.
   */
  private Operador operador;

  /**
   * Constructor de la clase.
   */
  public Tablero() {
    this.operador = new Operador();
  }

  /**
   * El método ejecutar lee la entrada de la terminal. Para guardar esa entrada,
   * se crea un arreglo unidimensional de 10000 elementos. El tamaño lo escogí
   * yo de manera arbitraria.
   * Es decir, se asume que ninguna de las entradas que se proporcionen van a
   * tener más de 10 000 elementos.
   */
  public void ejecutar() {
    Scanner scanner = new Scanner(System.in);
    String[] elementos = new String[10000];
    int total = 0; // para contar la cantidad de elementos leidos.

    while (scanner.hasNext()) { /*
                                 * Agrego los elementos del input a un array
                                 * unidimensional.
                                 * Poco convencional pero sirve...
                                 */
      elementos[total++] = scanner.next();
    }
    scanner.close();

    procesarInput(elementos, total);
  }

  /**
   * Honestamente, le pedí ayuda a la IA con este método. está bien raro.
   *
   * @param elementos el arreglo unidimensional con el input.
   * @param i         el número de elemento.
   * @return un arreglo con las filas, columnas...
   */
  private int[] leerDimensiones(final String[] elementos, final int i) {
    int filas = Integer.parseInt(elementos[i]);
    int cols = Integer.parseInt(elementos[i + 1]);
    return new int[] { filas, cols, i + 2 };
  }

  /**
   * Se procesa la entrada brindada. Se verifica que tenga validez.
   *
   * @param elementos el arreglo unidimensional con el input.
   * @param total     la cantidad de elementos que tiene el input.
   *
   */
  private void procesarInput(final String[] elementos, final int total) {
    int i = 0;
    int numeroTablero = 1;

    /*
     * Esto es una solución sencilla al test case 22. Si el total de elementos
     * es menor a 11, se da invalid input
     *
     * ¿Porqué 11? porque el tamaño mínimo del tablero es 3x3. Un tablero válido
     * de esas dimensiones (que son las mínimas) tendría 9 elementos (dentro
     * de la matriz) más los dos elementos al inicio que son los dos int que
     * establecen el largo y ancho del tablero.
     *
     * De este caso se puede ver un punto débil que tiene esta clase. Las
     * dimensiones se toman como elementos.
     *
     * Ahora bien, para el caso específico del test case 22, con haber puesto
     * "if (total <2)", ya hubiera sido suficiente...
     */
    if (total < 11) {
      System.out.println("invalid input");

      return;
    }

    while (i < total - 1) {
      try {
        /*
         * Leo las dimensiones de mi tablero.
         */
        int[] dims = leerDimensiones(elementos, i);
        int filas = dims[0];
        int cols = dims[1];
        i = dims[2];

        if (filas < 3 || cols < 3) {
          System.out.println("invalid input");

          return;
        }

        matriz = new String[filas][cols];
        for (int f = 0; f < filas; f++) {
          for (int c = 0; c < cols; c++) {
            String elemento = elementos[i++];
            matriz[f][c] = elemento;

            if (!elemento.equals("--")
                && (elemento.length() != 2
                    || !("RVHWB".contains(String.valueOf(
                        elemento.charAt(0)))
                        && "123456".contains(String.valueOf(
                            elemento.charAt(1)))))) {
              System.out.println("invalid input");

              return;
            }
          }
        }

        boolean esUltimoTablero = (i >= total - 1);
        matriz = operador.procesarMatriz(matriz, filas, cols);
        imprimirMatriz(numeroTablero++, matriz, filas, cols, esUltimoTablero);

      } catch (Exception e) {
        System.out.println("invalid input");
        return;
      }
    }
  }

  /**
   * Se muestran los tableros a los que se les hizo operaciones.
   *
   * @param numero          es el numero de tablero.
   * @param matriz          el tablero a imprimir, ya con las operaciones
   *                        hechas.
   * @param filas           las dimensiones del tablero.
   * @param cols            dimensiones del tablero.
   * @param esUltimoTablero indica si es el último tablero a imprimir
   */
  private void imprimirMatriz(final int numero, final String[][] matriz,
      final int filas, final int cols, final boolean esUltimoTablero) {
    System.out.println(numero + ":");
    for (int i = 0; i < filas; i++) {
      for (int j = 0; j < cols; j++) {
        System.out.print(matriz[i][j]);
        if (j < cols - 1) {
          System.out.print(" ");
        }
      }
      System.out.println();
    }
    if (!esUltimoTablero) {
      System.out.println();
    }
  }
}
