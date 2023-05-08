package com.hts.gestor.contenido.services;

import com.hts.gestor.contenido.dto.RespuestaGenerica;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface GestorBucketService {
	
	RespuestaGenerica createBucket(String bucketName);
	
	RespuestaGenerica listBucket();
	
	RespuestaGenerica deleteBucket(String bucketName);
	RespuestaGenerica deleteFile(String bucketName, String objectName );
	
	RespuestaGenerica uploadFile(String bucketName, String fileName, MultipartFile file, boolean publicFile) throws IOException;
	public List<String> listFiles(String bucketName);

	byte[] downloadFile(String bucketName, String fileName) throws IOException;
}
