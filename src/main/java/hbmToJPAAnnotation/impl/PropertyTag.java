package hbmToJPAAnnotation.impl;

import java.util.*;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PropertyTag extends Attribute {
	private String length;
	private String precision;
	private String scale;
	private Boolean hasEnumType;
	private String eumPackageFullName;

	public PropertyTag() {
	}

	public PropertyTag(String name, String type, Boolean notNull, Boolean update, Boolean unique, String uniquekey, String column, String setterGetter, String length, String precision, String scale,
			Boolean hasEnumType, String eumPackageFullName) {
		super(name, type, setterGetter, notNull, update, unique, uniquekey, column);
		this.length = length;
		this.precision = precision;
		this.scale = scale;
		this.hasEnumType = hasEnumType;
		this.eumPackageFullName = eumPackageFullName;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public Boolean getHasEnumType() {
		return hasEnumType;
	}

	public void setHasEnumType(Boolean hasEnumType) {
		this.hasEnumType = hasEnumType;
	}

	public String getEumPackageFullName() {
		return eumPackageFullName;
	}

	public void setEumPackageFullName(String eumPackageFullName) {
		this.eumPackageFullName = eumPackageFullName;
	}

	private PropertyTag extractPropertyFromNode(Node node) throws Exception {
		String length = null;
		String precision = null;
		String scale = null;
		Boolean hasEnumType = null;
		String name = null;
		String column = null;
		String type = null;
		String setterGetter = null;
		Boolean notNull = null;
		Boolean update = null;
		Boolean unique = null;
		String uniquekey = null;
		String eumPackageFullName = null;
		String childType = null;

		if (node.hasChildNodes()) {
			Element elmnt = (Element) node;
			NodeList typ = elmnt.getElementsByTagName("type");
			Node nd = typ.item(0);
			if (nd != null)
				childType = "type";
			else {
				typ = elmnt.getElementsByTagName("column");
				nd = typ.item(0);
				if (nd != null)
					childType = "column";
				else
					throw new Exception();
			}
		}

		if (!node.hasChildNodes() || childType.equals("type")) {

			name = node.getAttributes().getNamedItem("name").getNodeValue();

			length = node.getAttributes().getNamedItem("length") != null ? node.getAttributes().getNamedItem("length").getNodeValue() : null;

			precision = node.getAttributes().getNamedItem("precision") != null ? node.getAttributes().getNamedItem("precision").getNodeValue() : null;

			scale = node.getAttributes().getNamedItem("scale") != null ? node.getAttributes().getNamedItem("scale").getNodeValue() : null;

			column = node.getAttributes().getNamedItem("column") != null ? node.getAttributes().getNamedItem("column").getNodeValue() : name;

			type = node.getAttributes().getNamedItem("type") != null ? node.getAttributes().getNamedItem("type").getNodeValue() : "Date??";

			setterGetter = node.getAttributes().getNamedItem("name").getNodeValue();

			notNull = node.getAttributes().getNamedItem("not-null") != null ? Boolean.valueOf(node.getAttributes().getNamedItem("not-null").getNodeValue()) : null;

			update = node.getAttributes().getNamedItem("update") != null ? Boolean.valueOf(node.getAttributes().getNamedItem("update").getNodeValue()) : null;

			unique = Boolean.valueOf(node.getAttributes().getNamedItem("unique").getNodeValue());

			uniquekey = node.getAttributes().getNamedItem("unique-key") != null ? node.getAttributes().getNamedItem("unique-key").getNodeValue() : null;

			if (node.hasChildNodes()) {
				hasEnumType = true;

				Element typeTageElement = (Element) node;

				NodeList typeTage = typeTageElement.getElementsByTagName("type");
				if (typeTage.item(0).hasChildNodes()) {
					Node paramNode = typeTage.item(0);
					Element ParamElement = (Element) paramNode;
					NodeList paramTag = ParamElement.getElementsByTagName("param");
					eumPackageFullName = paramTag.item(0).getTextContent();

					String[] re = eumPackageFullName.split(Pattern.quote("."));
					type = re[re.length - 1];
				}

			}
		} else if (childType.equals("column")) {
			Node nodeColumn = null;
			if (node.hasChildNodes()) {
				Element typeTageElement = (Element) node;
				NodeList typeTage = typeTageElement.getElementsByTagName("column");
				nodeColumn = typeTage.item(0);
			}

			name = node.getAttributes().getNamedItem("name").getNodeValue();

			length = node.getAttributes().getNamedItem("length") != null ? node.getAttributes().getNamedItem("length").getNodeValue()
					: nodeColumn.getAttributes().getNamedItem("length") != null ? nodeColumn.getAttributes().getNamedItem("length").getNodeValue() : null;

			precision = node.getAttributes().getNamedItem("precision") != null ? node.getAttributes().getNamedItem("precision").getNodeValue()
					: nodeColumn.getAttributes().getNamedItem("precision") != null ? nodeColumn.getAttributes().getNamedItem("precision").getNodeValue() : null;

			scale = node.getAttributes().getNamedItem("scale") != null ? node.getAttributes().getNamedItem("scale").getNodeValue()
					: nodeColumn.getAttributes().getNamedItem("scale") != null ? nodeColumn.getAttributes().getNamedItem("scale").getNodeValue() : null;

			column = node.getAttributes().getNamedItem("column") != null ? node.getAttributes().getNamedItem("column").getNodeValue() : nodeColumn.getAttributes().getNamedItem("name").getNodeValue();

			type = node.getAttributes().getNamedItem("type") != null ? node.getAttributes().getNamedItem("type").getNodeValue() : null;

			notNull = node.getAttributes().getNamedItem("not-null") != null ? Boolean.valueOf(node.getAttributes().getNamedItem("not-null").getNodeValue())
					: nodeColumn.getAttributes().getNamedItem("not-null") != null ? Boolean.valueOf(nodeColumn.getAttributes().getNamedItem("not-null").getNodeValue()) : null;

			setterGetter = node.getAttributes().getNamedItem("name").getNodeValue();

			update = node.getAttributes().getNamedItem("update") != null ? Boolean.valueOf(node.getAttributes().getNamedItem("update").getNodeValue())
					: nodeColumn.getAttributes().getNamedItem("update") != null ? Boolean.valueOf(nodeColumn.getAttributes().getNamedItem("update").getNodeValue()) : null;

			unique = node.getAttributes().getNamedItem("unique") != null ? Boolean.valueOf(node.getAttributes().getNamedItem("unique").getNodeValue())
					: Boolean.valueOf(nodeColumn.getAttributes().getNamedItem("unique").getNodeValue());

			uniquekey = node.getAttributes().getNamedItem("unique-key") != null ? node.getAttributes().getNamedItem("unique-key").getNodeValue()
					: nodeColumn.getAttributes().getNamedItem("unique-key") != null ? nodeColumn.getAttributes().getNamedItem("unique-key").getNodeValue() : null;

		} else {
			throw new Exception();
		}

		return new PropertyTag(

				name,

				this.getType(type),

				notNull,

				update,

				unique,

				uniquekey,

				column,

				name,

				length,

				precision,

				scale,

				hasEnumType,

				eumPackageFullName);

	}

	private String getType(String type) {
		Set<String> primitiveDataTypes = new HashSet<String>(Arrays.asList("int", "long", "float", "double", "boolean"));
		if(type == null) {
			return "Date??";
		}
		else {
			if(primitiveDataTypes.contains(type)) {
				return type;
			}
			else {
				return type.replaceAll(type.substring(0, 1), type.substring(0, 1).toUpperCase());
			}
		}
	}

	public List<PropertyTag> getPropertyTag(Document document) throws Exception {
		NodeList nodeList = document.getElementsByTagName("property");
		List<PropertyTag> pList = new ArrayList<>();
		for (int x = 0, size = nodeList.getLength(); x < size; x++) {

			NamedNodeMap attrList = nodeList.item(x).getAttributes();

			pList.add(extractPropertyFromNode(nodeList.item(x)));
		}

		return pList;

	}

}
