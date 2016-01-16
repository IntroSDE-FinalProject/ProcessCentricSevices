package introsde.finalproject.rest.pcs.resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
    
    private String errorMessage(Exception e){
    	return "{ \n \"error\" : \"Error in PCS, due to the exception: "+e+"\"}";
    }
    
    /**
     * I° Integration Logic: insertNewMeasure(idUser, measureName, value)
     * 		SS setMeasure(idUser, measureName, Value) (save new Measure in the Database)
     * 		BLS checkTarget(Measure) (check if the new Measure satisfies a target)
     * 		SS getMotivationPhrase()
     * 		BLS getCurrentHealth() (send list of measures to the client)
     *
     */
    @GET
    @Path("/measure")
    @Produces( MediaType.APPLICATION_JSON )
    public Response insertNewMeasure(@QueryParam("measure") int measure, 
    		@QueryParam("value") int value) {
    	try{
    		System.out.println("insertNewMeasure: Starting for idPerson "+ this.idPerson +"...");
    		MeasureType m = setMeasure(measure, value);
    		Boolean check = checkTarget(m);
    		String phrase = getPhrase(check);
    		ListMeasureType currentHealth = getCurrentHealth();
    		NewMeasureResponseWrapper nmrw = createWrapper(phrase, currentHealth);
    		return Response.ok(nmrw).build();
    		
    	}catch(Exception e){
    		System.out.println("PCS Error catch creating post reminder response.getStatus() != 200  ");
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    				.entity(errorMessage(e)).build();
    	}
    }
    
    /**
     * Create an object of type NewMeasureResponseWrapper
     * This wrapper is used to put together the currentHealth and the
     * motivation phrase to send to the client
     * @param phrase String
     * @param currentHealth ListMeasureType
     * @return NewMeasureResponseWrapper
     */
	private NewMeasureResponseWrapper createWrapper(String phrase, ListMeasureType currentHealth) {
		NewMeasureResponseWrapper n = new NewMeasureResponseWrapper();
		n.setMeasure(currentHealth);
		n.setPhrase(phrase);
		return n;
	}
	
	/**
	 * 
	 * @param check Boolean
	 * @return String a motivation phrase
	 */
	private String getPhrase(Boolean check) {
		if (check == true) {
			return "Very good, you achieved a new target!!! :)";
		}else{
			return getMotivationPhrase();
		}
	}
	
	/**
	 * Calls BLS
	 * @return ListMeasureType
	 */
	private ListMeasureType getCurrentHealth() {
		Response response = serviceBLS.path(path+"/currentHealth").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		return response.readEntity(ListMeasureType.class);
	}
	
	/**
	 * Returns a motivation phrase
	 * Calls one time the BLS
	 * @return String
	 */
	private String getMotivationPhrase() {
		Response response = serviceBLS.path(path+"/motivation").request().accept(MediaType.TEXT_PLAIN).get(Response.class);
		System.out.println(response);
		return response.readEntity(String.class);
	}
	
	/**
	 * Checks if the target is achieved for the measure passed as parameter
	 * Calls one time the BLS
	 * @param m MeasureType
	 * @return Boolean true if a target is achieved, false otherwise
	 */
	private Boolean checkTarget(MeasureType m) {
		Response response = serviceBLS.path(path+"/measure/"+m.getIdMeasure()+"/check").request().accept(mediaType).get(Response.class);
		System.out.println(response);
		return response.readEntity(Boolean.class);
	}
	
	/**
	 * This method creates a new measure object given an integer,
	 * corresponding to the id of a measure definition, and the value of the new measure.
	 * The new measure is sent to the Storage Service.
	 * This method calls two times the BLS and one time the SS
	 * @param measure int
	 * @param value int
	 * @return MeasureType The new measure
	 */
	private MeasureType setMeasure(int measure, int value) {
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