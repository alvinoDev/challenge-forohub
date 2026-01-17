package alvino.dev.challenge_forohub.domain.topico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    public DatosRespuestaTopico create(DatosRegistroTopico datos) {
        if( topicoRepository.existsByTitulo(datos.titulo()) ){
            throw new RuntimeException("Titulo duplicado");
        }

        Topico topico = new Topico(datos);
        topicoRepository.save(topico);
        return new DatosRespuestaTopico(topico);
    }
}
