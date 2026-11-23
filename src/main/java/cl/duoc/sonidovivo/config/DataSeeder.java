package cl.duoc.sonidovivo.config;

import cl.duoc.sonidovivo.model.*;
import cl.duoc.sonidovivo.repository.CategoriaRepository;
import cl.duoc.sonidovivo.repository.ProductoRepository;
import cl.duoc.sonidovivo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(Usuario.builder()
                    .nombre("Administrador")
                    .apellido("Sonido Vivo")
                    .fechaNacimiento(LocalDate.of(1990, 1, 1))
                    .correo("admin@sonidovivo.cl")
                    .password(passwordEncoder.encode("Admin1234"))
                    .direccion("Av. Valparaíso 123, Viña del Mar")
                    .region("Valparaíso")
                    .genero("Otro")
                    .rol(Rol.ADMINISTRADOR)
                    .activo(true)
                    .build());

            usuarioRepository.save(Usuario.builder()
                    .nombre("Carlos")
                    .apellido("Vendedor")
                    .fechaNacimiento(LocalDate.of(1995, 5, 12))
                    .correo("vendedor@sonidovivo.cl")
                    .password(passwordEncoder.encode("Vendedor1234"))
                    .direccion("Calle Álvarez 456, Viña del Mar")
                    .region("Valparaíso")
                    .genero("Masculino")
                    .rol(Rol.VENDEDOR)
                    .activo(true)
                    .build());

            usuarioRepository.save(Usuario.builder()
                    .nombre("Pedro")
                    .apellido("Músico")
                    .fechaNacimiento(LocalDate.of(2000, 8, 20))
                    .correo("cliente@sonidovivo.cl")
                    .password(passwordEncoder.encode("Cliente1234"))
                    .direccion("Los Crisantemos 789, Viña del Mar")
                    .region("Valparaíso")
                    .genero("Masculino")
                    .rol(Rol.CLIENTE)
                    .activo(true)
                    .build());
        }

        if (categoriaRepository.count() == 0 && productoRepository.count() == 0) {
            Map<String, Categoria> categorias = new HashMap<>();
        categorias.put("Guitarras Acústicas", categoriaRepository.save(Categoria.builder().nombre("Guitarras Acústicas").descripcion("Folk, Dreadnought, Clásica, Electroacústica, Niños").build()));
        categorias.put("Guitarras Eléctricas", categoriaRepository.save(Categoria.builder().nombre("Guitarras Eléctricas").descripcion("Stratocaster, Telecaster, Les Paul, SG, Semi-hollow, Signature").build()));
        categorias.put("Bajos Eléctricos", categoriaRepository.save(Categoria.builder().nombre("Bajos Eléctricos").descripcion("Precision, Jazz, Activo, Acústico, Fretless").build()));
        categorias.put("Baterías", categoriaRepository.save(Categoria.builder().nombre("Baterías").descripcion("Acústica, Electrónica, Cajas, Platillos, Stands").build()));
        categorias.put("Teclados y Pianos", categoriaRepository.save(Categoria.builder().nombre("Teclados y Pianos").descripcion("Digital, Sintetizador, MIDI Controller, Órgano").build()));
        categorias.put("Amplificadores", categoriaRepository.save(Categoria.builder().nombre("Amplificadores").descripcion("Guitarra combo, Guitarra cabezal, Bajo, Acústico, Teclado").build()));
        categorias.put("Micrófonos", categoriaRepository.save(Categoria.builder().nombre("Micrófonos").descripcion("Dinámico, Condensador, USB, Ribbon, Lavalier").build()));
        categorias.put("Pedales de Efectos", categoriaRepository.save(Categoria.builder().nombre("Pedales de Efectos").descripcion("Distorsión, Overdrive, Fuzz, Reverb, Delay, Chorus, Multi-FX").build()));
        categorias.put("Accesorios", categoriaRepository.save(Categoria.builder().nombre("Accesorios").descripcion("Cuerdas, Púas, Soportes, Cables, Correas, Afinadores").build()));
        categorias.put("Estudio y Grabación", categoriaRepository.save(Categoria.builder().nombre("Estudio y Grabación").descripcion("Interfaces, Auriculares, Monitores, Pop filters, DAW").build()));

        productoRepository.save(Producto.builder().id("GA001").nombre("Guitarra Acústica Folk").marca("Yamaha").modelo("F310").categoria(categorias.get("Guitarras Acústicas")).precio(129990).precioOferta(117000).stock(8).imagen("https://images.unsplash.com/photo-1510915361894-db8b60106cb1?w=600").descripcion("Tapa de abeto, aros y fondo de meranti. Ideal para iniciantes.").build());
        productoRepository.save(Producto.builder().id("GA002").nombre("Guitarra Acústica Dreadnought").marca("Fender").modelo("CD-60S").categoria(categorias.get("Guitarras Acústicas")).precio(189990).precioOferta(null).stock(5).imagen("https://images.unsplash.com/photo-1510915361894-db8b60106cb1?w=600").descripcion("Tapa de abeto macizo, brazo de caoba. Sonido cálido y proyectado.").build());
        productoRepository.save(Producto.builder().id("GA003").nombre("Guitarra Acústica Clásica 4/4").marca("Yamaha").modelo("C40").categoria(categorias.get("Guitarras Acústicas")).precio(89990).precioOferta(null).stock(10).imagen("https://images.unsplash.com/photo-1510915361894-db8b60106cb1?w=600").descripcion("Nailon, tapa de abeto. Ideal para estudio y flamenco.").build());
        productoRepository.save(Producto.builder().id("GA004").nombre("Guitarra Electroacústica").marca("Takamine").modelo("GN20CE").categoria(categorias.get("Guitarras Acústicas")).precio(349990).precioOferta(null).stock(3).imagen("https://images.unsplash.com/photo-1510915361894-db8b60106cb1?w=600").descripcion("Pickup integrado, afinador incorporado.").build());
        productoRepository.save(Producto.builder().id("GA005").nombre("Guitarra 3/4 Niños").marca("Yamaha").modelo("JR1").categoria(categorias.get("Guitarras Acústicas")).precio(79990).precioOferta(null).stock(6).imagen("https://images.unsplash.com/photo-1510915361894-db8b60106cb1?w=600").descripcion("Tamaño reducido para niños de 6 a 10 años.").build());
        productoRepository.save(Producto.builder().id("GE001").nombre("Guitarra Eléctrica Stratocaster").marca("Squier").modelo("Affinity Strat").categoria(categorias.get("Guitarras Eléctricas")).precio(249990).precioOferta(225000).stock(5).imagen("https://images.unsplash.com/photo-1550985616-10810253b84d?w=600").descripcion("Cuerpo de álamo, mástil de arce, pastillas SSS.").build());
        productoRepository.save(Producto.builder().id("GE002").nombre("Guitarra Eléctrica Les Paul").marca("Epiphone").modelo("Les Paul Std").categoria(categorias.get("Guitarras Eléctricas")).precio(329990).precioOferta(null).stock(4).imagen("https://images.unsplash.com/photo-1550985616-10810253b84d?w=600").descripcion("Cuerpo caoba, tapa arce, pastillas humbucker.").build());
        productoRepository.save(Producto.builder().id("GE003").nombre("Guitarra Eléctrica SG").marca("Epiphone").modelo("SG Standard").categoria(categorias.get("Guitarras Eléctricas")).precio(319990).precioOferta(null).stock(3).imagen("https://images.unsplash.com/photo-1550985616-10810253b84d?w=600").descripcion("Cuerpo caoba, mástil caoba, 2 humbuckers.").build());
        productoRepository.save(Producto.builder().id("GE004").nombre("Guitarra Eléctrica Telecaster").marca("Squier").modelo("Affinity Tele").categoria(categorias.get("Guitarras Eléctricas")).precio(239990).precioOferta(null).stock(4).imagen("https://images.unsplash.com/photo-1550985616-10810253b84d?w=600").descripcion("Cuerpo álamo, clavijero vintage, 2 pastillas single.").build());
        productoRepository.save(Producto.builder().id("GE005").nombre("Guitarra Eléctrica Semi-hollow").marca("Epiphone").modelo("ES-335").categoria(categorias.get("Guitarras Eléctricas")).precio(549990).precioOferta(null).stock(2).imagen("https://images.unsplash.com/photo-1550985616-10810253b84d?w=600").descripcion("Semi-hueca, 2 humbuckers, ideal para jazz y blues.").build());
        productoRepository.save(Producto.builder().id("BA001").nombre("Bajo Eléctrico 4 Cuerdas").marca("Squier").modelo("Affinity PJ").categoria(categorias.get("Bajos Eléctricos")).precio(299990).precioOferta(270000).stock(5).imagen("https://images.unsplash.com/photo-1564186763535-ebb21ef5277f?w=600").descripcion("Pickup PJ, cuerpo álamo, mástil arce.").build());
        productoRepository.save(Producto.builder().id("BA002").nombre("Bajo Eléctrico Jazz Bass").marca("Fender").modelo("Player Jazz").categoria(categorias.get("Bajos Eléctricos")).precio(699990).precioOferta(null).stock(2).imagen("https://images.unsplash.com/photo-1564186763535-ebb21ef5277f?w=600").descripcion("Alder body, 2 Alnico V Jazz single-coil.").build());
        productoRepository.save(Producto.builder().id("BA003").nombre("Bajo Acústico 4 Cuerdas").marca("Yamaha").modelo("APX700II").categoria(categorias.get("Bajos Eléctricos")).precio(429990).precioOferta(null).stock(2).imagen("https://images.unsplash.com/photo-1564186763535-ebb21ef5277f?w=600").descripcion("Electroacústico, afinador incorporado.").build());
        productoRepository.save(Producto.builder().id("BT001").nombre("Batería Acústica 5 piezas").marca("Pearl").modelo("Roadshow").categoria(categorias.get("Baterías")).precio(599990).precioOferta(null).stock(2).imagen("https://images.unsplash.com/photo-1519892300165-280b920e2c01?w=600").descripcion("Incluye stands, platillos y pedal de bombo.").build());
        productoRepository.save(Producto.builder().id("BT002").nombre("Batería Electrónica 8 pads").marca("Roland").modelo("TD-02KV").categoria(categorias.get("Baterías")).precio(799990).precioOferta(null).stock(2).imagen("https://images.unsplash.com/photo-1519892300165-280b920e2c01?w=600").descripcion("Módulo TD-02, 8 pads de goma, pedal hi-hat.").build());
        productoRepository.save(Producto.builder().id("BT003").nombre("Caja Snare 14\"").marca("Pearl").modelo("STE1450").categoria(categorias.get("Baterías")).precio(89990).precioOferta(81000).stock(4).imagen("https://images.unsplash.com/photo-1519892300165-280b920e2c01?w=600").descripcion("Acero, 14x5\", 10 tensores.").build());
        productoRepository.save(Producto.builder().id("BT004").nombre("Platillo Hi-Hat 14\"").marca("Zildjian").modelo("A Series").categoria(categorias.get("Baterías")).precio(149990).precioOferta(null).stock(3).imagen("https://images.unsplash.com/photo-1519892300165-280b920e2c01?w=600").descripcion("Latón B20, sonido brillante y claro.").build());
        productoRepository.save(Producto.builder().id("BT005").nombre("Platillo Crash 16\"").marca("Zildjian").modelo("A Series").categoria(categorias.get("Baterías")).precio(129990).precioOferta(null).stock(3).imagen("https://images.unsplash.com/photo-1519892300165-280b920e2c01?w=600").descripcion("Latón B20, ataque rápido.").build());
        productoRepository.save(Producto.builder().id("TC001").nombre("Teclado Digital 61 teclas").marca("Yamaha").modelo("PSR-E373").categoria(categorias.get("Teclados y Pianos")).precio(249990).precioOferta(null).stock(4).imagen("https://images.unsplash.com/photo-1520523839897-bd0b52f945a0?w=600").descripcion("61 teclas sensibles al tacto, 622 voces.").build());
        productoRepository.save(Producto.builder().id("TC002").nombre("Piano Digital 88 teclas").marca("Yamaha").modelo("P-45").categoria(categorias.get("Teclados y Pianos")).precio(499990).precioOferta(null).stock(2).imagen("https://images.unsplash.com/photo-1520523839897-bd0b52f945a0?w=600").descripcion("88 teclas pesadas, 10 voces, pedal sustain incluido.").build());
        productoRepository.save(Producto.builder().id("TC003").nombre("Sintetizador 49 teclas").marca("Arturia").modelo("MiniLab MKII").categoria(categorias.get("Teclados y Pianos")).precio(129990).precioOferta(117000).stock(5).imagen("https://images.unsplash.com/photo-1520523839897-bd0b52f945a0?w=600").descripcion("MIDI controller, 49 mini teclas.").build());
        productoRepository.save(Producto.builder().id("TC004").nombre("Teclado MIDI 88 teclas").marca("M-Audio").modelo("Hammer 88").categoria(categorias.get("Teclados y Pianos")).precio(399990).precioOferta(null).stock(2).imagen("https://images.unsplash.com/photo-1520523839897-bd0b52f945a0?w=600").descripcion("88 teclas martillo, sin sonidos propios.").build());
        productoRepository.save(Producto.builder().id("AM001").nombre("Amplificador Guitarra 15W").marca("Fender").modelo("Frontman 15G").categoria(categorias.get("Amplificadores")).precio(99990).precioOferta(null).stock(5).imagen("https://images.unsplash.com/photo-1516924962513-888b0b549560?w=600").descripcion("15W, distorsión incorporada, entrada auxiliar.").build());
        productoRepository.save(Producto.builder().id("AM002").nombre("Amplificador Guitarra 40W").marca("Marshall").modelo("MG40GFX").categoria(categorias.get("Amplificadores")).precio(299990).precioOferta(null).stock(3).imagen("https://images.unsplash.com/photo-1516924962513-888b0b549560?w=600").descripcion("40W, 4 canales, efectos digitales integrados.").build());
        productoRepository.save(Producto.builder().id("AM003").nombre("Amplificador Bajo 100W").marca("Hartke").modelo("HD100").categoria(categorias.get("Amplificadores")).precio(449990).precioOferta(null).stock(2).imagen("https://images.unsplash.com/photo-1516924962513-888b0b549560?w=600").descripcion("100W, tweeter integrado, ecualizador de 4 bandas.").build());
        productoRepository.save(Producto.builder().id("AM004").nombre("Amplificador Acústico 40W").marca("Fishman").modelo("Loudbox Mini").categoria(categorias.get("Amplificadores")).precio(499990).precioOferta(450000).stock(2).imagen("https://images.unsplash.com/photo-1516924962513-888b0b549560?w=600").descripcion("60W, 2 canales, reverb y chorus incorporados.").build());
        productoRepository.save(Producto.builder().id("MI001").nombre("Micrófono Dinámico Cardioide").marca("Shure").modelo("SM58").categoria(categorias.get("Micrófonos")).precio(149990).precioOferta(null).stock(8).imagen("https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=600").descripcion("Estándar industria para voz en vivo.").build());
        productoRepository.save(Producto.builder().id("MI002").nombre("Micrófono Dinámico Instrumento").marca("Shure").modelo("SM57").categoria(categorias.get("Micrófonos")).precio(139990).precioOferta(null).stock(6).imagen("https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=600").descripcion("Ideal para captura de instrumentos y amplificadores.").build());
        productoRepository.save(Producto.builder().id("MI003").nombre("Micrófono Condensador").marca("Audio-Tech.").modelo("AT2020").categoria(categorias.get("Micrófonos")).precio(199990).precioOferta(null).stock(4).imagen("https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=600").descripcion("Cardioide, XLR, ideal para grabación en estudio.").build());
        productoRepository.save(Producto.builder().id("MI004").nombre("Micrófono USB de Condensador").marca("Blue").modelo("Yeti").categoria(categorias.get("Micrófonos")).precio(299990).precioOferta(null).stock(5).imagen("https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=600").descripcion("USB, 4 patrones polares, ideal para streaming y podcast.").build());
        productoRepository.save(Producto.builder().id("PE001").nombre("Pedal Distorsión").marca("Boss").modelo("DS-1").categoria(categorias.get("Pedales de Efectos")).precio(79990).precioOferta(72000).stock(7).imagen("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600").descripcion("Clásico pedal de distorsión, 3 controles.").build());
        productoRepository.save(Producto.builder().id("PE002").nombre("Pedal Reverb").marca("Boss").modelo("RV-6").categoria(categorias.get("Pedales de Efectos")).precio(179990).precioOferta(null).stock(4).imagen("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600").descripcion("8 modos de reverb, control de shimmer.").build());
        productoRepository.save(Producto.builder().id("PE003").nombre("Pedal Multi-efectos").marca("Boss").modelo("ME-80").categoria(categorias.get("Pedales de Efectos")).precio(349990).precioOferta(null).stock(2).imagen("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600").descripcion("Diseño tipo pedalboard, 8 efectos simultáneos.").build());
        productoRepository.save(Producto.builder().id("PE004").nombre("Pedal Tuner Cromático").marca("Boss").modelo("TU-3").categoria(categorias.get("Pedales de Efectos")).precio(89990).precioOferta(null).stock(8).imagen("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600").descripcion("Afinador cromático, indicador de tono.").build());
        productoRepository.save(Producto.builder().id("PE005").nombre("Pedal Delay").marca("MXR").modelo("Carbon Copy").categoria(categorias.get("Pedales de Efectos")).precio(179990).precioOferta(null).stock(4).imagen("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600").descripcion("Delay analógico cálido, tiempo 600ms.").build());
        productoRepository.save(Producto.builder().id("PE006").nombre("Pedal Overdrive").marca("Ibanez").modelo("TS9").categoria(categorias.get("Pedales de Efectos")).precio(99990).precioOferta(90000).stock(6).imagen("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600").descripcion("Tube Screamer clásico, sonido suave y orgánico.").build());
        productoRepository.save(Producto.builder().id("AC001").nombre("Cuerdas Guitarra Eléctrica 09-42").marca("Ernie Ball").modelo("Super Slinky").categoria(categorias.get("Accesorios")).precio(8990).precioOferta(null).stock(25).imagen("https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=600").descripcion("Juego 6 cuerdas, calibre ligero.").build());
        productoRepository.save(Producto.builder().id("AC002").nombre("Cuerdas Guitarra Acústica 12-53").marca("Ernie Ball").modelo("Earthwood").categoria(categorias.get("Accesorios")).precio(10990).precioOferta(null).stock(20).imagen("https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=600").descripcion("Bronce fósforo, sonido cálido.").build());
        productoRepository.save(Producto.builder().id("AC003").nombre("Cuerdas Bajo 45-105").marca("Ernie Ball").modelo("Regular Slinky").categoria(categorias.get("Accesorios")).precio(14990).precioOferta(null).stock(12).imagen("https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=600").descripcion("Cuerdas de níquel enrollado, set 4 cuerdas.").build());
        productoRepository.save(Producto.builder().id("AC004").nombre("Púas de Guitarra x10 (0.73mm)").marca("Fender").modelo("351").categoria(categorias.get("Accesorios")).precio(3990).precioOferta(null).stock(50).imagen("https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=600").descripcion("Celulosa, grosor medio.").build());
        productoRepository.save(Producto.builder().id("AC005").nombre("Capotraste Guitarra").marca("Dunlop").modelo("Trigger").categoria(categorias.get("Accesorios")).precio(12990).precioOferta(12000).stock(15).imagen("https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=600").descripcion("Capotraste de resorte, compatible 6 cuerdas.").build());
        productoRepository.save(Producto.builder().id("AC006").nombre("Afinador de Clip").marca("Snark").modelo("SN-5").categoria(categorias.get("Accesorios")).precio(8990).precioOferta(null).stock(20).imagen("https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=600").descripcion("Afinador cromático de clip, pantalla giratoria.").build());
        productoRepository.save(Producto.builder().id("AC007").nombre("Cable Instrumento 3m").marca("Monster").modelo("S100-I-3").categoria(categorias.get("Accesorios")).precio(12990).precioOferta(null).stock(15).imagen("https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=600").descripcion("Cable trenzado, conectores dorados, 3 metros.").build());
        productoRepository.save(Producto.builder().id("AC008").nombre("Cable Instrumento 6m").marca("Monster").modelo("S100-I-6").categoria(categorias.get("Accesorios")).precio(17990).precioOferta(null).stock(10).imagen("https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=600").descripcion("Cable trenzado, conectores dorados, 6 metros.").build());
        productoRepository.save(Producto.builder().id("AC009").nombre("Soporte Guitarra de Piso").marca("Hercules").modelo("GS302B").categoria(categorias.get("Accesorios")).precio(22990).precioOferta(null).stock(12).imagen("https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=600").descripcion("Soporte plegable con enganche automático.").build());
        productoRepository.save(Producto.builder().id("AC010").nombre("Soporte Guitarra de Pared").marca("Hercules").modelo("WAH-202").categoria(categorias.get("Accesorios")).precio(18990).precioOferta(17000).stock(10).imagen("https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=600").descripcion("Montaje a pared, enganche automático.").build());
        productoRepository.save(Producto.builder().id("ES001").nombre("Interfaz de Audio 2x2 USB").marca("Focusrite").modelo("Scarlett Solo").categoria(categorias.get("Estudio y Grabación")).precio(149990).precioOferta(null).stock(4).imagen("https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=600").descripcion("1 entrada XLR+instrumento, 2 salidas, 24bit/192kHz.").build());
        productoRepository.save(Producto.builder().id("ES002").nombre("Auriculares de Estudio").marca("Audio-Tech.").modelo("ATH-M20x").categoria(categorias.get("Estudio y Grabación")).precio(79990).precioOferta(null).stock(6).imagen("https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=600").descripcion("Circumaurales, respuesta 15Hz-20kHz.").build());
        productoRepository.save(Producto.builder().id("ES003").nombre("Auriculares de Estudio Pro").marca("Audio-Tech.").modelo("ATH-M50x").categoria(categorias.get("Estudio y Grabación")).precio(219990).precioOferta(null).stock(4).imagen("https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=600").descripcion("Referencia de industria, sonido neutro y detallado.").build());
        productoRepository.save(Producto.builder().id("ES004").nombre("Monitor de Estudio 5\"").marca("Yamaha").modelo("HS5").categoria(categorias.get("Estudio y Grabación")).precio(349990).precioOferta(null).stock(2).imagen("https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=600").descripcion("Altavoz activo, respuesta plana, ideal mezcla.").build());
        productoRepository.save(Producto.builder().id("ES005").nombre("Pop Filter para Micrófono").marca("Sennheiser").modelo("MZP 40").categoria(categorias.get("Estudio y Grabación")).precio(14990).precioOferta(13000).stock(8).imagen("https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=600").descripcion("Doble malla, brazo flexible con clip.").build());
        }
    }
}
