package main;

import exception.*;
import model.comercial.Categoria;
import model.comercial.MetodoDePago;
import model.comercial.Producto;
import model.enums.*;
import model.oscuro.ConsejoSombrio;
import model.oscuro.RegistroEsclavos;
import model.oscuro.TrabajadorEsclavizado;
import model.produccion.Fabrica;
import model.usuario.*;

import java.util.*;
import java.io.*;

public class Main {
    private static final Map <String, Usuario> usuarios = new HashMap<>();
    private static final Map<Long, Producto> productos = new HashMap<>();
    private static final Map<Long, Categoria> categorias = new HashMap<>();
    private static final Map<Long, Fabrica> fabricas = new HashMap<>();
    private static final Map<Long, TrabajadorEsclavizado> trabajadores = new HashMap<>();
    private static final RegistroEsclavos registro = RegistroEsclavos.getInstancia();
    private static final Scanner sc = new Scanner(System.in);
    private static final String claveEspecial = "Cabrita2025";
    
    // Nombres de archivos para persistencia
    private static final String ARCHIVO_USUARIOS = "usuarios.ser";
    private static final String ARCHIVO_PRODUCTOS = "productos.ser";
    private static final String ARCHIVO_CATEGORIAS = "categorias.ser";
    private static final String ARCHIVO_FABRICAS = "fabricas.ser";
    private static final String ARCHIVO_TRABAJADORES = "trabajadores.ser";
    private static final String ARCHIVO_REGISTRO = "registro.ser";


    public static void main(String[] args) {
        
        // Cargar datos guardados
        cargarDatos();

        boolean seguir = true;

        System.out.println("Bienvenido al Sistema de Glow Up");
        while (seguir) {

            System.out.println("Elija una de las siguientes opciones:");
            System.out.println("1. Iniciar Sesion");
            System.out.println("2. Crear Usuario");
            System.out.println("3. Salir");

            switch (sc.nextLine()) {
                case "1"->{
                    System.out.println("Por favor inicie sesión con su email y contraseña");
                    System.out.println("Digite su usuario");
                    String usuario = leerString();
                    System.out.println("Digite su contraseña");
                    String contrasena = leerString();
                    try {
                        Usuario usuarioActual = login(usuario, contrasena);
                        if (usuarioActual != null) {
                            switch (usuarioActual.getRol()) {
                                case CLIENTE -> menuCliente((Cliente) usuarioActual);
                                case DESARROLLADOR -> menuDesarrolladorProducto((DesarrolladorProducto)usuarioActual);
                                case ADMINISTRADOR_CONTENIDO -> menuAdminContenido((AdministradorContenido)usuarioActual);
                                case ADMINISTRADOR_USUARIO -> menuAdminUsuario((AdministradorUsuario)usuarioActual);
                                case DUENA -> menuDuena((Duena) usuarioActual);
                            }
                            usuarioActual = null;
                        }
                    } catch (UsuarioNoEncontradoException | AccesoDenegadoException e) {
                        System.out.println("Error de inicio de sesión: " + e.getMessage());
                    }
                }
                case "2"->{
                    menuCrearUsuario();
                }
                case "3"->{
                    guardarDatos();
                    seguir = false;
                }
                default -> {System.out.println("Opcion invalida, intente de nuevo");}

            }
        }
    }


    //login
    public static Usuario login(String usuario, String contrasena)
    throws UsuarioNoEncontradoException, AccesoDenegadoException {
        try{
            if (usuarios.containsKey(usuario)) {
                Usuario usuarioActual = usuarios.get(usuario);
                if (usuarioActual.getPasswordHash() != null && usuarioActual.getPasswordHash().equals(contrasena)) {
                    if (usuarioActual.getEstadoCuenta().equals(EstadoCuenta.ACTIVA)) {
                        System.out.println("Bienvenido/a " +  usuarioActual.getNombre());
                        return usuarioActual;
                    } else {
                        throw new AccesoDenegadoException("Su cuenta está suspendida, contacte a soporte");
                    }

                } else {
                    throw new AccesoDenegadoException("Contraseña incorrecta");
                }
            } else {
                throw new UsuarioNoEncontradoException("Usuario no encontrado");
            }
        } catch (AccesoDenegadoException|UsuarioNoEncontradoException e){
            System.out.println("Error de login: " + e.getMessage());
        }
        return null;
    }

    //Menus
    public static void menuPanelDeUsuario(Usuario usuarioActual) {
        boolean seguir = true;
        while (seguir) {
            System.out.println("Panel de usuario");
            System.out.println("Nombre: " + usuarioActual.getNombre());
            System.out.println("Email: " + usuarioActual.getEmail());
            System.out.println("Password Hash: " + usuarioActual.getPasswordHash());
            System.out.println("Rol: " + usuarioActual.getRol());
            System.out.println("Estado de la cuenta: " + usuarioActual.getEstadoCuenta());
            System.out.println("Presione 1 para editar su perfil o cualquier tecla para volver");
            String opcion = sc.nextLine();
            if (opcion.equals("1")) {
                System.out.println("Por favor elija que desea editar");
                System.out.println("1. Nombre");
                System.out.println("2. Email");
                System.out.println("3. Password Hash");
                System.out.println("Presione cualquier tecla para salir");
                switch (sc.nextLine()) {
                    case "1"->{
                        System.out.println("Por favor introduce el nuevo nombre del usuario:");
                        usuarioActual.setNombre(leerString());
                        System.out.println("\n✅Nombre de usuario actualizado exitosamente a " + usuarioActual.getNombre()+"\n");
                        seguir = false;
                    }
                    case "2"->{
                        System.out.println("Por favor introduce el nuevo email del usuario:");
                        usuarioActual.setEmail(leerString());
                        System.out.println("\n✅Email actualizado exitosamente a " + usuarioActual.getEmail()+"\n");
                        seguir = false;
                    }
                    case "3"->{
                        System.out.println("Por favor introduce el nuevo password del usuario:");
                        usuarioActual.setPasswordHash(leerString());
                        System.out.println("\n✅Contraseña actualizada exitosamente a " + usuarioActual.getPasswordHash()+"\n");
                        seguir = false;
                    }
                    default -> {System.out.println("Saliendo...");seguir = false;}
                }
            } else {
                seguir = false;
            }
        }
    }
    public static void menuCrearUsuario(){
        boolean seguir = true;
        while (seguir){
            System.out.println("Seleccione que tipo de usuario desea crear:");
            System.out.println("1. Crear Administrador Usuario");
            System.out.println("2. Crear Administrador Contenido");
            System.out.println("3. Crear Cliente");
            System.out.println("4. Cancelar");
            switch (sc.nextLine()) {
                case "1"->{
                    try{
                        System.out.println("Por favor ingrese la clave maestra para crear este usuario");
                        String clave = sc.nextLine();
                        if(clave.equals(claveEspecial)){
                            RolUsuario rol = RolUsuario.ADMINISTRADOR_USUARIO;
                            EstadoCuenta estadoCuenta = EstadoCuenta.ACTIVA;
                            NivelAcceso nivelAcceso = NivelAcceso.BAJO;
                            System.out.println("Por favor ingrese la siguiente información");
                            System.out.println("Nombre:");
                            String nombre = leerString();
                            System.out.println("Email:");
                            String email = leerString();
                            if (usuarios.containsKey(email)) {
                                while (usuarios.containsKey(email)) {
                                    System.out.println("El email ya esta en uso intente con uno nuevo: ");
                                    email = leerString();
                                }
                            }
                            System.out.println("Password:");
                            String passwordHash = leerString();
                            boolean flag = true;
                            while (flag){
                                System.out.println("Seleccione la opcion que corresponde a sus nivel de acceso");
                                System.out.println("1. Bajo");
                                System.out.println("2. Medio");
                                System.out.println("3. Total");
                                switch(sc.nextLine()){
                                    case "1"->{
                                        flag = false;
                                    }
                                    case "2"->{
                                        nivelAcceso = NivelAcceso.MEDIO;
                                        flag = false;
                                    }
                                    case "3"->{
                                        nivelAcceso = NivelAcceso.TOTAL;
                                        flag = false;
                                    }
                                    default -> {System.out.println("\n❌Opcion invalida, intente de nuevo\n");}
                                }
                            }
                            AdministradorUsuario u = new AdministradorUsuario(nombre,email,passwordHash,rol,estadoCuenta,nivelAcceso);
                            usuarios.put(u.getEmail(),u);
                            System.out.println("\n✅Usuario creado exitosamente\n");
                        } else {
                            throw new AccesoDenegadoException("Clave maestra incorrecta");
                        }
                    } catch (AccesoDenegadoException e){
                        System.out.println("Error de acceso: " + e.getMessage());
                    }
                }
                case "2"->{
                    try{
                        System.out.println("Por favor ingrese la clave maestra para crear este usuario");
                        String clave = sc.nextLine();
                        if(clave.equals(claveEspecial)){
                            RolUsuario rol = RolUsuario.ADMINISTRADOR_CONTENIDO;
                            EstadoCuenta estadoCuenta = EstadoCuenta.ACTIVA;
                            PermisosEdicion permisosEdicion = PermisosEdicion.PARCIALES;
                            System.out.println("Por favor ingrese la siguiente información");
                            System.out.println("Nombre:");
                            String nombre = leerString();
                            System.out.println("Email:");
                            String email = leerString();
                            if (usuarios.containsKey(email)) {
                                while (usuarios.containsKey(email)) {
                                    System.out.println("El email ya esta en uso intente con uno nuevo: ");
                                    email = leerString();
                                }
                            }
                            System.out.println("Password:");
                            String passwordHash = leerString();
                            boolean flag = true;
                            while (flag){
                                System.out.println("Seleccione la opcion que corresponde a sus permisos");
                                System.out.println("1. Ninguno");
                                System.out.println("2. Parciales");
                                System.out.println("3. Totales");
                                switch(sc.nextLine()){
                                    case "1"->{
                                        permisosEdicion = PermisosEdicion.NINGUNO;
                                        flag = false;
                                    }
                                    case "2"->{
                                        flag = false;
                                    }
                                    case "3"->{
                                        permisosEdicion = PermisosEdicion.TOTALES;
                                        flag = false;
                                    }
                                    default -> {System.out.println("Opcion invalida, intente de nuevo");}
                                }
                            }
                            AdministradorContenido u = new AdministradorContenido(nombre,email,passwordHash,rol,estadoCuenta,permisosEdicion);
                            usuarios.put(u.getEmail(),u);
                            System.out.println("\n✅Usuario creado exitosamente\n");
                        } else {
                            throw new AccesoDenegadoException("\n❌Clave maestra incorrecta\n");
                        }
                    } catch (AccesoDenegadoException e){
                        System.out.println("Error de acceso: " + e.getMessage());
                    }
                }
                case "3"->{
                    RolUsuario rol =  RolUsuario.CLIENTE;
                    EstadoCuenta estadoCuenta = EstadoCuenta.ACTIVA;
                    System.out.println("Por favor proporcione la siguiente información: ");
                    System.out.println("Nombre:");
                    String nombre = leerString();
                    System.out.println("Email:");
                    String email = leerString();
                    if (usuarios.containsKey(email)) {
                        while (usuarios.containsKey(email)) {
                            System.out.println("El email ya esta en uso intente con uno nuevo: ");
                            email = leerString();
                        }
                    }
                    System.out.println("Password:");
                    String passwordHash = leerString();
                    System.out.println("Dirección de envio");
                    String direccion = leerString();
                    System.out.println("Teléfono");
                    String telefono = leerString();
                    Cliente cliente = new Cliente(nombre,email,passwordHash,rol,estadoCuenta,direccion,telefono);
                    usuarios.put(cliente.getEmail(),cliente);
                    System.out.println("\n✅Cliente creado exitosamente\n");
                }
                case "4"->{seguir = false;}
                default -> {System.out.println("Opcion invalida, intente de nuevo");}
            }
        }
    }
    public static void menuDesarrolladorProducto(DesarrolladorProducto usuarioActual) {
        System.out.println("Bienvenido al panel de desarrollador producto, seleccione una opción:");
        boolean seguir = true;
        while (seguir) {
            System.out.println("1. Acceder al panel de usuario");
            System.out.println("2. Trabajar duro");
            System.out.println("Presione cualquier otra tecla para salir");
            switch(sc.nextLine()){
                case "1"->{
                    menuPanelDeUsuario(usuarioActual);
                }
                case "2"->{
                    System.out.println("Trabajando duro");
                    System.out.println("Trabajando duro");
                    System.out.println("Trabajando duro");
                    System.out.println("Trabajando duro");
                    System.out.println("Trabajando duro");
                    System.out.println("Trabajando duro");
                    System.out.println("Trabajando duro");
                    System.out.println("Trabajando duro");
                }
                default -> {System.out.println("Saliendo..."); seguir = false;}
            }
        }

    }
    public static void menuAdminUsuario(AdministradorUsuario usuarioActual){
        System.out.println("Bienvenido al panel de administración de usuarios");
        boolean seguir = true;
        while (seguir) {
            System.out.println("A continuación se listan los usuarios:");
            mostrarUsuarios();
            System.out.println("Por favor ingrese el mail del usuario que desea seleccionar");
            String mail = sc.nextLine();
            try {
                if(usuarios.containsKey(mail)){
                    System.out.println("Seleccione que desea hacer con la cuenta del usuario: ");
                    System.out.println("1. Activar cuenta");
                    System.out.println("2. Suspender cuenta");
                    System.out.println("Presione cualquier tecla para salir");
                    switch (sc.nextLine()){
                        case "1"->{
                            usuarios.get(mail).setEstadoCuenta(EstadoCuenta.ACTIVA);
                        }
                        case "2"->{
                            usuarios.get(mail).setEstadoCuenta(EstadoCuenta.SUSPENDIDA);
                        }
                        default -> {System.out.println("Saliendo...");seguir = false;}
                    }
                } else {
                    throw new UsuarioNoEncontradoException("\n❌Usuario no encontrado\n");
                }
            } catch (UsuarioNoEncontradoException e) {
                System.out.println(e.getMessage());
                System.out.println("Saliendo");
                seguir = false;
            }

        }

    }
    public static void menuCliente(Cliente usuarioActual){
        boolean seguir = true;
        while (seguir) {
            System.out.println("Por favor elija una de las siguientes opciones");
            System.out.println("1. Añadir método de pago");
            System.out.println("2. Panel de usuario");
            System.out.println("3. Ir al menu de compra");
            System.out.println("4. Mostrar carrito");
            System.out.println("5. Comprar carrito");
            System.out.println("6. Vaciar carrito");
            System.out.println("7. Mostrar historial de compras");
            System.out.println("8. Salir");
            switch (sc.nextLine()) {
                case "1"->{
                    System.out.println("Por favor proporcione la siguiente informacion para añadir el método de pago:");
                    System.out.println("Digite el titular: ");
                    String titular = leerString();
                    System.out.println("Digite el numero: ");
                    String numeroEnmascarado = leerString();
                    System.out.println("Digite el saldo: ");
                    double saldo = pedirDouble();
                    TipoMetodoDePago tipo = null;
                    String opcion = sc.nextLine();
                    boolean flag = true;
                    while (flag){
                        System.out.println("Seleccione su tipo de método de pago");
                        System.out.println("1. Efectivo");
                        System.out.println("2. Tarjeta");
                        System.out.println("3. Transferencia");
                        System.out.println("4. Bono");
                        opcion = sc.nextLine();
                        switch (opcion) {
                            case "1"->{tipo = TipoMetodoDePago.EFECTIVO; flag = false;}
                            case "2"->{tipo = TipoMetodoDePago.TARJETA; flag = false;}
                            case "3"->{tipo = TipoMetodoDePago.TRANSFERENCIA; flag = false;}
                            case "4"->{tipo = TipoMetodoDePago.BONO; flag = false;}
                            default -> {System.out.println("\n❌Opcion invalida, intente de nuevo\n");}
                        }
                    }
                    MetodoDePago mp = new MetodoDePago(tipo,titular,numeroEnmascarado,saldo);
                    usuarioActual.anadirMetodoDePago(mp.getId(),mp);
                    System.out.println("Método de pago añadido con exito");
                }
                case "2"->{
                    menuPanelDeUsuario(usuarioActual);
                }
                case "3"->{
                    menuCompra(usuarioActual);
                }
                case "4"->{
                    try{
                        if(usuarioActual.getCarrito() == null){
                            throw new CarritoVacioException("El carrito esta vacio");
                        }
                        System.out.println(usuarioActual.getCarrito());
                        System.out.println("Total: "+ usuarioActual.getCarrito().calcularTotal());
                    } catch (CarritoVacioException e){
                        System.out.println(e.getMessage());
                    }
                }
                case "5"->{
                    if (usuarioActual.getMetodosDePago().isEmpty()){
                        System.out.println("No tienes métodos de pago, por favor añade uno para poder comprar");
                    } else {
                        System.out.println("Por favor elija uno de sus métodos de pago");
                        for (MetodoDePago metodo: usuarioActual.getMetodosDePago().values()){
                            System.out.println("Titular: " +metodo.getTitular());
                            System.out.println("Saldo: " +metodo.getSaldo()+"\n");
                        }
                        System.out.println("Escriba el id del método de pago con el que desea pagar");
                        Long idMetodo = pedirLong();
                        try {
                            usuarioActual.realizarCompra(idMetodo);
                        } catch (SaldoInsuficienteException|SinMetodoDePagoException|CarritoVacioException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                case "6"->{
                    try{
                        if (usuarioActual.getCarrito() != null) {
                            usuarioActual.getCarrito().vaciarCarrito();
                            System.out.println("Carrito vaciado exitosamente");
                        } else {
                            throw new CarritoVacioException("No se puede acceder al carrito pues está vacio");
                        }
                    } catch (CarritoVacioException e) {
                        System.out.println(e.getMessage());
                    }

                }
                case "7"->{
                    System.out.println(usuarioActual.getHistorialCompras());
                }
                case "8"->{seguir=false;}
                default -> {System.out.println("Opcion invalida, intente de nuevo");}
            }

        }
    }
    public static void menuDuena(Duena usuarioActual){

        boolean seguir = true;
        while(seguir){
            System.out.println("Elija que desea hacer: ");
            System.out.println("1. Acceder como Administrador de Usuario");
            System.out.println("2. Acceder como Administrador de Contenido");
            System.out.println("3. Acceder al Consejo Sombrio");
            System.out.println("4. Acceder al registro de esclavos");
            System.out.println("5. Acceder como clienta");
            System.out.println("6. Acceder como desarrollador de producto");
            System.out.println("Presione cualquier otra tecla para salir");
            String opcion = sc.nextLine();
            switch (opcion) {
                case "1" ->{
                    menuAdminUsuario(usuarioActual.getAdministradorUsuario());
                }
                case "2" ->{
                    menuAdminContenido(usuarioActual.getAdministradorContenido());
                }
                case "3" ->{
                    menuConsejoSombrio();
                }
                case "4" ->{
                    menuRegistroEsclavos();
                }
                case "5" ->{
                    menuCliente(usuarioActual.getCliente());
                }
                case "6" ->{
                    menuDesarrolladorProducto(usuarioActual.getDesarrolladorProducto());
                }
                default -> {
                    System.out.println("Saliendo....");
                    seguir = false;
                }
            }
        }

    }
    public static void menuAdminContenido(AdministradorContenido usuarioActual){
        boolean seguir = true;
        while(seguir){
            System.out.println("Por favor elija una de las siguientes acciones: ");
            System.out.println("1. Crear nuevo producto");
            System.out.println("2. Actualizar producto");
            System.out.println("3. Ver productos");
            System.out.println("4. Eliminar productos");
            System.out.println("5. Crear categoria");
            System.out.println("6. Actualizar categoria");
            System.out.println("7. Eliminar categoria");
            System.out.println("8. Ver categorias");
            System.out.println("9. Acceder a su Panel de Usuario");
            System.out.println("10. Salir");
            switch (sc.nextLine()) {
                case "1"->{
                    System.out.println("Creación de nuevo producto: Por favor proporcione la siguiente información");
                    System.out.println("Nombre: ");
                    String nombre = leerString();
                    System.out.println("Descripcion: ");
                    String descripcion = leerString();
                    System.out.println("Precio: ");
                    double precio = pedirDouble();
                    if (precio < 0){
                        while (precio < 0){
                            System.out.println("Vuelva a intentar, no se aceptan precios negativos");
                            precio = pedirDouble();
                        }
                    }
                    System.out.println("Stock: ");
                    Long stock = pedirLong();
                    if (stock < 0){
                        while (stock < 0){
                            System.out.println("Vuelva a intentar, no se aceptan stock negativos");
                            stock = pedirLong();
                        }
                    }

                    Categoria categoria = null;
                    if (!categorias.isEmpty()) {
                        System.out.println("A continuación se listan las categorias disponibles");
                        System.out.println("Escriba el id de la categoria a la que desea añadir el producto o -1 para no añadirlo a ninguna");
                        mostrarCategorias();
                        Long idCategoria = pedirLong();
                        sc.nextLine();
                        categoria = categorias.get(idCategoria);
                    }
                    Producto p = new Producto(nombre, descripcion,precio,stock,categoria);
                    productos.put(p.getId(), p);
                    System.out.println("\n✅ Producto añadido exitosamente!\n");
                }
                case "2"->{
                    menuActualizarProducto();
                }
                case "3"->{
                    mostrarProductos();
                }
                case "4"->{
                    try{
                        mostrarProductos();
                        System.out.println("Por favor escriba el id del producto que desea seleccionar");
                        Long idProducto = pedirLong();
                        sc.nextLine();
                        if (productos.containsKey(idProducto)){
                            productos.remove(idProducto);
                            System.out.println("\n✅ Producto eliminado exitosamente!\n");
                        } else {
                            throw new ProductoNoEncontradoException("\n❌Producto no encontrado por favor intente de nuevo\n");
                        }
                    } catch(ProductoNoEncontradoException e){
                        System.out.println(e.getMessage());
                    }
                }
                case "5"->{
                    System.out.println("Creación de nueva Categoria: Por favor proporcione la siguiente información");
                    System.out.println("Nombre: ");
                    String nombre = leerString();
                    System.out.println("Descripción: ");
                    String descripcion = leerString();
                    Categoria c = new Categoria(nombre,descripcion);
                    categorias.put(c.getId(),c);
                    System.out.println("\n✅Categoria creada exitosamente\n");
                }
                case "6"->{
                    menuActualizarCategoria();
                }
                case "7"->{
                    System.out.println("Eliminar Categoria: A continuación se listan las categorias");
                    mostrarCategorias();
                    System.out.println("Por favor escriba el id de la categoria que desea eliminar");
                    Long idCategoria = pedirLong();
                    sc.nextLine();
                    try{
                        if (categorias.containsKey(idCategoria)){
                            Categoria c = categorias.get(idCategoria);
                            c.getProductos().forEach(producto -> producto.setCategoria(null));
                            categorias.remove(idCategoria);
                            System.out.println("\n❌Categoria eliminada exitosamente\n");
                        } else {
                            throw new CategoriaNoEncontradaException("\n❌La categoria seleccionada no existe\n");
                        }
                    } catch (CategoriaNoEncontradaException e){
                        System.out.println(e.getMessage());
                    }

                }
                case "8"->{
                    mostrarCategorias();
                }
                case "9"->{
                    menuPanelDeUsuario(usuarioActual);
                }
                case "10"->{
                    seguir = false;
                }
                default -> {System.out.println("Opcion invalida intente de nuevo");}
            }
        }
    }
    public static void menuCompra(Cliente usuarioActual){
        System.out.println("Bienvenido al menú de compra de Glow Up");
        System.out.println("A continuación puede observar la lista de productos disponibles");
        boolean seguir = true;
        while (seguir) {
            mostrarProductos();
            System.out.println("Escriba el id del producto que desea seleccionar");
            Long idProducto = pedirLong();
            try{
                if (productos.containsKey(idProducto)) {
                    Producto p  = productos.get(idProducto);
                    System.out.println("Producto seleccionado: " + p.getNombre());
                    System.out.println("Eliga que desea hacer");
                    System.out.println("1. Obtener detalles de producto");
                    System.out.println("2. Añadir producto al carrito");
                    System.out.println("3. Volver");
                    switch (sc.nextLine()) {
                        case "1"->{
                            productos.get(idProducto).obtenerDetalles();
                        }
                        case "2"->{
                            System.out.println("Escriba la cantidad de productos que desea comprar");
                            Long cantidadProductos = pedirLong();
                            try{
                                usuarioActual.agregarAlCarrito(productos.get(idProducto), cantidadProductos);
                            } catch (StockInsuficienteException e){
                                System.out.println(e.getMessage());
                            }
                        }
                        case "3"->{seguir=false;}
                        default -> {System.out.println("\n❌Opcion invalida, intente de nuevo\n");}
                    }
                } else {
                    throw new ProductoNoEncontradoException("\n❌Producto no encontrado por favor intente de nuevo\n");
                }
            } catch (ProductoNoEncontradoException e){
                System.out.println(e.getMessage());
            }

        }
    }
    public static void menuActualizarProducto(){
        boolean seguir = true;
        while (seguir) {
            System.out.println("Actualizar producto: A continuación se listan todos los productos:");
            mostrarProductos();
            System.out.println("Escriba 1 si desea actualizar un producto o cualquier tecla para volver");
            String opcion = sc.nextLine();
            if (opcion.equals("1")){
                System.out.println("Escriba el id del producto a actualizar");
                Long id = pedirLong();
                sc.nextLine();
                try{
                    if(productos.containsKey(id)){
                        Producto p = productos.get(id);
                        System.out.println("Elija el campo que desea actualizar: ");
                        System.out.println("1. Nombre del producto");
                        System.out.println("2. Descripcion del producto");
                        System.out.println("3. Precio del producto");
                        System.out.println("4. Stock del producto");
                        System.out.println("5. Categoria del producto");
                        System.out.println("6 Salir");
                        switch (sc.nextLine()) {
                            case "1"->{
                                System.out.println("Escriba el nuevo nombre del producto");
                                String nombre = leerString();

                                Categoria categoriaActual = p.getCategoria();

                                Producto nuevoP = new Producto(nombre, p.getDescripcion(), p.getPrecio(), p.getStock(), categoriaActual);
                                nuevoP.setId(p.getId());

                                if (categoriaActual != null) {
                                    categoriaActual.getProductos().remove(p);
                                    categoriaActual.getProductos().add(nuevoP);
                                }

                                productos.put(nuevoP.getId(), nuevoP);

                                System.out.println("Producto actualizado exitosamente");
                            }
                            case "2"->{
                                System.out.println("Escriba la nueva descripcion del producto");
                                String descripcion = leerString();

                                Categoria categoriaActual = p.getCategoria();

                                Producto nuevoP = new Producto(p.getNombre(), descripcion, p.getPrecio(), p.getStock(), categoriaActual);
                                nuevoP.setId(p.getId());

                                if (categoriaActual != null) {
                                    categoriaActual.getProductos().remove(p);
                                    categoriaActual.getProductos().add(nuevoP);
                                }

                                productos.put(nuevoP.getId(), nuevoP);

                                System.out.println("Producto actualizado exitosamente");
                            }
                            case "3"->{
                                System.out.println("Escriba el nuevo precio del producto");
                                double precio = pedirDouble();
                                if (precio < 0){
                                    while (precio < 0){
                                        System.out.println("Vuelva a intentar, no se aceptan precios negativos");
                                        precio = pedirDouble();
                                    }
                                }

                                Categoria categoriaActual = p.getCategoria();

                                Producto nuevoP = new Producto(p.getNombre(), p.getDescripcion(), precio, p.getStock(), categoriaActual);
                                nuevoP.setId(p.getId());

                                if (categoriaActual != null) {
                                    categoriaActual.getProductos().remove(p);
                                    categoriaActual.getProductos().add(nuevoP);
                                }

                                productos.put(nuevoP.getId(), nuevoP);

                                System.out.println("\n✅Producto actualizado exitosamente\n");
                            }
                            case "4"->{
                                System.out.println("Escriba el nuevo stock del producto");
                                long stock = pedirLong();
                                if (stock < 0){
                                    while (stock < 0){
                                        System.out.println("Vuelva a intentar, no se aceptan stock negativos");
                                        stock = pedirLong();
                                    }
                                }

                                Categoria categoriaActual = p.getCategoria();

                                Producto nuevoP = new Producto(p.getNombre(), p.getDescripcion(), p.getPrecio(), stock, categoriaActual);
                                nuevoP.setId(p.getId());

                                if (categoriaActual != null) {
                                    categoriaActual.getProductos().remove(p);
                                    categoriaActual.getProductos().add(nuevoP);
                                }

                                productos.put(nuevoP.getId(), nuevoP);

                                System.out.println("Producto actualizado exitosamente");
                            }
                            case "5"->{
                                mostrarCategorias();
                                Categoria categoria = null;
                                if (!categorias.isEmpty()) {
                                    System.out.println("A continuación se listan las categorias disponibles");
                                    System.out.println("Escriba el id de la categoria a la que desea añadir el producto o -1 para no añadirlo a ninguna");
                                    mostrarCategorias();
                                    Long idNuevaCategoria = pedirLong();
                                    sc.nextLine();

                                    Categoria categoriaAntigua = p.getCategoria();
                                    if (categoriaAntigua != null) {
                                        categoriaAntigua.getProductos().remove(p);
                                    }

                                    Categoria nuevaCategoria = null;

                                    if (idNuevaCategoria != -1 && categorias.containsKey(idNuevaCategoria)) {
                                        nuevaCategoria = categorias.get(idNuevaCategoria);
                                    }

                                    Producto productoActualizado = new Producto(p.getNombre(), p.getDescripcion(), p.getPrecio(), p.getStock(), nuevaCategoria);

                                    if (nuevaCategoria != null) {
                                        nuevaCategoria.getProductos().add(productoActualizado);
                                    }

                                    productos.put(p.getId(), productoActualizado);
                                    System.out.println("\n✅Producto actualizado exitosamente\n");

                                } else {
                                    System.out.println("\n❌No hay categorias disponibles\n");
                                }
                            }
                            case "6"->{seguir=false;}
                            default -> {System.out.println("\n❌Opcion invalida intente de nuevo\n");}
                        }
                    } else {
                        throw new ProductoNoEncontradoException("\n❌Producto no encontrado por favor intente de nuevo\n");
                    }
                } catch (ProductoNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                seguir = false;
            }
        }
    }
    public static void menuActualizarCategoria(){
        boolean seguir = true;
        while (seguir) {
            System.out.println("Actualizar Categoria: A continuación se listan todas las categorias disponibles");
            mostrarCategorias();
            System.out.println("Escriba 1 para actualizar categoria o cualquier tecla para salir");
            String opcion = sc.nextLine();
            if (opcion.equals("1")) {
                System.out.println("Escriba el id de la categoria a actualizar");
                Long id = pedirLong();
                sc.nextLine();
                try{
                    if (categorias.containsKey(id)) {
                        Categoria c = categorias.get(id);
                        System.out.println("Elija el campo que desea actualizar: ");
                        System.out.println("1. Nombre");
                        System.out.println("2. Descripcion");
                        System.out.println("3. Salir");
                        String eleccion=sc.nextLine();
                        switch (eleccion) {
                            case "1"->{
                                System.out.println("Por favor proporcione el nuevo nombre");
                                String nombre = leerString();
                                ArrayList<Producto> productosCategoria = c.getProductos();
                                Categoria cNueva = new Categoria(nombre,c.getDescripcion());
                                cNueva.setId(c.getId());
                                cNueva.setProductos(productosCategoria);
                                for(Producto p : productosCategoria){
                                    p.setCategoria(cNueva);
                                }
                                categorias.put(id, cNueva);
                            }
                            case "2"->{
                                System.out.println("Por favor proporcione la nueva descripcion");
                                String descripcion = leerString();
                                ArrayList<Producto> productosCategoria = c.getProductos();
                                Categoria cNueva = new Categoria(c.getNombre(),descripcion);
                                cNueva.setId(c.getId());
                                cNueva.setProductos(productosCategoria);
                                for(Producto p : productosCategoria){
                                    p.setCategoria(cNueva);
                                }
                                categorias.put(id, cNueva);
                            }
                            case "3"->{
                                seguir = false;
                            }
                            default -> {System.out.println("\n❌Opcion invalida intente de nuevo\n");}
                        }
                    } else {
                        throw new CategoriaNoEncontradaException("\n❌No existe la categoria, intente de nuevo\n");
                    }
                } catch (CategoriaNoEncontradaException e){
                    System.out.println(e.getMessage());
                }
            } else {
                seguir = false;
            }
        }
    }
    public static void menuConsejoSombrio(){
        boolean seguir = true;
        while (seguir) {
            System.out.println("Menu sombrio");
            System.out.println("Seleccione que desea hacer");
            System.out.println("1. Añadir miembro del consejo");
            System.out.println("2. Eliminar miembro del consejo");
            System.out.println("3. Ver miembros del consejo");
            System.out.println("Cualquier tecla para salir");
            switch(sc.nextLine()){
                case "1"->{
                    System.out.println("A continuación se listan los usuarios");
                    mostrarUsuarios();
                    System.out.println("Escriba el mail del que desea añadir");
                    String mail = sc.nextLine();
                    if (usuarios.containsKey(mail)) {
                        ConsejoSombrio consejo = ConsejoSombrio.getInstancia();
                        consejo.agregarMiembro(usuarios.get(mail));
                    } else {
                        System.out.println("Usuario no encontrado intente de nuevo");
                    }
                }
                case "2"->{
                    try{
                        System.out.println("A continuación se listan los usuarios");
                        mostrarUsuarios();
                        System.out.println("Escriba el mail del usuario que desea eliminar");
                        String mail = sc.nextLine();
                        if (usuarios.containsKey(mail)) {
                            ConsejoSombrio consejo = ConsejoSombrio.getInstancia();
                            consejo.eliminarMiembro(usuarios.get(mail));
                        } else {
                            throw new UsuarioNoEncontradoException("\n❌Usuario no encontrado intente de nuevo\n");
                        }
                    } catch (UsuarioNoEncontradoException e){
                        System.out.println(e.getMessage());
                    }

                }
                case "3"->{
                    ConsejoSombrio consejoSombrio = ConsejoSombrio.getInstancia();
                    System.out.println(consejoSombrio.getMiembros());
                }
                default -> {System.out.println("Saliendo....");seguir=false;}
            }
        }
    }
    public static void menuRegistroEsclavos(){
        System.out.println("Bienvenida al registro de esclavos");
        System.out.println("Seleccione una opción");
        System.out.println("1. Ver esclavos registrados");
        System.out.println("2. Añadir esclavo");
        System.out.println("3. Transferir esclavo");
        System.out.println("Presione cualquier otra tecla para salir");
        switch (sc.nextLine()) {
            case "1" ->{
                System.out.println(registro.getTrabajadores());
            }
            case "2" ->{
                System.out.println("Añadir nuevo esclavo");
                System.out.println("Proporcione la siguiente información");
                System.out.println("1. Nombre");
                String nombre = leerString();
                System.out.println("2. PaisOrigen");
                String paisOrigen = leerString();
                System.out.println("3. Edad");
                int edad = sc.nextInt();
                if (edad < 0){
                    while (edad < 0 || edad > 100){
                        System.out.println("Vuelva a intentar, no se aceptan edades fuera del rango [1-100]");
                        edad = sc.nextInt();
                    }
                }
                sc.nextLine();
                System.out.println("4. Fecha de Captura");
                String fechaCaptura = leerString();
                System.out.println("5. Nivel de Salud");
                System.out.println("Seleccione una de las siguientes opciones escribiendo la primera letra");
                System.out.println("E. ESTABLE (Por defecto con cualquier tecla)");
                System.out.println("P. PRECARIA");
                System.out.println("S. SALUDABLE");
                NivelSalud nivelSalud = NivelSalud.ESTABLE;
                switch (sc.nextLine()) {
                    case "P" ->{
                        nivelSalud = NivelSalud.PRECARIA;
                    }
                    case "S"->{
                        nivelSalud = NivelSalud.SAlUDABLE;
                    }
                    default -> {System.out.println("Salud estable por defecto");}
                }
                System.out.println("6. Fábrica");
                mostrarFabricas();
                System.out.println("Seleccione el id de la fábrica");
                long id = pedirLong();
                Fabrica fabrica = null;
                if (fabricas.containsKey(id)){
                    fabrica = fabricas.get(id);
                } else {
                    System.out.println("No se encontró la fábrica (es null por defecto)");
                }

                TrabajadorEsclavizado t = new TrabajadorEsclavizado(nombre,paisOrigen,edad,fechaCaptura,nivelSalud,fabrica);
                trabajadores.put(t.getId(),t);
            }
            case "3" ->{
                System.out.println("Interfaz de transferencia de esclavos");
                System.out.println("A continuación se muestran los trabajadores");
                mostrarTrabajadores();
                System.out.println("Escriba el id del trabajador que desea transferir");
                long id = pedirLong();
                if (trabajadores.containsKey(id)){
                    TrabajadorEsclavizado trabajador = trabajadores.get(id);
                    Fabrica fabricaAntigua = trabajador.getAsignadoA();
                    System.out.println("Se muestran las fábricas");
                    mostrarFabricas();
                    System.out.println("Seleccione el id de la fábrica a la que lo desea transferir");
                    long idFabricaNueva = pedirLong();
                    if (fabricas.containsKey(idFabricaNueva)){
                        trabajador.reasignarFabrica(fabricas.get(idFabricaNueva));
                    } else {
                        System.out.println("Fábrica no encontrada, intente de nuevo");
                    }
                } else {
                    System.out.println("Trabajador no encontrado");
                }
            }
            default -> {System.out.println("Saliendo...");}
        }
    }

    //Mostrar
    public static void mostrarProductos(){
        if (productos.isEmpty()){
            System.out.println("❌\nNo hay productos para mostrar\n");
        }else {
            for(Producto p: productos.values()){
                System.out.println("ID: "+p.getId());
                System.out.println("Nombre: "+p.getNombre());
                System.out.println("Descripcion: "+p.getDescripcion());
                System.out.println("Precio: "+p.getPrecio());
            }
        }

    }
    public static void mostrarCategorias(){
        if (categorias.isEmpty()){
            System.out.println("No hay categorias para mostrar");
        }else {
            for (Categoria c: categorias.values()){
                System.out.println(c);
            }
        }
    }
    public static void mostrarUsuarios(){
        if (usuarios.isEmpty()){
            System.out.println("No hay usuarios para mostrar");
        } else {
            for(Usuario u: usuarios.values()){
                System.out.println("Email: "+u.getEmail()+" Nombre: "+u.getNombre()+" Rol: "+u.getRol());
            }
        }
    }
    public static void mostrarFabricas(){
        if (fabricas.isEmpty()){
            System.out.println("No hay fabricas para mostrar");
        }else {
            for(Fabrica f: fabricas.values()){
                System.out.println(f);
            }
        }
    }
    public static void mostrarTrabajadores(){
        if(trabajadores.isEmpty()){
            System.out.println("No hay trabajadores para mostrar");
        }else {
            for (TrabajadorEsclavizado t: trabajadores.values()){
                System.out.println("ID: "+t.getId()+" Nombre: "+t.getNombre());
            }
        }
    }

    //Pedir con manejo de errores
    public static long pedirLong(){
        boolean seguir = true;
        long l = 0L;
        while(seguir){
            try {
                l = Long.parseLong(sc.nextLine());
                seguir = false;
            } catch (NumberFormatException e){
                System.out.println("Por favor escriba un número válido");
            }
        }
        return l;
    }
    public static double pedirDouble(){
        boolean seguir = true;
        double d = 0;
        while(seguir){
            try {
                d = Double.parseDouble(sc.nextLine());
                seguir = false;
            } catch (NumberFormatException e){
                System.out.println("Por favor escriba un número válido");
            }
        }
        return d;
    }

    public static String leerString(){
        String s = sc.nextLine();
        if(s.isEmpty()){
            System.out.println("El campo no puede estar vacio");
            while (s.isEmpty()){
                System.out.println("Por favor vuelva a intentar");
                s = sc.nextLine();
            }
        }
        return s;
    }

    // Inicializar datos de ejemplo
//    public static void inicializarDatosEjemplo() {
//        // Añadir dueña si no existe
//        if (!usuarios.containsKey("sakura@glow.com")) {
//            usuarios.put("sakura@glow.com",
//                    new Duena("Sakura",
//                            "sakura@glow.com",
//                            "ClaveMaestra",
//                            RolUsuario.DUENA,
//                            EstadoCuenta.ACTIVA,
//                            "Calle 11230",
//                            "777777")
//            );
//        }
//
//        // Añadir clientes de ejemplo si no existen
//        if (!usuarios.containsKey("juanpa@glow.com")) {
//            Cliente juanpa = new Cliente("Juanpa",
//                    "juanpa@glow.com",
//                    "1234",
//                    RolUsuario.CLIENTE,
//                    EstadoCuenta.ACTIVA,
//                    "Calle 10",
//                    "3102323");
//            usuarios.put("juanpa@glow.com", juanpa);
//
//            // Añadir método de pago de ejemplo
//            MetodoDePago mp1 = new MetodoDePago(TipoMetodoDePago.TARJETA, "Juanpa", "****1234", 50000.0);
//            juanpa.anadirMetodoDePago(mp1.getId(), mp1);
//        }
//
//        if (!usuarios.containsKey("maria@glow.com")) {
//            Cliente maria = new Cliente("Maria",
//                    "maria@glow.com",
//                    "maria123",
//                    RolUsuario.CLIENTE,
//                    EstadoCuenta.ACTIVA,
//                    "Avenida 5 #20-30",
//                    "3001234567");
//            usuarios.put("maria@glow.com", maria);
//
//            MetodoDePago mp2 = new MetodoDePago(TipoMetodoDePago.EFECTIVO, "Maria", "CASH001", 25000.0);
//            maria.anadirMetodoDePago(mp2.getId(), mp2);
//        }
//
//        if (!usuarios.containsKey("carlos@glow.com")) {
//            Cliente carlos = new Cliente("Carlos",
//                    "carlos@glow.com",
//                    "carlos456",
//                    RolUsuario.CLIENTE,
//                    EstadoCuenta.ACTIVA,
//                    "Carrera 15 #45-12",
//                    "3209876543");
//            usuarios.put("carlos@glow.com", carlos);
//
//            MetodoDePago mp3 = new MetodoDePago(TipoMetodoDePago.TRANSFERENCIA, "Carlos", "TRANS789", 75000.0);
//            carlos.anadirMetodoDePago(mp3.getId(), mp3);
//        }
//
//        // Añadir categorías de ejemplo si no existen
//        Categoria categoriaCremas = null;
//        Categoria categoriaMaquillaje = null;
//        Categoria categoriaCuidado = null;
//
//        if (categorias.isEmpty()) {
//            categoriaCremas = new Categoria("Cremas", "Productos de cremas faciales y corporales");
//            if (categoriaCremas.getProductos() == null) {
//                categoriaCremas.setProductos(new ArrayList<>());
//            }
//            categorias.put(categoriaCremas.getId(), categoriaCremas);
//
//            categoriaMaquillaje = new Categoria("Maquillaje", "Productos de maquillaje y cosméticos");
//            if (categoriaMaquillaje.getProductos() == null) {
//                categoriaMaquillaje.setProductos(new ArrayList<>());
//            }
//            categorias.put(categoriaMaquillaje.getId(), categoriaMaquillaje);
//
//            categoriaCuidado = new Categoria("Cuidado Personal", "Productos para el cuidado personal");
//            if (categoriaCuidado.getProductos() == null) {
//                categoriaCuidado.setProductos(new ArrayList<>());
//            }
//            categorias.put(categoriaCuidado.getId(), categoriaCuidado);
//        } else {
//            // Buscar categorías existentes
//            for (Categoria cat : categorias.values()) {
//                if (cat.getNombre().equals("Cremas")) {
//                    categoriaCremas = cat;
//                    if (categoriaCremas.getProductos() == null) {
//                        categoriaCremas.setProductos(new ArrayList<>());
//                    }
//                } else if (cat.getNombre().equals("Maquillaje")) {
//                    categoriaMaquillaje = cat;
//                    if (categoriaMaquillaje.getProductos() == null) {
//                        categoriaMaquillaje.setProductos(new ArrayList<>());
//                    }
//                } else if (cat.getNombre().equals("Cuidado Personal")) {
//                    categoriaCuidado = cat;
//                    if (categoriaCuidado.getProductos() == null) {
//                        categoriaCuidado.setProductos(new ArrayList<>());
//                    }
//                }
//            }
//        }
//
//        // Añadir productos de ejemplo si no existen
//        if (productos.isEmpty()) {
//            Producto producto1 = new Producto("Crema Hidratante",
//                    "Crema hidratante facial con vitamina E", 223.3, 100L, categoriaCremas);
//            productos.put(producto1.getId(), producto1);
//
//            Producto producto2 = new Producto("Base de Maquillaje",
//                    "Base de larga duración con acabado natural", 45000.0, 50L, categoriaMaquillaje);
//            productos.put(producto2.getId(), producto2);
//
//            Producto producto3 = new Producto("Labial Mate",
//                    "Labial de larga duración en varios tonos", 25000.0, 75L, categoriaMaquillaje);
//            productos.put(producto3.getId(), producto3);
//
//            Producto producto4 = new Producto("Crema Anti-Edad",
//                    "Crema anti-edad con colágeno y ácido hialurónico", 85000.0, 30L, categoriaCremas);
//            productos.put(producto4.getId(), producto4);
//
//            Producto producto5 = new Producto("Protector Solar",
//                    "Protector solar SPF 50+ para rostro y cuerpo", 35000.0, 60L, categoriaCuidado);
//            productos.put(producto5.getId(), producto5);
//
//            Producto producto6 = new Producto("Mascarilla Facial",
//                    "Mascarilla facial purificante con arcilla", 28000.0, 40L, categoriaCuidado);
//            productos.put(producto6.getId(), producto6);
//        }
//
//        // Añadir administradores de ejemplo si no existen
//        if (!usuarios.containsKey("admin@glow.com")) {
//            AdministradorContenido adminContenido = new AdministradorContenido(
//                    "Admin Contenido",
//                    "admin@glow.com",
//                    "admin123",
//                    RolUsuario.ADMINISTRADOR_CONTENIDO,
//                    EstadoCuenta.ACTIVA,
//                    PermisosEdicion.TOTALES);
//            usuarios.put("admin@glow.com", adminContenido);
//        }
//
//        if (!usuarios.containsKey("adminuser@glow.com")) {
//            AdministradorUsuario adminUsuario = new AdministradorUsuario(
//                    "Admin Usuario",
//                    "adminuser@glow.com",
//                    "admin456",
//                    RolUsuario.ADMINISTRADOR_USUARIO,
//                    EstadoCuenta.ACTIVA,
//                    NivelAcceso.TOTAL);
//            usuarios.put("adminuser@glow.com", adminUsuario);
//        }
//
//        if (!usuarios.containsKey("desarrollador@glow.com")) {
//            DesarrolladorProducto desarrollador = new DesarrolladorProducto();
//            desarrollador.setNombre("Desarrollador");
//            desarrollador.setEmail("desarrollador@glow.com");
//            desarrollador.setPasswordHash("dev123");
//            desarrollador.setRol(RolUsuario.DESARROLLADOR);
//            desarrollador.setEstadoCuenta(EstadoCuenta.ACTIVA);
//            desarrollador.especialidad = "Desarrollo de productos cosméticos";
//            usuarios.put("desarrollador@glow.com", desarrollador);
//        }
//    }

    // Métodos de persistencia con Serializable
    @SuppressWarnings("unchecked")
    public static void cargarDatos() {
        try {
            // Cargar usuarios
            File archivoUsuarios = new File(ARCHIVO_USUARIOS);
            if (archivoUsuarios.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoUsuarios))) {
                    Map<String, Usuario> usuariosCargados = (Map<String, Usuario>) ois.readObject();
                    usuarios.putAll(usuariosCargados);
                }
            }
            
            // Cargar productos
            File archivoProductos = new File(ARCHIVO_PRODUCTOS);
            if (archivoProductos.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoProductos))) {
                    Map<Long, Producto> productosCargados = (Map<Long, Producto>) ois.readObject();
                    productos.putAll(productosCargados);
                }
            }
            
            // Cargar categorías
            File archivoCategorias = new File(ARCHIVO_CATEGORIAS);
            if (archivoCategorias.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoCategorias))) {
                    Map<Long, Categoria> categoriasCargadas = (Map<Long, Categoria>) ois.readObject();
                    categorias.putAll(categoriasCargadas);
                }
            }
            
            // Cargar fábricas
            File archivoFabricas = new File(ARCHIVO_FABRICAS);
            if (archivoFabricas.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoFabricas))) {
                    Map<Long, Fabrica> fabricasCargadas = (Map<Long, Fabrica>) ois.readObject();
                    fabricas.putAll(fabricasCargadas);
                }
            }
            
            // Cargar trabajadores
            File archivoTrabajadores = new File(ARCHIVO_TRABAJADORES);
            if (archivoTrabajadores.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoTrabajadores))) {
                    Map<Long, TrabajadorEsclavizado> trabajadoresCargados = (Map<Long, TrabajadorEsclavizado>) ois.readObject();
                    trabajadores.putAll(trabajadoresCargados);
                }
            }
            
            // Cargar registro de esclavos
            File archivoRegistro = new File(ARCHIVO_REGISTRO);
            if (archivoRegistro.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoRegistro))) {
                    ois.readObject(); // El registro es singleton, se carga automáticamente con los trabajadores
                }
            }
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
            // Si hay error, continuamos con datos vacíos
        }
    }
    
    public static void guardarDatos() {
        try {
            // Guardar usuarios
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
                oos.writeObject(usuarios);
            }
            
            // Guardar productos
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PRODUCTOS))) {
                oos.writeObject(productos);
            }
            
            // Guardar categorías
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_CATEGORIAS))) {
                oos.writeObject(categorias);
            }
            
            // Guardar fábricas
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_FABRICAS))) {
                oos.writeObject(fabricas);
            }
            
            // Guardar trabajadores
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_TRABAJADORES))) {
                oos.writeObject(trabajadores);
            }
            
            // Guardar registro de esclavos
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_REGISTRO))) {
                oos.writeObject(registro);
            }
            
        } catch (IOException e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }

}
