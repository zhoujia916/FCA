package puzzle.fca.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.lang.StringBuilder;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="fca_article_template")
public class FcaArticleTemplate implements Serializable{ 
	/**
	* Constructor
	*/
	public FcaArticleTemplate(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("FcaArticleTemplate{");
        sb.append("templateId=").append(templateId);
        sb.append(", name=").append(name);
        sb.append(", content=").append(content);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="template_id", unique=true)
	private Integer templateId;
	@Column(name="name", nullable=true, length=50)
	private String name;
	@Column(name="content", nullable=true, length=21845)
	private String content;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getTemplateId(){
		return templateId;
	}
	
	public void setTemplateId(Integer templateId){
		this.templateId = templateId;
	}
		
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
		
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}
		
}
