package hbmToJPAAnnotation;

import java.util.List;

import hbmToJPAAnnotation.impl.*;

public interface MigrationSteps<T> {
	public List<PropertyTag> getPropertyTag(T t) throws Exception;

	public List<FormulaTag> getFormulaPropertyTag(T t) throws Exception;

	public List<FilterTag> getFilterTag(T t) throws Exception;

	public List<FilterTag> getFilterDefTag(T t) throws Exception;

	public List<ManyToManyTag> getManyToManyTag(T t) throws Exception;

	public List<OneToManyTag> getOneToManyTag(T t) throws Exception;

	public List<ManyToOneTag> getManyToOneTag(T t) throws Exception;

	public T readFromHbm(String hbmDirection) throws Exception;
}
