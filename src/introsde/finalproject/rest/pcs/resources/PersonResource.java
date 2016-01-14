package introsde.finalproject.rest.pcs.resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import introsde.finalproject.rest.generated.ListMeasureType;
import introsde.finalproject.rest.generated.MeasureDefinitionType;
import introsde.finalproject.rest.generated.MeasureType;
import introsde.finalproject.rest.generated.PersonType;
import introsde.finalproject.rest.pcs.wrapper.NewMeasureResponseWrapper;

/**
 *
 */
@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class PersonResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int idPerson;
    private WebTarget serviceBLS = null;
	private WebTarget serviceSS = null;
    private String mediaType = null;
    private String path = null;


    public PersonResource(UriInfo uriInfo, Request request,int id, String mediaType2) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idPerson = id;
        this.path = "person/"+this.idPerson;
    }

    public PersonResource(UriInfo uriInfo, Request request,int id, WebTarget serviceBLS, WebTarget serviceSS, String mediatype) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idPerson = id;
        this.serviceBLS = serviceBLS;
        this.serviceSS = serviceSS;
        this.mediaType = mediatype;
        this.path = "person/"+this.idPerson;
    }
    
    /**
     * I° Integration Logic: insertNewMeasure(idUser, measureName, value)
     * 		SS setMeasure(idUser, measureName, Value) (save new Measure in the Database)
     * 		BLS checkTarget(Measure) (check if the new Measure satisfies a target)
     * 		SS getMotivationPhrase()
     * 		BLS getCurrentHealth() (send list of measures to the client)
     * @return
     */
    @GET
	@Path("/measure")
	@Produces( MediaType.APPLICATION_JSON )
	public Response insertNewMeasure(@QueryParam("measure") int measure, 
    		@QueryParam("value") Double value) {
		System.out.println("insertNewMeasure: Starting for idPerson "+ this.idPerson +"...");
		MeasureType m = setMeasure(measure, value);
		//Boolean check = checkTarget(m);
		String phrase = getPhrase(false);
		ListMeasureType currentHealth = getCurrentHealth();
		NewMeasureResponseWrapper nmrw = createWrapper(phrase, currentHealth);
		return Response.ok(nmrw).build();
	}

	private NewMeasureResponseWrapper createWrapper(String phrase, ListMeasureType currentHealth) {
		NewMeasureResponseWrapper n = new NewMeasureResponseWrapper();
		n.setMeasure(currentHealth);
		n.setPhrase(phrase);
		return n;
	}

	private String getPhrase(Boolean check) {
		if (check == true) {
			return "Very good, you achieved a new target!!! :)";
		}else{
			return getMotivationPhrase();
		}
	}

	private ListMeasureType getCurrentHealth() {
		Response response = serviceBLS.path(path+"/currentHealth").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		return response.readEntity(ListMeasureType.class);
	}

	private String getMotivationPhrase() {
		Response response = serviceBLS.path(path+"/motivation").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		return response.readEntity(String.class);
	}

	private Boolean checkTarget(MeasureType m) {
		// TODO Auto-generated method stub
		return null;
	}

	private MeasureType setMeasure(int measure, Double value) {
		//retrieve measureDefinition corresponding to measure
		Response response = serviceBLS.path("/measureDefinition").queryParam("measure", measure).request().accept(mediaType).get(Response.class);
		System.out.println(response);
		MeasureDefinitionType mdef = response.readEntity(MeasureDefinitionType.class);
		//create a new measure
		MeasureType newMeasure = new MeasureType();
		newMeasure.setMeasureDefinition(mdef);
		newMeasure.setValue(String.valueOf(value));
		//set today date to timestamp field
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String timestamp = format.format(Calendar.getInstance().getTime());
		newMeasure.setTimestamp(timestamp);
		//post new measure to StorageService
		Response response2 = serviceSS.path(path+"/measure").request(mediaType)
				.post(Entity.entity(newMeasure, mediaType), Response.class);
		System.out.println(response2);
		//retrieve the id of the new saved measure
		Integer idMeasure = response2.readEntity(Integer.class);
		//retrieve the new measure from BLS
		Response response3 = serviceBLS.path(path+"/measure/"+idMeasure).request().accept(mediaType).get(Response.class);
		System.out.println(response3);
		MeasureType m = response3.readEntity(MeasureType.class);
		return m;
	}
    
    
}