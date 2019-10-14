package hbmToJPAAnnotation.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ManyToManyTag extends AttributeSet {
	private String schema;
	private String middleTableName;
	private String foreignKeyOne;
	private String foreignkeySecond;

	private Boolean notNullOne;
	private Boolean notNullSecond;

	public ManyToManyTag() {

	}

	public ManyToManyTag(String schema, String middleTableName, String foreignKeyOne, String foreignkeySecond, String fullPackageName, String type, String name, String setterGetter,
			Boolean notNullOne, Boolean notNullSecond) {

		super(name, type, setterGetter, fullPackageName);

		this.schema = schema;
		this.middleTableName = middleTableName;
		this.foreignKeyOne = foreignKeyOne;
		this.foreignkeySecond = foreignkeySecond;
		this.notNullOne = notNullOne;
		this.notNullSecond = notNullSecond;
	}

	public Boolean getNotNullOne() {
		return notNullOne;
	}

	public void setNotNullOne(Boolean notNullOne) {
		this.notNullOne = notNullOne;
	}

	public Boolean getNotNullSecond() {
		return notNullSecond;
	}

	public void setNotNullSecond(Boolean notNullSecond) {
		this.notNullSecond = notNullSecond;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getMiddleTableName() {
		return middleTableName;
	}

	public void setMiddleTableName(String middleTableName) {
		this.middleTableName = middleTableName;
	}

	public String getForeignKeyOne() {
		return foreignKeyOne;
	}

	public void setForeignKeyOne(String foreignKeyOne) {
		this.foreignKeyOne = foreignKeyOne;
	}

	public String getForeignkeySecond() {
		return foreignkeySecond;
	}

	public void setForeignkeySecond(String foreignkeySecond) {
		this.foreignkeySecond = foreignkeySecond;
	}

	public List<ManyToManyTag> getManyToManyTag(Document document) throws Exception {
		List<ManyToManyTag> manyToManyTagList = new ArrayList<ManyToManyTag>();

		String schema;
		String middleTableName;
		String foreignKeyOne;
		String foreignkeySecond;
		String fullPackageName;
		String type;
		String name;
		String setterGetter;
		Boolean notNullOne;
		Boolean notNullSecond;

		NodeList nodeList = document.getElementsByTagName("set");
		for (int x = 0, size = nodeList.getLength(); x < size; x++) {
			Node node = nodeList.item(x);

			Element element = (Element) node;
			NodeList key = element.getElementsByTagName("key");
			Element elementF1 = (Element) key.item(0);
			NodeList foreignkeyOne = elementF1.getElementsByTagName("column");

			NodeList foreignSecond = element.getElementsByTagName("many-to-many");
			if (foreignSecond.getLength() != 0) {
				schema = node.getAttributes().getNamedItem("schema") != null ? node.getAttributes().getNamedItem("schema").getNodeValue() : null;
				middleTableName = node.getAttributes().getNamedItem("table").getNodeValue();
				name = node.getAttributes().getNamedItem("name").getNodeValue();
				setterGetter = name.replace(name.substring(0, 1), name.substring(0, 1).toUpperCase());

				foreignKeyOne = foreignkeyOne.item(0).getAttributes().getNamedItem("name").getNodeValue();
				notNullOne = foreignkeyOne.item(0).getAttributes().getNamedItem("not-null") != null ? Boolean.valueOf(foreignkeyOne.item(0).getAttributes().getNamedItem("not-null").getNodeValue())
						: null;

				fullPackageName = foreignSecond.item(0).getAttributes().getNamedItem("entity-name").getNodeValue();

				String[] re = foreignSecond.item(0).getAttributes().getNamedItem("entity-name").getNodeValue().split(Pattern.quote("."));
				type = "Set<" + re[re.length - 1] + ">";

				if (foreignSecond.item(0).hasChildNodes()) {
					Element elementForeignSecond = (Element) foreignSecond.item(0);
					NodeList columnSecond = elementForeignSecond.getElementsByTagName("column");
					foreignkeySecond = columnSecond.item(0).getAttributes().getNamedItem("name").getNodeValue();
					notNullSecond = columnSecond.item(0).getAttributes().getNamedItem("not-null") != null
							? Boolean.valueOf(columnSecond.item(0).getAttributes().getNamedItem("not-null").getNodeValue())
							: null;
				} else {
					foreignkeySecond = foreignSecond.item(0).getAttributes().getNamedItem("name").getNodeValue();
					notNullSecond = foreignSecond.item(0).getAttributes().getNamedItem("not-null") != null
							? Boolean.valueOf(foreignSecond.item(0).getAttributes().getNamedItem("not-null").getNodeValue())
							: null;
				}
				ManyToManyTag model = new ManyToManyTag(schema, middleTableName, foreignKeyOne, foreignkeySecond, fullPackageName, type, name, setterGetter, notNullOne, notNullSecond);
				manyToManyTagList.add(model);
			}

		}

		return manyToManyTagList;

	}

}
