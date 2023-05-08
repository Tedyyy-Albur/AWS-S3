package com.hts.gestor.contenido.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.amazonaws.services.rekognition.model.Asset;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import com.hts.gestor.contenido.dto.RespuestaGenerica;
import com.amazonaws.services.s3.model.Bucket;
import com.hts.gestor.contenido.dto.BucketObjectRepresentation;
import com.hts.gestor.contenido.dto.EstatusResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Service("gestorBucketService")
@RequiredArgsConstructor
public class GestorBucketServicesImpl implements GestorBucketService {
	
	private final S3Service s3Service;
	private final AmazonS3 amazonS3Client;

	@Override
	public RespuestaGenerica createBucket(String bucketName) {
	
		String result = s3Service.createS3Bucket(bucketName,true);
		
		return new RespuestaGenerica(EstatusResponse.OK.getEstatus(), "Creacion de Bucket",
				"resultado:", result);
	}

	@Override
	public RespuestaGenerica listBucket() {
		List<Bucket> buckets = s3Service.listBuckets();
		
		return new RespuestaGenerica(EstatusResponse.OK.getEstatus(), "Lista de Buckets",
				"listabuckets:", buckets);
	}

	@Override
	public RespuestaGenerica deleteBucket(String bucketName) {
		
		String result = s3Service.deleteBucket(bucketName);
		return new RespuestaGenerica(EstatusResponse.OK.getEstatus(), "Eliminacion de Bucket",
				"resultado:", result);
	}

	@Override
	public RespuestaGenerica deleteFile(String bucketName, String objectName) {
		String result = s3Service.deleteObject(bucketName, objectName);
		return new RespuestaGenerica(EstatusResponse.OK.getEstatus(), "Eliminacion de Archivo",
				"resultado:", result);
	}

	@Override
	public RespuestaGenerica uploadFile(String bucketName, String fileName, MultipartFile file, boolean publicFile) throws IOException {
		BucketObjectRepresentation representation = new BucketObjectRepresentation();
		representation.setObjectName(fileName);
		representation.setStream(file);
		
		String result="";
		try {
			result = s3Service.putObject(bucketName, representation, publicFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new RespuestaGenerica(EstatusResponse.OK.getEstatus(), "Alta de archivo",
				"resultado:", result);		
		
	}

	public List<String> listFiles(String bucketName) {

		ListObjectsRequest listObjectsRequest =
				new ListObjectsRequest()
						.withBucketName(bucketName);

		List<String> keys = new ArrayList<>();

		ObjectListing objects = amazonS3Client.listObjects(listObjectsRequest);

		while (true) {
			List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();
			if (objectSummaries.size() < 1) {
				break;
			}

			for (S3ObjectSummary item : objectSummaries) {
				if (!item.getKey().endsWith("/"))
					keys.add(item.getKey());
			}

			objects = amazonS3Client.listNextBatchOfObjects(objects);
		}

		return keys;
	}


	@Override
	public  byte[]  downloadFile(String bucketName, String fileName) throws IOException {
		return s3Service.downloadFile(bucketName, fileName);
	}
}
