package hbmToJPAAnnotation.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OneToManyTag extends AttributeSet {
	private String foreignKey;

	public OneToManyTag() {

	}

	public OneToManyTag(String name, String type, String setterGetter, String fullPackageName, String foreignKey) {

		super(name, type, setterGetter, fullPackageName);
		this.foreignKey = foreignKey;
	}

	public String getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}

	public List<OneToManyTag> getOneToManyTag(Document document) throws Exception {
		List<OneToManyTag> oneToManyTagList = new ArrayList<OneToManyTag>();

		String foreignKey;
		String fullPackageName;
		String type;
		String name;
		String setterGetter;

		NodeList nodeList = document.getElementsByTagName("set");
		for (int x = 0, size = nodeList.getLength(); x < size; x++) {
			Node node = nodeList.item(x);

			Element element = (Element) node;
			NodeList key = element.getElementsByTagName("key");
			Element elementF1 = (Element) key.item(0);
			NodeList foreignkey = elementF1.getElementsByTagName("column");

			NodeList oneToManyTag = element.getElementsByTagName("one-to-many");
			if (oneToManyTag.getLength() != 0) {
				name = node.getAttributes().getNamedItem("name").getNodeValue();
				setterGetter = name.replace(name.substring(0, 1), name.substring(0, 1).toUpperCase());

				foreignKey = foreignkey.item(0).getAttributes().getNamedItem("name").getNodeValue();

				fullPackageName = oneToManyTag.item(0).getAttributes().getNamedItem("class").getNodeValue();

				String[] re = oneToManyTag.item(0).getAttributes().getNamedItem("class").getNodeValue().split(Pattern.quote("."));
				type = "Set<" + re[re.length - 1] + ">";

				OneToManyTag model = new OneToManyTag(name, type, setterGetter, fullPackageName, foreignKey);
				oneToManyTagList.add(model);
			}

		}

		return oneToManyTagList;

	}
}
