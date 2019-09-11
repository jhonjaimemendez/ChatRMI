# ChatRMI
Chat entre maquinas cliente y el servidor. Cada vez que un cliente solicita un servicio de chat, desde el servidor se abre una interfaz graficas. Ademas cada cliente debe estar logueado y sus mensajes son almacenados en un servidor de bases de datos.


# Arquitectura

La arquitectura de chat entre el servidor y el cliente es una arquitectura de 3 niveles, compuesta por el siguiente nivel: Persistencia configurada con el servidor de base de datos MySQL, el nivel de negocio implementado con la plataforma de desarrollo de Java y el nivel de cliente implementado con el desarrollo de Java plataforma.

Esta arquitectura permite que la aplicación se implemente en 3 máquinas diferentes, una para cada nivel.

# Funcionamiento

La aplicación comienza desplegando inicialmente el servidor de la base de datos, luego el servidor de chat RMI y finalmente el cliente. 

Ahora, el cliente, al iniciar la aplicación, inicia sesión, si el proceso es correcto, se muestra una interfaz gráfica para iniciar el chat con el servidor, de la misma manera que se muestra una interfaz gráfica en el servidor. Cuando inicia el chat (el servidor o el cliente pueden iniciar el chat). Al enviar los mensajes, se cifran con un algoritmo AES y el receptor los descifra. Teniendo en cuenta que AES es un algoritmo simétrico, el servidor genera la clave y la comparte el cliente al momento de iniciar sesión.

Todos los mensajes entre el cliente y el servidor se almacenan en la base de datos llamada AgaChat, por lo que todos los usuarios están registrados en la base de datos.


# Instalacion y configuracion

Ver video adjunto

# Tecnologías de la información

A continuación se muestra una lista de las tecnologías de información utilizadas para el desarrollo del proyecto:

Database engine	MySQL: version 10.1.30
Programming language:	Java version 8

