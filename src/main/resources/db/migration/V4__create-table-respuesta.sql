CREATE TABLE respuesta (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    mensaje TEXT NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    solucion BOOLEAN DEFAULT FALSE,
    autor_id BIGINT NOT NULL,
    topico_id BIGINT NOT NULL,

    CONSTRAINT fk_respuesta_usuario_id FOREIGN KEY (autor_id) REFERENCES usuario(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_respuesta_topico_id FOREIGN KEY (topico_id) REFERENCES topico(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);