package hbmToJPAAnnotation.impl;

public class AttributeSet extends Attribute {

	private String fullPackageName;

	public AttributeSet() {
	}

	public AttributeSet(String name, String type, String setterGetter, String fullPackageName) {

		super(name, type, setterGetter, null, null, null, null, null);
		this.fullPackageName = fullPackageName;
	}

	public String getFullPackageName() {
		return fullPackageName;
	}

	public void setFullPackageName(String fullPackageName) {
		this.fullPackageName = fullPackageName;
	}

}
