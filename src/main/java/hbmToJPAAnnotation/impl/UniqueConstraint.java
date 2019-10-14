package hbmToJPAAnnotation.impl;

import java.util.List;

public class UniqueConstraint {
	private String name;
	private List<String> columns;

	public UniqueConstraint(String name, List<String> columns) {
		this.name = name;
		this.columns = columns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

}
