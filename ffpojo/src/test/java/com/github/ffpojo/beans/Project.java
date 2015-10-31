package com.github.ffpojo.beans;

import java.util.Date;

import com.github.ffpojo.metadata.positional.annotation.AccessorType;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.metadata.positional.annotation.extra.DatePositionalField;

@PositionalRecord(ignorePositionNotFound=true)
public class Project{
	@PositionalField(initialPosition=1, finalPosition=10)
	private String description;
	@DatePositionalField(initialPosition=11, finalPosition=18, dateFormat="ddMMyyyy")
	private Date startDate;
	@DatePositionalField(initialPosition=19, finalPosition=26, dateFormat="ddMMyyyy")
	private Date endDate;
	public String getDescricao() {
		return description;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Project [description=" + description +"]";
	}
}
