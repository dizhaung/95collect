package com.afunms.polling.om;

import java.io.Serializable;

public class Portinfocollectdata implements Serializable {
	private String nodeId;
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	private String port;
	private String ipAddress;
	private String collecttime;
	
	public String getCollecttime() {
		return collecttime;
	}

	public void setCollecttime(String collecttime) {
		this.collecttime = collecttime;
	}

	public Portinfocollectdata(String nodeId,String port,String ipAddress,String collecttime){
		this.nodeId = nodeId;
		this.port = port;
		this.ipAddress = ipAddress;
		this.collecttime = collecttime;
	}
	public Portinfocollectdata(){}
}
