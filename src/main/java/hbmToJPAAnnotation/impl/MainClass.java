package hbmToJPAAnnotation.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import hbmToJPAAnnotation.MigrationSteps;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class MainClass implements MigrationSteps<Document> {

    @Override
    public Document readFromHbm(String hbmDirection) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        return db.parse(new File(hbmDirection));

    }

    @Override
    public List<PropertyTag> getPropertyTag(Document document) throws Exception {
        return new PropertyTag().getPropertyTag(document);
    }

    @Override
    public List<FormulaTag> getFormulaPropertyTag(Document document) throws Exception {
        return new FormulaTag().getFormulaPropertyTag(document);
    }

    @Override
    public List<FilterTag> getFilterTag(Document document) throws Exception {
        return new FilterTag().getFilterTag(document);
    }

    @Override
    public List<FilterTag> getFilterDefTag(Document document) throws Exception {
        return new FilterTag().getFilterDefTag(document);
    }

    @Override
    public List<ManyToManyTag> getManyToManyTag(Document document) throws Exception {
        return new ManyToManyTag().getManyToManyTag(document);
    }

    @Override
    public List<OneToManyTag> getOneToManyTag(Document document) throws Exception {
        return new OneToManyTag().getOneToManyTag(document);
    }

    @Override
    public List<ManyToOneTag> getManyToOneTag(Document document) throws Exception {
        return new ManyToOneTag().getManyToOneTag(document);
    }

    private void generateTemplate(String directory, String packageName, String tableName, String sequenceName, String typeKey, String className, List<PropertyTag> dataModel,
                                  List<FormulaTag> formulaModel, List<ManyToOneTag> manyToOneModel, List<FilterTag> filterDefModel, List<FilterTag> filterModel, Boolean hasFilter, List<ManyToManyTag> manyToManyModel,
                                  List<OneToManyTag> oneToManyModel, List<Attribute> stterGetterModel, List<UniqueConstraint> uniqueConstraintModel) throws TemplateException, IOException {

        // Instantiate Configuration class
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Create Data Model
        Map<String, Object> map = new HashMap<>();
        map.put("packageName", packageName);
        map.put("className", className);
        map.put("tableName", tableName);
        map.put("schema", Variables.TABLE_SCHEMA);
        map.put("sequenceName", sequenceName);
        map.put("type", typeKey);

        map.put("attributes", dataModel);
        map.put("formulaAttributes", formulaModel);
        map.put("manyToOneList", manyToOneModel);
        map.put("filterDefAttributes", filterDefModel);
        map.put("filterAttributes", filterModel);
        map.put("hasFilter", hasFilter);
        map.put("manyToManyList", manyToManyModel);
        map.put("oneToManyList", oneToManyModel);
        map.put("stterGetterList", stterGetterModel);
        map.put("uniqueConstraintList", uniqueConstraintModel);
        // Instantiate template
        Template template = cfg.getTemplate("model.ftl");
        // File output

        Path path = Paths.get(directory);

        Files.createDirectories(path);

        Writer file = new FileWriter(new File(directory + "\\" + className + ".java"));
        template.process(map, file);
        file.flush();
        file.close();
    }

    private List<UniqueConstraint> groupByUniqueConstraintsByConstraintName(List<Attribute> resultList) {
        List<UniqueConstraint> uniqueConstraints = new ArrayList<UniqueConstraint>();
        Map<String, List<Attribute>> result = resultList.stream().collect(Collectors.groupingBy(Attribute::getUniquekey));
        result.forEach((k, v) -> {
            List<String> columns = v.stream().map(p -> p.getColumn()).collect(Collectors.toList());
            uniqueConstraints.add(new UniqueConstraint(k, columns));
        });

        return uniqueConstraints;

    }

    private List<UniqueConstraint> prepareUniqueConstraints(List<PropertyTag> result, List<ManyToOneTag> manyToOneResult) {
        List<Attribute> uniqueConstraintList = new ArrayList<Attribute>();
        uniqueConstraintList = result.stream().filter(p -> (p.getUniquekey() != null)).collect(Collectors.toList());
        uniqueConstraintList.addAll(manyToOneResult.stream().filter(p -> (p.getUniquekey() != null)).collect(Collectors.toList()));
        return groupByUniqueConstraintsByConstraintName(uniqueConstraintList);
    }

    private List<Attribute> prepareSetterGetter(List<PropertyTag> result, List<FormulaTag> formulaResult, List<ManyToOneTag> manyToOneResult, List<ManyToManyTag> manyToManyTagResult,
                                                List<OneToManyTag> oneToManyTagResult) {
        List<Attribute> getSetList = new ArrayList<Attribute>();
        getSetList = result.stream().map(p -> {
            Attribute a = new Attribute();
            a.setName(p.getName());
            a.setType(p.getType());
            a.setSetterGetter(p.getSetterGetter());
            return a;
        }).collect(Collectors.toList());

        getSetList.addAll(formulaResult.stream().map(p -> {
            Attribute a = new Attribute();
            a.setName(p.getName());
            a.setType(p.getType());
            a.setSetterGetter(p.getSetterGetter());
            return a;
        }).collect(Collectors.toList()));

        getSetList.addAll(manyToOneResult.stream().map(p -> {
            Attribute a = new Attribute();
            a.setName(p.getName());
            a.setType(p.getType());
            a.setSetterGetter(p.getSetterGetter());
            return a;
        }).collect(Collectors.toList()));

        getSetList.addAll(manyToManyTagResult.stream().map(p -> {
            Attribute a = new Attribute();
            a.setName(p.getName());
            a.setType(p.getType());
            a.setSetterGetter(p.getSetterGetter());
            return a;
        }).collect(Collectors.toList()));

        getSetList.addAll(oneToManyTagResult.stream().map(p -> {
            Attribute a = new Attribute();
            a.setName(p.getName());
            a.setType(p.getType());
            a.setSetterGetter(p.getSetterGetter());
            return a;
        }).collect(Collectors.toList()));

        return getSetList;

    }

    private void processHbm(String fileDirection) throws Exception {
        Document document = readFromHbm(fileDirection);
        NodeList idTag = document.getElementsByTagName("id");
        String idType = idTag.item(0).getAttributes().getNamedItem("type").getNodeValue();
        idType = idType.replace(idType.substring(0, 1), idType.substring(0, 1).toUpperCase());

        NodeList clazz = document.getElementsByTagName("class");
        NamedNodeMap classAttrList = clazz.item(0).getAttributes();
        String fullPackageName = classAttrList.getNamedItem("name").getNodeValue();
        String[] re = fullPackageName.split(Pattern.quote("."));

        String tableName = classAttrList.getNamedItem("table").getNodeValue();
        String className = re[re.length - 1];
        String packageName = fullPackageName.substring(0, (fullPackageName.length() - re[re.length - 1].length() - 1));

        String directory = Variables.TEMPLATE_DIR + packageName.replace(".", "\\");

        NodeList generator = document.getElementsByTagName("generator");
        Element elementGenerator = (Element) generator.item(0);
        NodeList paramGenerator = elementGenerator.getElementsByTagName("param");
        String sequenceName = paramGenerator.item(0).getTextContent();

        List<PropertyTag> result = getPropertyTag(document);
        List<FormulaTag> formulaResult = getFormulaPropertyTag(document);
        List<ManyToOneTag> manyToOneResult = getManyToOneTag(document);
        List<FilterTag> filterDefResult = getFilterDefTag(document);
        List<FilterTag> filterResult = getFilterTag(document);

        result = result.stream().filter(r -> !formulaResult.stream().map(f -> f.getName()).collect(Collectors.toList()).contains(r.getName())).collect(Collectors.toList());
        Boolean hasFilter = filterResult != null && filterResult.size() > 0 ? true : null;

        List<ManyToManyTag> manyToManyTagResult = getManyToManyTag(document);
        List<OneToManyTag> oneToManyTagResult = getOneToManyTag(document);

        List<Attribute> stterGetterList = prepareSetterGetter(result, formulaResult, manyToOneResult, manyToManyTagResult, oneToManyTagResult);
        List<UniqueConstraint> uniqueConstraintList = prepareUniqueConstraints(result, manyToOneResult);

        generateTemplate(directory, packageName, tableName, sequenceName, idType, className, result, formulaResult, manyToOneResult, filterDefResult, filterResult, hasFilter, manyToManyTagResult,
                oneToManyTagResult, stterGetterList, uniqueConstraintList);

    }

    private void getAllHbmFile(String directoryName) {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                try {
                    processHbm(file.getAbsolutePath());
                } catch (Exception e) {
                    System.err.println(file.getAbsolutePath());
                    e.printStackTrace();
                }
            } else if (file.isDirectory()) {
                getAllHbmFile(file.getAbsolutePath());
            }
        }
    }

    private void start(String directoryToStart) {
        getAllHbmFile(directoryToStart);

    }

    public static void main(String[] args) {
        String mm;
        // "D:\\java\\WS\\goodsCirculation\\src\\main\\resources\\hibernate\\goodCirculation";
        mm = args[0];
        Variables.TEMPLATE_DIR = args[1];
        if (args.length > 2)
            Variables.TABLE_SCHEMA = args[2];
        MainClass m = new MainClass();
        m.start(mm);
    }
}
