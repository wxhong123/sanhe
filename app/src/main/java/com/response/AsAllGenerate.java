package com.response;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AsAllGenerate { 

	private String id;

	private Timestamp time;

	private String ver;

	private String filePath;

	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	public Timestamp getTime() {
		return time;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}
	
	public String getVer() {
		return ver;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && obj instanceof AsAllGenerate) {
			AsAllGenerate another = (AsAllGenerate) obj;
			equals = new EqualsBuilder()
					.append(id, another.getId())
					.isEquals();
		}
		return equals;
	}
	
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(id)
				.toHashCode();
	}
	
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", getId())
				.toString();
	}
}