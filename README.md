# Chat-con-Socket-Cliente-Servidor

Aplicacion realizada en JAVA Netbeans 8.2

## Instalacion: 
En este proyecto se hara una aplicacion cliente/servidor pos socket pa chatear atraves de ella se utiliza para el envio de la contraseña el protocolo de seguridad SSH , la misma esta compuesta por dos proyectos independientes entre si.
En el repositorio aparecen tres carpeta: 
	* Carpeta /RedesII_Chat_Cliente : Proyecto para correr el servidor
	* Carpeta /RedesII_Chat_Servidor : Proyecto para correr el cliente
	* Carpeta /Librerias : son las librerias necesesarias para la ejecuacion de ambos proyectos

	### Si los proyectos al descargarlo dan error , es debido a que hay que agregar los .JAR que estan en la carpeta Librerias

## CREDENCIALES
	* Servidor : 	

	| USUARIO | CONTRASEÑA|
	| ----------- | -----------|
	| system |   |


	* CLiente :

	| USUARIO | CONTRASEÑA|
	| ----- | ---- |
	| herick | hola123 |
	| diorfelis | hola123 |
	| luis| hola123 |
	| jorge | hola123 |

## Funcionalidades:
	* Cliente :
		* El cliente puede logiarse con las credenciales de arriba , sino manda mensaje de error
		* Las credenciales se evian por el puerto 10101 y la contraseña por protocolo SSH
		* El cliente puede chatear con otro cliente y puede mandar mensajes al servidor
		* El cliente puede observar su lista de contactos y puede observar cuantos mensajes sin leer tiene y cuantas personas estan conectadas
		* El cliente puede desconectarse del servidor una vez reaizado su tiempo de ejecuccion

	* Servidor:
		* El servidor puede observar todo lo que se envia a traves del puerto seleccionado
		* El servidor puede agregar mas usuarios
		* El servidor puede eliminar un usuario
		* El servidor puede mandar un mensaje a todos los clientes como un superusuario
		* El servidor puede puede ver a todos los usuarios conectados
		* El servidor puede limpiar su pantalla
## Errores:
	* Cliente:
		* En el login al ser incorrecto los campos text quedan en blanco 
		* si se esta en la misma pantalla de chat donde otro cliente te manda un mensaje sigue estando en el estado de "mensajes sin leer"
		* No se hace los agregar/eliminar la lista 
	* Servidor: 
		* En el login al ser incorrecto los campos text quedan en blanco 
