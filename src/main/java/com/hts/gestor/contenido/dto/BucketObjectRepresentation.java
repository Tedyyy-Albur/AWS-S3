package com.hts.gestor.contenido.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BucketObjectRepresentation {
	private String objectName;
	private MultipartFile stream;
}
