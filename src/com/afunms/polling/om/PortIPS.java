package com.afunms.polling.om;

import java.io.Serializable;

public class PortIPS implements Serializable {
	private Long id;	//ID
	private String ipaddress;	//�豸IP
	private String restype;	//dynamic
	

	private String category;	//Interface
	private String entity;	//�����
    private String utilhdx;	//����
    private String utilhdxPerc;	//����������
    private String collecttime;	//�ɼ�ʱ��
    private String utilhdxunit;//������λ
    private String percunit;//����λ
	private String recover;// ��������ֵ
	private String discardsPerc;	//�˿ڶ�����
	private String errorsPerc;	//�˿ڴ�����
	private String subentity;	//�˿�����
	private String utilhdxflag ; //�˿������쳣��־λ
	private double ifSpeed;	//�˿�����

	public Double getIfSpeed() {
		return ifSpeed;
	}

	public void setIfSpeed(Double ifSpeed) {
		this.ifSpeed = ifSpeed;
	}

	public String getUtilhdxflag() {
		return utilhdxflag;
	}

	public void setUtilhdxflag(String utilhdxflag) {
		this.utilhdxflag = utilhdxflag;
	}

	public PortIPS(String ipaddress,Double ifSpeed,String subentity,String utilhdx,String restype,String entity,String category,String utilhdxPerc,String utilhdxunit,String percunit,String recover,String discardsPerc,String errorsPerc,String collecttime){
		this.ipaddress=ipaddress;
		this.subentity = subentity;
		this.restype = restype;
		this.entity = entity;
		this.category = category;
		this.utilhdx = utilhdx;
		this.utilhdxPerc = utilhdxPerc;
		this.utilhdxunit = utilhdxunit;
		this.percunit = percunit;
		this.discardsPerc = discardsPerc;
		this.errorsPerc = errorsPerc;
		this.recover = recover;
		this.collecttime = collecttime;
		this.ifSpeed = ifSpeed;
	}
	
	public PortIPS(){}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getRestype() {
		return restype;
	}

	public void setRestype(String restype) {
		this.restype = restype;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getUtilhdx() {
		return utilhdx;
	}

	public void setUtilhdx(String utilhdx) {
		this.utilhdx = utilhdx;
	}

	public String getUtilhdxPerc() {
		return utilhdxPerc;
	}

	public void setUtilhdxPerc(String utilhdxPerc) {
		this.utilhdxPerc = utilhdxPerc;
	}

	public String getCollecttime() {
		return collecttime;
	}

	public void setCollecttime(String collecttime) {
		this.collecttime = collecttime;
	}

	public String getUtilhdxunit() {
		return utilhdxunit;
	}

	public void setUtilhdxunit(String utilhdxunit) {
		this.utilhdxunit = utilhdxunit;
	}

	public String getPercunit() {
		return percunit;
	}

	public void setPercunit(String percunit) {
		this.percunit = percunit;
	}

	public String getRecover() {
		return recover;
	}

	public void setRecover(String recover) {
		this.recover = recover;
	}

	public String getDiscardsPerc() {
		return discardsPerc;
	}

	public void setDiscardsPerc(String discardsPerc) {
		this.discardsPerc = discardsPerc;
	}

	public String getErrorsPerc() {
		return errorsPerc;
	}

	public void setErrorsPerc(String errorsPerc) {
		this.errorsPerc = errorsPerc;
	}
	public String getSubentity() {
		return subentity;
	}

	public void setSubentity(String subentity) {
		this.subentity = subentity;
	}
	
}
