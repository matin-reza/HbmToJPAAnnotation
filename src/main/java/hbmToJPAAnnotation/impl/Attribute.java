package hbmToJPAAnnotation.impl;

public class Attribute {
	private String name;
	private String column;
	private String type;
	private String setterGetter;
	private Boolean notNull;
	private Boolean update;
	private Boolean unique;
	private String uniquekey;

	public Attribute() {

	}

	public Attribute(String name, String type, String setterGetter, Boolean notNull, Boolean update, Boolean unique, String uniquekey, String column) {
		this.name = name;
		this.type = type.equals("Binary") ? "byte[]" : type;
		this.notNull = notNull;
		this.update = update;
		this.unique = unique;
		this.uniquekey = uniquekey;
		this.column = column;
		this.setterGetter = name.subSequence(0, 1).toString().toUpperCase() + name.subSequence(1, name.length());
	}

	public Boolean getNotNull() {
		return notNull;
	}

	public void setNotNull(Boolean notNull) {
		this.notNull = notNull;
	}

	public Boolean getUpdate() {
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

	public Boolean getUnique() {
		return unique;
	}

	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

	public String getUniquekey() {
		return uniquekey;
	}

	public void setUniquekey(String uniquekey) {
		this.uniquekey = uniquekey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSetterGetter() {
		return setterGetter;
	}

	public void setSetterGetter(String setterGetter) {
		this.setterGetter = setterGetter;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

}
