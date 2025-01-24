# Verificador de tableros de Candy Crush

## Autor: Enrique Ramírez

## Descripción del problema:

Se debe implementar un programa que reciba un tablero junto con sus dimensiones, verifique si dicho tablero es válido según las reglas establecidas, y, en caso de ser válido, aplicar una serie de operaciones sobre la representación matricial del tablero.

Las operaciones a aplicar tienen una jerarquía de importancia.


| Prioridad | Tipo de combinación                          | Efecto                          |
|-----------|----------------------------------------------|----------------------------------|
| 1         | Vertical u horizontal de 5 o más elementos  | Genera bomba de color (B)       |
| 2         | En forma de L o T (ver adelante)            | Genera elemento empaquetado (W) |
| 3         | Vertical de 4 elementos                     | Genera elemento de rayas verticales (V) |
| 3         | Horizontal de 4 elementos                   | Genera elemento de rayas horizontales (H) |
| 4         | Vertical u horizontal de 3 elementos        | No genera ningún elemento       |

Cuando se hayan hecho todas las operaciones posibles, se muestra el resultado. Si la entrada que se está leyendo incumple las reglas, se muestra `invalid input` y se para de analizar los tableros subsiguientes, si hay.

Cada elemento del tablero tiene un nombre y color.

El color es representado por un número:

| Código | Color representado |
|--------|---------------------|
| 1      | Rojo               |
| 2      | Naranja            |
| 3      | Amarillo           |
| 4      | Verde              |
| 5      | Azul               |
| 6      | Morado             |

El nombre es representado por una letra. Note que eliminar ciertas letras tiene consecuencias:

| Código | Nombre                        | Descripción                                                                                          |
|--------|-------------------------------|------------------------------------------------------------------------------------------------------|
| R      | Elemento regular              | Al ser eliminado no provoca que otros elementos se destruyan.                                       |
| V      | Elemento de rayas verticales  | Al ser eliminado provoca que se eliminen todos los elementos de la columna en que se encuentra.     |
| H      | Elemento de rayas horizontales| Al ser eliminado provoca que se eliminen todos los elementos de la fila en que se encuentra.        |
| W      | Elemento empaquetado (wrapped)| Al ser eliminado provoca que los ocho elementos alrededor suyo sean destruidos.                     |
| B      | Bomba de color                | Al ser eliminado, todos los elementos de su mismo color que están en el tablero (en el orden izquierda a derecha y de arriba hacia abajo). |

```
Ejemplo de un tablero: 
5 6
R1 R1 R1 R1 R1 R1
R1 R2 R2 R2 R5 R4
R1 R2 R3 R6 R5 R4
R1 R2 R3 R6 R5 R4
R1 R3 R3 R3 R6 R4
```

## Análisis y estructura del proyecto

De la fase de análisis, se decidió implementar una clase que recibiera el input y validara los tableros, y otra que hiciera operaciones sobre los mismos.

- La clase `Tablero` envía a `Operador` los tableros que son válidos para que les haga las operaciones correspondientes.

- La clase `Operador` hace todas las operaciones posibles sobre el tablero en un orden jerárquico. 

- Luego de efectuadas las operaciones, se devuelve el resultado a la clase `Tablero` para que lo proporcione como output.

Una alternativa podría haber separado el programa en más clases. Por ejemplo, que haya una clase que solo se encargue de recibir la entrada y dar salida de datos, mientras que una segunda clase valide dicha entrada, siendo la tercera clase la que aplica operaciones.

### Proceso de validación

La clase tablero recibe una serie de tableros (matrices) con dimensiones. Los tableros vienen separados por lineas vacias.

```
Ejemplo: 

5 6
R1 R1 R1 R1 R1 R1
R1 R2 R2 R2 R5 R4
R1 R2 R3 R6 R5 R4
R1 R2 R3 R6 R5 R4
R1 R3 R3 R3 R6 R4

5 4
R1 R6 R4 R5
H1 R5 R5 V2
R1 R6 R4 R5
R4 R4 B4 R5
R4 R6 R5 W3
```

El método `procesarInput` verifica que se cumplan las condiciones necesarias para que la entrada sea válida. Es decir:

- Que haya un largo y ancho preestablecido.
- Que dichas dimensiones preestablecidas sean iguales a las dimensiones reales del tablero.
- Que cada elemento tenga el mismo formato. Es decir, una letra mayúscula seguida de un número entero positivo.
-  Los elementos solo pueden tener las letras `R,V,H,W, o B` y solo los `números del 1 al 6`, o ser un elemento vacío, que se representa con `--`. 

Luego de validar las condiciones, envía las matrices válidas a `Operador` para que se efectúen las operaciones necesarias.

El método `imprimirMatriz` muestra las matrices válidas. Si la matriz no era válida, en `procesarInput` se habría imprimido `invalid input` y se hubiera detenido la ejecución.

Nota:
- Esta clase asume que el usuario manda datos desde la terminal. Si se asume que solo tenemos que pasar los tests con el makefile de Jeisson, entonces la clase sería menos extraña, similar a como en los Hackerrank.

Independientemente de lo anterior, el programa pasa los tests, lo que se comprueba usando el makefile de Jeisson.

### Proceso de modificación del tablero:

En la clase `Operador`, el método `procesarMatriz` recibe cada tablero como un arreglo bidimensional String. Adicionalmente, recibe las dimensiones de cada tablero.

Se llama a los métodos que realizan cada una de las operaciones necesarias. Esto se hace en un orden jerárquico. Además, cada vez que se realiza una operación, el ciclo se reincia. 

Por ejemplo, si tengo la matriz

```
R5 R3 R1
R5 R6 H1
R5 R2 R1
```

Se revisa si se puede aplicar gravedad. No se puede pues el tablero está lleno.

Se revisa si se puede hacer la operacion de primer orden jerárquico. 

Como no es posible, se procede a intentar con la segunda. Tampoco es posible. La tercera tampoco.

Se procede a intentar con la cuarta. Es posible aplicarla, por lo que se elimina la primer columna, ya que los 3 elementos son del mismo color.

```
-- R3 R1
-- R6 H1
-- R2 R1
```

Entonces, después de eso, el ciclo vuelve a reiniciar. No se pueden efectuar la gravedad ni las primeras 3 operaciones del orden jerárquico. La cuarta sí, por lo que se aplica:

```
-- R3 --
-- R6 --
-- R2 --
```

Como el elemento en `fila 2` `columna 3` era un `H`, al ser eliminado, se eliminan todos elementos de la fila donde este se encontraba, por lo que queda así:
```
-- R3 --
-- -- --
-- R2 --
```
Nota: es importante resaltar que al hacer el efecto de `H`, seguimos en el mismo ciclo que cuando eliminamos su columna.

Ahora, se sale del ciclo y se verifica si se puede aplicar gravedad. Sí se puede:

```
-- -- --
-- R3 --
-- R2 --
```
Se sale del ciclo y se vuelve a intentar aplicar las operaciones. Como no es posible aplicar ninguna otra operación ni aplicar gravedad, se retorna la matriz.

## Ejecución

Para ejecutar la solución, se procede de la siguiente manera:

```
javac src/*.java -d build/
```
Luego,
```
Jar cfe bin/Directory.jar Main -C build .
```
Finalmente,
```
java -jar bin/Directory.jar
```
Una vez hecho eso, se ingresan las matrices a analizar, se muestra un ejemplo:

```
3 3
R1 R1 R1
R2 R3 R4
R5 R6 B1

3 3
R2 R3 R4
R1 R1 R1
R5 R6 B1

3 3
R2 R3 R4
R5 R6 B1
R1 R1 R1
```

Luego se debe presionar `enter`, y luego `control+d` para indicarle al programa que es el fin de la entrada.

El programa va a proporcionar el resultado para los datos ingresados.

```
1:
-- -- -- 
R2 R3 R4 
R5 R6 B1 

2:
-- -- -- 
R2 R3 R4 
R5 R6 B1 

3:
-- -- -- 
R2 R3 R4 
R5 R6 B1 
```
## Notas:

- Por la matriz compuesta que venia en el enunciado de Jeisson, el checkstyle da varios errores. Son porque se generan espacios entre los signos de llave. Creo que es por una extensión que tengo instalada en `VsCode`.

- Todos los tests pasan con el makefile de Jeisson, aunque le hice una modificación con ChatGPT porque no me corría bien.







