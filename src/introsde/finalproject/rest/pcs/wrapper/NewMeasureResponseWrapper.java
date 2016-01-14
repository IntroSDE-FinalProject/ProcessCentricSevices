package introsde.finalproject.rest.pcs.wrapper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import introsde.finalproject.rest.generated.ListMeasureType;

@XmlRootElement(name="Response")
public class NewMeasureResponseWrapper {
	
	@XmlElement(name="currentHealth")
	@JsonProperty("currentHealth")
	public ListMeasureType currentHealth = new ListMeasureType();
	
	@XmlElement(name="phrase")
	@JsonProperty("phrase")
	public String phrase = new String();
	
	public void setMeasure(ListMeasureType currentHealth) {
		this.currentHealth = currentHealth;
	}
	
	public void setPhrase(String phrase){
		this.phrase = phrase;
	}
}
