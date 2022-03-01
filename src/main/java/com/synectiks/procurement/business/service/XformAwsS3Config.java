package com.synectiks.procurement.business.service;

import com.amazonaws.auth.BasicAWSCredentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class XformAwsS3Config {

	private String apiAccessKey;
	private String apiSecretKey;
//	private String bucket;
	private String region;

	public BasicAWSCredentials getAwsCredentials() {
		return new BasicAWSCredentials(this.apiAccessKey,this.apiSecretKey); 
	}
}
