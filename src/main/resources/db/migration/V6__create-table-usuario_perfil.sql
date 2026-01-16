CREATE TABLE usuario_perfil (
    usuario_id BIGINT NOT NULL,
    perfil_id BIGINT NOT NULL,

    -- Definición de la Llave Primaria Compuesta
    PRIMARY KEY (usuario_id, perfil_id),

    CONSTRAINT fk_usuario_perfil_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_usuario_perfil_perfil_id FOREIGN KEY (perfil_id) REFERENCES perfil(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);