package com.afunms.polling.om;

import java.io.Serializable;

public class PortInfo implements Serializable  {
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	private String nodeId;
	private String port;
	private String ipAddress;
	private String collecttime;
	public String getCollecttime() {
		return collecttime;
	}
	public void setCollecttime(String collecttime) {
		this.collecttime = collecttime;
	}
	public PortInfo(String nodeId,String port,String ipAddress,String collecttime){
		this.nodeId = nodeId;
		this.port = port;
		this.ipAddress = ipAddress;
		this.collecttime = collecttime;
	}
	public PortInfo(){}
}
