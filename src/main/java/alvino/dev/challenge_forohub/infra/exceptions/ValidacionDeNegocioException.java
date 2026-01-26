package alvino.dev.challenge_forohub.infra.exceptions;

public class ValidacionDeNegocioException extends RuntimeException {
    public ValidacionDeNegocioException(String mensaje) {
        super(mensaje);
    }
}
