package hbmToJPAAnnotation.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ManyToOneTag extends Attribute {
	private String packageNameAssosiationObject;

	public ManyToOneTag() {
	}

	public ManyToOneTag(String name, String type, String setterGetter, Boolean notNull, Boolean update, Boolean unique, String uniquekey, String column, String packageNameAssosationObject) {
		super(name, type, setterGetter, notNull, update, unique, uniquekey, column);
		this.packageNameAssosiationObject = packageNameAssosationObject;
	}

	public String getPackageNameAssosationObject() {
		return packageNameAssosiationObject;
	}

	public void setPackageNameAssosationObject(String packageNameAssosationObject) {
		this.packageNameAssosiationObject = packageNameAssosationObject;
	}

	public List<ManyToOneTag> getManyToOneTag(Document document) throws Exception {
		NodeList nodeList = document.getElementsByTagName("many-to-one");
		List<ManyToOneTag> pList = new ArrayList<ManyToOneTag>();
		for (int x = 0, size = nodeList.getLength(); x < size; x++) {
			NamedNodeMap attrList = nodeList.item(x).getAttributes();

			Node nodeColumn = null;

			if (nodeList.item(x).hasChildNodes()) {
				Element columnElement = (Element) nodeList.item(x);
				NodeList columnTage = columnElement.getElementsByTagName("column");
				nodeColumn = columnTage.item(0);
			}

			String packageNameAssosationObject = attrList.getNamedItem("entity-name").getNodeValue();
			String[] re = packageNameAssosationObject.split(Pattern.quote("."));
			String objectName = re[re.length - 1];

			pList.add(new ManyToOneTag(

					attrList.getNamedItem("name").getNodeValue(),

					objectName,

					attrList.getNamedItem("name").getNodeValue(),

					Boolean.valueOf(attrList.getNamedItem("not-null") != null ? attrList.getNamedItem("not-null").getNodeValue()
							: nodeColumn != null && nodeColumn.getAttributes().getNamedItem("not-null") != null ? nodeColumn.getAttributes().getNamedItem("not-null").getNodeValue() : null),

					Boolean.valueOf(attrList.getNamedItem("update") != null ? attrList.getNamedItem("update").getNodeValue()
							: nodeColumn != null && nodeColumn.getAttributes().getNamedItem("update") != null ? nodeColumn.getAttributes().getNamedItem("update").getNodeValue() : null),

					Boolean.valueOf(attrList.getNamedItem("unique") != null ? attrList.getNamedItem("unique").getNodeValue()
							: nodeColumn != null && nodeColumn.getAttributes().getNamedItem("unique") != null ? nodeColumn.getAttributes().getNamedItem("unique").getNodeValue() : null),

					attrList.getNamedItem("unique-key") != null ? attrList.getNamedItem("unique-key").getNodeValue()
							: nodeColumn != null && nodeColumn.getAttributes().getNamedItem("unique-key") != null ? nodeColumn.getAttributes().getNamedItem("unique-key").getNodeValue() : null,

					nodeColumn != null ? nodeColumn.getAttributes().getNamedItem("name").getNodeValue()
							: attrList.getNamedItem("column") != null ? attrList.getNamedItem("column").getNodeValue() : attrList.getNamedItem("name").getNodeValue(),

					packageNameAssosationObject));
		}

		return pList;
	}
}
