package com.afunms.polling.om;

import java.io.Serializable;

/** @author Hibernate CodeGenerator */
public class UtilHdx implements Serializable {

    /** identifier field */
    private Long id;

    /** nullable persistent field */
    private String ipaddress;

    /** nullable persistent field */
    private String restype;

    /** nullable persistent field */
    private String category;

    /** nullable persistent field */
    private String utilhdx;

	public String getUtilhdx() {
		return utilhdx;
	}

	public void setUtilhdx(String utilhdx) {
		this.utilhdx = utilhdx;
	}

	public String getUtilhdxperc() {
		return utilhdxperc;
	}

	public void setUtilhdxperc(String utilhdxperc) {
		this.utilhdxperc = utilhdxperc;
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

	/** nullable persistent field */
	private String utilhdxperc;

    /** nullable persistent field */
    private java.util.Calendar collecttime;

    /** nullable persistent field */
    private String utilhdxunit;

    /** nullable persistent field */
    private String percunit;

	private String recover;

    /** full constructor */
    public UtilHdx(java.lang.String ipaddress, java.lang.String restype, java.lang.String category, java.lang.String utilhdx,java.lang.String utilhdxperc, java.util.Calendar collecttime, java.lang.String utilhdxunit,java.lang.String percunit, java.lang.String recover) {
        this.ipaddress = ipaddress;
        this.restype = restype;
        this.category = category;
        this.utilhdx = utilhdx;
        this.utilhdxperc = utilhdxperc;
        this.collecttime = collecttime;
        this.utilhdxunit = utilhdxunit;
        this.percunit = percunit;
        this.recover = recover;
    }

    /** default constructor */
    public UtilHdx() {
    }



    public java.lang.String getIpaddress() {
        return this.ipaddress;
    }

	public void setIpaddress(java.lang.String ipaddress) {
		this.ipaddress = ipaddress;
	}

    public java.lang.String getRestype() {
        return this.restype;
    }

	public void setRestype(java.lang.String restype) {
		this.restype = restype;
	}

    public java.lang.String getCategory() {
        return this.category;
    }

	public void setCategory(java.lang.String category) {
		this.category = category;
	}

    
	/**
	 * @return
	 */
	public java.util.Calendar getCollecttime() {
		return collecttime;
	}

	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param calendar
	 */
	public void setCollecttime(java.util.Calendar calendar) {
		collecttime = calendar;
	}

	/**
	 * @param integer
	 */
	public void setId(Long l) {
		id = l;
	}

	

}
