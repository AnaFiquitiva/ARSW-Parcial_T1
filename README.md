
### Escuela Colombiana de Ingeniería
### Arquitecturas de Software - ARSW
## Ejercicio Fórmula BBP - Parcial Practico

**Ejercicio Fórmula BBP**

La fórmula [BBP](https://en.wikipedia.org/wiki/Bailey-Borwein-Plouffe_formula) es un algoritmo que permite calcular el enésimo dígito de PI en base 16, sin necesitar calcular los n-1 dígitos anteriores. Esto lo convierte en un problema vergonzosamente paralelo.

Para este ejercicio se quiere calcular, en el menor tiempo posible, al menos el primer millón de dígitos de PI (en base 16). Para esto:

1. Cree una clase de tipo Thread que represente el ciclo de vida de un hilo que calcule una parte de los dígitos requeridos.
2. Haga que la función PiDigits.getDigits() reciba como parámetro adicional un valor N, correspondiente al número de hilos entre los que se va a paralelizar la solución. Haga que dicha función espere hasta que los N hilos terminen de resolver el problema para combinar las respuestas y entonces retornar el resultado.
3. Ajuste la implementación para que cada 5 segundos los hilos se detengan e impriman el número de dígitos que han procesado y una vez se presione la tecla enter que los hilos continúen su proceso.

---

## ¿Qué se hizo?

### Punto 2 - Paralelización con N hilos

Se modificó el método getDigits de la clase PiDigits para que recibiera un parámetro adicional N que indica cuántos hilos se van a usar para calcular los dígitos de PI.

El proceso fue el siguiente:

**Paso 1 - Validaciones:** Se agregaron las validaciones necesarias para que el valor de N sea positivo y los demás parámetros sean válidos. Si alguno falla, lanza una excepción.

**Paso 2 - Creación y lanzamiento de hilos:** Se divide el total de dígitos a calcular entre los N hilos de forma equitativa. Si el número de dígitos no es exactamente divisible, el último hilo se encarga del sobrante. Cada hilo recibe su rango y se lanza.

**Paso 3 - Join y combinación de resultados:** Una vez lanzados todos los hilos, el método espera a que cada uno termine usando join(). Cuando todos terminan, se combinan los resultados parciales en un solo arreglo que se retorna.

El resultado fue verificado comparando la salida del método original (sin hilos) contra el nuevo (con 2 hilos), obteniendo resultados idénticos.

---

### Punto 3 - Pausa cada 5 segundos (avance)

Se empezó a trabajar en el mecanismo de pausa. Se modificó la clase PiDigitsThread para que:

- Tenga un flag compartido (paused) que indica si los hilos deben pausarse.
- Tenga un objeto lock compartido para sincronización entre hilos.
- Procese los dígitos en bloques pequeños, verificando en cada bloque si debe pausarse usando wait().
- Lleve un contador processed con el número de dígitos calculados hasta el momento.

El mecanismo de llamar la pausa cada 5 segundos y esperar el Enter del usuario quedó pendiente de implementar en el Main.

---

## ¿Cómo correrlo?

### Requisitos
- Java 8 o superior
- Maven

### Pasos

1. Compilar y ejecutar:
```
mvn compile exec:java -Dexec.mainClass="edu.eci.arsw.math.Main"
```

2. La salida esperada es:
```
Sin hilos:   243F6A8885
Con 2 hilos: 243F6A8885
Resultados iguales: true
```
