package cl.sonidovivo.publicacionesservice.config;

import cl.sonidovivo.publicacionesservice.model.Publicacion;
import cl.sonidovivo.publicacionesservice.repository.PublicacionRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Publicaciones de ejemplo para mostrar el blog.
@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner publicacionesIniciales(PublicacionRepository repo) {
        return args -> {
            if (repo.count() > 0) {
                return;
            }
            crear(repo, "Cómo cuidar las cuerdas de tu guitarra",
                    "Consejos simples para mantener las cuerdas limpias y prolongar su duración.",
                    "Después de tocar, limpia las cuerdas con un paño seco para retirar humedad y residuos. También es importante guardar el instrumento correctamente y reemplazar las cuerdas cuando pierdan brillo o afinación.",
                    LocalDate.of(2026, 10, 2));
            crear(repo, "Cómo elegir tu primer micrófono",
                    "Revisamos algunos aspectos básicos para escoger un micrófono según su uso.",
                    "Antes de comprar un micrófono es importante definir si se utilizará para presentaciones, ensayos o grabación. También se debe considerar el tipo de conexión y el entorno donde será utilizado.",
                    LocalDate.of(2026, 10, 6));
            crear(repo, "Cuándo revisar tu instrumento",
                    "Algunas señales que indican que tu guitarra o bajo necesita mantenimiento.",
                    "Problemas de afinación, ruidos extraños, cuerdas demasiado altas o dificultad para tocar pueden indicar que el instrumento necesita una revisión técnica.",
                    LocalDate.of(2026, 10, 9));
        };
    }

    private void crear(PublicacionRepository repo, String titulo, String resumen,
            String contenido, LocalDate fecha) {
        Publicacion p = new Publicacion();
        p.setTitulo(titulo);
        p.setResumen(resumen);
        p.setContenido(contenido);
        p.setFecha(fecha);
        p.setAutor("Sonido Vivo");
        repo.save(p);
    }
}
