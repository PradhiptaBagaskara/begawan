package com.pt.begawanpolosoro.update;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseUpdate implements Serializable {

	@SerializedName("outputName")
	private String outputName;

	@SerializedName("size")
	private String size;

	public long getBytes() {
		return bytes;
	}

	public void setBytes(long bytes) {
		this.bytes = bytes;
	}

	@SerializedName("bytes")
	private long bytes;

	@SerializedName("updateRequired")
	private boolean updateRequired;

	@SerializedName("downloadUrl")
	private String downloadUrl;

	@SerializedName("versionName")
	private String versionName;

	@SerializedName("versionCode")
	private int versionCode;

	@SerializedName("hash")
	private String hash;

	public void setOutputName(String outputName){
		this.outputName = outputName;
	}

	public String getOutputName(){
		return outputName;
	}

	public void setSize(String size){
		this.size = size;
	}

	public String getSize(){
		return size;
	}

	public void setUpdateRequired(boolean updateRequired){
		this.updateRequired = updateRequired;
	}

	public boolean isUpdateRequired(){
		return updateRequired;
	}

	public void setDownloadUrl(String downloadUrl){
		this.downloadUrl = downloadUrl;
	}

	public String getDownloadUrl(){
		return downloadUrl;
	}

	public void setVersionName(String versionName){
		this.versionName = versionName;
	}

	public String getVersionName(){
		return versionName;
	}

	public void setVersionCode(int versionCode){
		this.versionCode = versionCode;
	}

	public int getVersionCode(){
		return versionCode;
	}

	public void setHash(String hash){
		this.hash = hash;
	}

	public String getHash(){
		return hash;
	}

	@Override
 	public String toString(){
		return 
			"ResponseUpdate{" + 
			"outputName = '" + outputName + '\'' + 
			",size = '" + size + '\'' + 
			",updateRequired = '" + updateRequired + '\'' + 
			",downloadUrl = '" + downloadUrl + '\'' + 
			",versionName = '" + versionName + '\'' + 
			",versionCode = '" + versionCode + '\'' + 
			",hash = '" + hash + '\'' + 
			"}";
		}
}