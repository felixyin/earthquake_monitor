package com.greathammer.eqm.util;

/**
 * Created by fy on 19/04/2017.
 */
public class JsonBean{

    private boolean status;
    private String message;
    private String data;
   
 
    public JsonBean(boolean status, String message, String data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

 
}
