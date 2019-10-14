package ${packageName};

import javax.validation.constraints.NotNull;
import org.baharan.framework.model.BaseEntity;
import javax.persistence.UniqueConstraint;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import org.hibernate.annotations.Formula;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import java.util.Date;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

<#list oneToManyList as attribute>
import ${attribute.fullPackageName};
</#list>
<#list manyToManyList as attribute>
import ${attribute.fullPackageName};
</#list>
<#list manyToOneList as attribute>
import ${attribute.packageNameAssosationObject};
</#list>
<#if hasFilter??>
import org.hibernate.annotations.*;
</#if>
 
<#assign uniqueSize = uniqueConstraintList?size>
@Entity
@Getter @Setter
@Table(name = "${tableName?upper_case}" <#if uniqueSize != 0>, uniqueConstraints = {<#list uniqueConstraintList as attribute><#assign countClm = attribute.columns?size>@UniqueConstraint(name = "${attribute.name?upper_case}", columnNames={<#list attribute.columns as clm>"${clm?upper_case}"<#if countClm != 1>,</#if><#assign countClm = countClm-1></#list>})<#if uniqueSize != 1>,</#if><#assign uniqueSize = uniqueSize-1></#list>}</#if>)
<#if hasFilter??>
<#assign j= filterDefAttributes?size>
<#if j != 0>
@FilterDefs({
<#list filterDefAttributes as attribute>
	  <#assign i= attribute.paramList?size>
	  @FilterDef(name = "${attribute.name}", parameters = {<#list attribute.paramList as attribute>@ParamDef(name = "${attribute.name}", type = "${attribute.type}")<#if i != 1>,</#if><#assign i = i-1>
	                                                          </#list>})<#if j != 1>,</#if><#assign j = j-1>
</#list>
})
</#if>
<#assign k= filterAttributes?size>
@Filters({
<#list filterAttributes as attribute>
   @Filter(name = "${attribute.name}", condition = "${attribute.condition}")<#if k != 1>,</#if><#assign k = k-1>
</#list>
})
</#if>
@SequenceGenerator(name = "sequence_db", sequenceName = "<#if schema??>${schema?upper_case}.</#if>${sequenceName?upper_case}", allocationSize = 1)
public class ${className} extends BaseEntity<${type}> {

	<#list attributes as attribute>
	<#if attribute.setterGetter?lower_case != "ip" && attribute.setterGetter?lower_case != "updateddate" && attribute.setterGetter?lower_case != "createddate">

    <#if attribute.notNull?? && attribute.notNull == true>
    @NotNull
    </#if>
    @Column(name = "${attribute.column?upper_case}"<#if attribute.notNull?? && attribute.notNull == true>, nullable = false</#if><#if attribute.unique?? && attribute.unique == true>, unique = ${attribute.unique?c}</#if><#if attribute.update?? && attribute.update == false>, updatable = ${attribute.update?c}</#if><#if attribute.scale??>, scale = ${attribute.scale}</#if><#if attribute.precision??>, precision = ${attribute.precision}</#if><#if attribute.length??>, length = ${attribute.length}</#if>)<#if attribute.hasEnumType??>${"\n"}    @Enumerated(EnumType.ORDINAL)</#if>
    private ${attribute.type} ${attribute.name};
    </#if>
    </#list>
	<#list formulaAttributes as attribute>

    @Formula(" ${attribute.value} ")
    private ${attribute.type} ${attribute.name};
    </#list>
	<#list manyToOneList as attribute>
	<#if attribute.setterGetter?lower_case != "updatedby" && attribute.setterGetter?lower_case != "createdby">

    <#if attribute.notNull?? && attribute.notNull == true>
    @NotNull
    </#if>
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "${attribute.column?upper_case}"<#if attribute.notNull?? && attribute.notNull == true>, nullable = false</#if><#if attribute.unique?? && attribute.unique == true>, unique = ${attribute.unique?c}</#if><#if attribute.update?? && attribute.update == false>, updatable = ${attribute.update?c}</#if>)
	private ${attribute.type} ${attribute.name};
	</#if>       
    </#list>
    <#list manyToManyList as attribute>

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "${attribute.middleTableName?upper_case}"<#if attribute.schema??>,schema = ${attribute.schema}</#if>,
       joinColumns=
           @JoinColumn(name = "${attribute.foreignKeyOne?upper_case}", referencedColumnName = "ID"<#if attribute.notNullOne?? && attribute.notNullOne == true>, nullable = false</#if>),
       inverseJoinColumns=
           @JoinColumn(name = "${attribute.foreignkeySecond?upper_case}", referencedColumnName = "ID"<#if attribute.notNullSecond?? && attribute.notNullSecond == true>, nullable = false</#if>)
    )
    private ${attribute.type} ${attribute.name};
	</#list>
    <#list oneToManyList as attribute>

	@OneToMany(fetch = FetchType.LAZY, mappedBy = ???)
    private ${attribute.type} ${attribute.name};
	</#list>

}