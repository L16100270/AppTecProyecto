# PFAppTec Puntos A Cubrir
# Intencion Explicita
Se utiliza en muchas partes de la aplicacion, practicamente para entrar a cualquier pantalla, por ejemplo cuando un usuario se loguea y
pasa a la siguiente Activity

# Intencion Implicita
En Ventana de Compra se puede contactar por llamada a quin realizo la publicacion con intencion implicita

# Menu
Se utiliza un menu tipo options, y un menu popup para cada elemento dentro del recycler view de publicaciones

# SnackBar
Se utiliza en conjunto con un FloatingActionButton para agregar una nueva publicacion

# RecyclerView
para visualizar las publicaciones se tiene el RecyclerView de publicaciones, tambien tiene un gesto SwipeRefreshLayout para
actualizar a voluntad segun el usuario

# Fragment
Al inicio de la aplicacion se carga un Fragment sobre el MainActivity, este fragment envuelve los elementos para que el usuario
ingrese sus credenciales

# Cuadro de dialogo
Se usa para confirmar la compra de una casa o para confirmar la publicacion de una casa o para confirmar salir de la aplicacion 
con el boton "BACK"

# Preferencias
Se agrega en un menu Optiones un apartado llamado Settings, donde el usuario puede activar desactivar un recordatorio del uso de la
aplicacio, esta preferencia se usa mas adelante

# Almacenamiento en Memoria Interna
Cuando un usuario se registra en la aplicacion, se guarda un respaldo con los datos del usuario en la memoria interna

# Almacenamiento en Memoria Externa
Se usa en varias partes del sistema, en el Registro de Usuario, Registro de Publicacion, Actualizacion de la cuenta para guardar una
imagen y actualizar ya sea foto de perfil o imagen de la casa, estas imagenes pueden ser vistas en la galeria del telefono

# ROOM
Se usa practicamente en toda la aplicacion desde que se loguea un usuario se hacen peticiones a la Base de datos, cuando se compra una
casa etc  

# Servicio
Al registrar un usuario se se regresa al Fragment en el login principal, y se queda ejecutando como servicio una funcion que respalda los datos 
del usuario, este servicio esta en la clase "MyService.kt"

# ContentProvider
Cuando se quiere agregar un numero telefonico al usuario se recuperan de la lista de contactos del telefono para que el usuario pueda elegir 
de estos

# Tarea Segundo Plano
Se uso AlarmManager, cuando se va a los Settings de la app en las preferencias antes mencionadas, al salir de la aplicacion se recupera esta
preferencia para activar o desactivar un recordatorio del uso de la aplicacion(esta programado en un relative de 30 segundos)

# Servicio Web
Originalmente se estaba usando el de Banxico: https://www.banxico.org.mx/SieAPIRest/service/v1/series/SF60653/datos/oportuno?token=2a695ec8a7ac3653658717e56143b2dbdbce0c51fd028712bec7ac42467fcff5
pero daba algunos problemas etc..

Se usa un web service que regresa el tipo de cambio que se utliza al momento de intercambiar la moneda para hacer la compra, y se pueda 
observar su precio en dolares 

**Version 1.0.0**

# License & Copyright
® Jose Ruben Rivera Castro 16100270
