# Quasar Fire Challenge
Backend en Java 17 con Springboot que permite, dada una flota satelital, ubicar la posición desde la que se está enviando un mensaje.

### Challenge
Se pueden revisar los detalles específicos del challenge en el archivo 'Operacion Fuego de Quasar v1.1.pdf'

### Requisitos
* Java 17
* Maven

## Ejecución
```bash
mvn spring-boot:run
```

## Uso
El contexto base es /api/v1 y corre en el puerto 8080.
También se encuentra hosteado en render.com

Los satelites son 3, llamados como en el challenge (Kenobi, Skywalker, y Sato)

La api se encuentra documentada en api/v1/swagger-ui/index.html
#### Nivel 1
Al iniciar la aplicación, se ejecuta la clase ConsoleApplication.java e imprime en pantalla los requisitos del nivel 1.

#### Nivel 2
El programa recibe un Array de Satelites con los mensajes y distancias que cada uno recibe.

Si se envía un solo Satelite, el programa informa que no es suficiente la información para hallar la posición.
Si se envía un array de solo dos Satelites, el programa intentará hallar la posición, y de no ser posible se informará.
Si se envía un array de 3 o mas satelites, el programa hará lo mismo con un mayor grado de presición.

#### Nivel 3
Al iniciar, la aplicación carga 3 satelites con las posiciones definidas en el challenge, el script usado es data.sql en la carpeta resources.
Al enviar un mensaje a un satelite, empieza una nueva "serie", la cual no se resuelve hasta consultar la posición y mensaje.

Si se quiere enviar un mensaje a un satelite que ya lo recibió, antes de consultar el resultado, el programa informa de esto.
Si se quiere obtener el resultado pero es necesaria mas información, el programa también informa de esto.

Una vez que se llama a GET /topsecret_split y la información es concluyente para determinar el mensaje y la posición, la serie se termina y se puede cargar nuevamente mensajes a los satelites que ya los tenían.

## Tests unitarios y reportes de coverage

#### Junit y Jacoco
El proyecto cuenta con tests unitarios y de mutación.

Para ejecutar los tests y generar un reporte
```
mvn clean verify
```
el reporte se puede ver accediendo al html en /target/site/index.html

#### Mutation testing (PITest)
Los test de mutación se pueden ejecutar con
```
mvn test-compile org.pitest:pitest-maven:mutationCoverage
```
el reporte se puede ver accediendo al html en /target/pit-reports/index.html

## Casos de prueba

#### Nivel 2
Se puede usar el siguiente curl
```
curl --location 'localhost:8080/api/v1/topsecret' \
--header 'Content-Type: application/json' \
--data '{
    "satellites": [
        {
            "name": "kenobi",
            "distance": 300.0,
            "message": [
                "este",
                "",
                "",
                "mensaje",
                ""
            ]
        },
        {
            "name": "skywalker",
            "distance": 633.0,
            "message": [
                "",
                "es",
                "",
                "",
                "diferente"
            ]
        },
        {
            "name": "sato",
            "distance": 1000,
            "message": [
                "este",
                "",
                "un",
                "",
                ""
            ]
        }
    ]
}'
```
y la respuesta esperada es
```
{
    "position": {
        "x": -500,
        "y": 100
    },
    "message": "este es un mensaje diferente"
}
```

Se pueden quitar satelites, modificar mensajes o distancias para obtener distintos resultados.

#### Nivel 3

Se cargan 3 satelites con los siguientes curl

###### Kenobi
```
curl --location 'localhost:8080/api/v1/topsecret_split/kenobi' \
--header 'Content-Type: application/json' \
--data '{
"distance": 300.0,
"message": ["este", "", "", "mensaje", ""]
}'
```
###### Skywalker
```
curl --location 'localhost:8080/api/v1/topsecret_split/skywalker' \
--header 'Content-Type: application/json' \
--data '{
"distance": 633.0,
"message": ["", "es", "", "", "unico"]
}'
```
###### Sato
```curl --location 'localhost:8080/api/v1/topsecret_split/sato' \
--header 'Content-Type: application/json' \
--data '{
"distance": 1000.0,
"message": ["este", "", "un", "", ""]
}'
```

y finalmente obtenemos la posición y el mensaje con el curl
```
curl --location 'localhost:8080/api/v1/topsecret_split'
```

que debería regresar
```
{
    "position": {
        "x": -500,
        "y": 100
    },
    "message": "este es un mensaje unico"
}
```

Al igual que en el nivel 2, se puede intentar obtener la posición con menos satelites, con diferentes distancias, o modificar el mensaje.

