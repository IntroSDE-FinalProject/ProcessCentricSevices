package introsde.finalproject.rest.pcs.resources;

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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import introsde.finalproject.rest.generated.PersonType;

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
    private WebTarget service = null;
    private String mediaType = null;


    public PersonResource(UriInfo uriInfo, Request request,int id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idPerson = id;
    }

    public PersonResource(UriInfo uriInfo, Request request,int id, WebTarget service, String mediatype) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idPerson = id;
        this.service = service;
        this.mediaType = mediatype;
    }

    //********************PERSON********************
    
   
    @GET
    @Produces( { MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN } )
    public PersonType readPerson() {
    	System.out.println("readPerson: Reading Person...");
    	Response response = service.path("person/"+this.idPerson).request().accept(mediaType).get(Response.class);
    	if (response.getStatus() == 200 || response.getStatus() == 202) {
    		PersonType u = response.readEntity(PersonType.class);
    		System.out.println(u);
			 return u;
		}else{
			return null;
		}
    	
    	/*
    	Person person = people.getPerson(this.idPerson);
    	if (person == null)
    		return Response.status(Response.Status.NOT_FOUND)
    				.entity("Get: Person with " + this.idPerson + " not found").build();
    	else{
    		System.out.println("Person: "+person.getIdPerson()+" "+person.getLastname());
    		return Response.ok(person).build();
    	}
    	*/
    }
//    
//    @PUT
//    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//    @Produces( MediaType.TEXT_PLAIN )
//	public Response updatePerson(Person person) {
//    	System.out.println("updatePerson: Updating person with id: "+this.idPerson);
//    	person.setIdPerson(this.idPerson);
//        int result = people.updatePerson(person);    
//        if (result >= 0)
//        	return Response.ok(result).build();
//        else if (result == -2)
//        	return Response.status(Response.Status.NOT_FOUND)
//    				.entity("updatePerson: Person with " + this.idPerson + " not found").build();
//        else
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();   	
//    }
//    
//    @DELETE
//    @Produces( MediaType.TEXT_PLAIN )
//    public Response deletePerson() {
//    	System.out.println("detetePerson: Deleting person with id: "+ this.idPerson);
//        int result = people.deletePerson(this.idPerson);
//        if (result == -1)
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();
//        else if (result == -2)
//        	return Response.status(Response.Status.NOT_FOUND)
//    				.entity("deletePerson: Person with " + this.idPerson + " not found").build();
//        else
//        	return Response.status(Response.Status.NO_CONTENT).build();
//    }
//    
//    @GET
//    @Path("/vitalsings")
//    @Produces( MediaType.APPLICATION_JSON )
//    public List<Measure> getVitalSigns() {
//    	System.out.println("getVitalSigns: Reading vital signs for idPerson "+ this.idPerson +"...");
//    	List<Measure> result = people.getVitalSigns(this.idPerson);
//    	return result;
//    }
//    
//    @GET
//    @Path("/currentHealth")
//    @Produces( MediaType.APPLICATION_JSON )
//    public List<Measure> getCurrentHealth() {
//    	System.out.println("getCurrentHealth: Reading CurrentHealth for idPerson "+ this.idPerson +"...");
//    	List<Measure> result = people.getCurrentHealth(this.idPerson);
//    	return result;
//    }
//    
//    //********************TARGET********************
//    
//    @GET
//    @Path("/target")
//    @Produces( MediaType.APPLICATION_JSON )
//    public List<Target> getTargetList() {
//    	System.out.println("getTargetList: Reading targets for idPerson "+ this.idPerson +"...");
//    	List<Target> result = people.getTargetList(this.idPerson);
//    	return result;
//    }
//    
//   
//    @POST
//	@Path("/target")
//    @Produces( MediaType.TEXT_PLAIN )
//    @Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})
//    public Response createTarget(Target target){
//    	System.out.println("New Target for " + target.getMeasureDefinition().getMeasureName());
//        System.out.println("createTarget: Creating new target...");
//        int id = this.people.createTarget(target, this.idPerson);
//        if(id == -1)
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();
//        else
//        	return Response.status(Response.Status.CREATED).entity(id).build();
//    }
//    
//    @PUT
//    @Path("/target/{targetId}")
//    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//    @Produces( MediaType.TEXT_PLAIN )
//	public Response updateTarget(Target target, @PathParam("targetId") int targetId) {
//    	System.out.println("updateTarget: Updating target with id: "+ targetId);
//    	target.setIdTarget(targetId);
//        int result = people.updateTarget(target);    
//        if (result >= 0)
//        	return Response.ok(result).build();
//        else if (result == -2)
//        	return Response.status(Response.Status.NOT_FOUND)
//    				.entity("updateTarget: Target with " + targetId + " not found").build();
//        else
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();   	
//    }
//    
//    @DELETE
//    @Path("/target/{targetId}")
//    @Produces( MediaType.TEXT_PLAIN )
//    public Response deleteTarget(@PathParam("targetId") int targetId) {
//    	System.out.println("deteteTarget: Deleting target with id: "+ targetId);
//        int result = people.deleteTarget(targetId);
//        if (result == -1)
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();
//        else if (result == -2)
//        	return Response.status(Response.Status.NOT_FOUND)
//    				.entity("deleteTarget: Target with " + targetId + " not found").build();
//        else
//        	return Response.status(Response.Status.NO_CONTENT).build();
//    }
//    
//    @GET
//    @Path("/target/{measureDefinitionId}")
//    @Produces( MediaType.APPLICATION_JSON )
//    public List<Target> getTarget(@PathParam("measureDefinitionId") int measureDefId) {
//    	System.out.println("getTarget: Reading target for idPerson = "+ this.idPerson +
//    			" and measureDefId = "+measureDefId+"...");
//    	List<Target> result = people.getTarget(this.idPerson, measureDefId);
//    	return result;
//    }
//    
//    //***********************REMINDER***********************
//    
//    @GET
//    @Path("/reminder")
//    @Produces( MediaType.APPLICATION_JSON )
//    public List<Reminder> getReminder() {
//    	System.out.println("getReminder: Reading reminders for idPerson "+ this.idPerson +"...");
//    	List<Reminder> result = people.getReminder(this.idPerson);
//    	return result;
//    }
//    
//    @POST
//	@Path("/reminder")
//    @Produces( MediaType.TEXT_PLAIN )
//    @Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})
//    public Response createReminder(Reminder reminder){
//    	//TODO finire
//    /*	System.out.println("New Reminder for person" + this.);
//        System.out.println("createReminder: Creating new reminder...");
//        int id = this.people.createTarget(target, this.idPerson);
//        if(id == -1)
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();
//        else
//        	return Response.status(Response.Status.CREATED).entity(id).build();*/
//    	return null;
//    }
//    
//    @PUT
//    @Path("/reminder/{reminderId}")
//    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//    @Produces( MediaType.TEXT_PLAIN )
//	public Response updateReminder(Reminder reminder, @PathParam("reminderId") int reminderId) {
//    	System.out.println("updateReminder: Updating reminder with id: "+ reminderId);
//    	reminder.setIdReminder(reminderId);
//        int result = people.updateReminder(reminder);    
//        if (result >= 0)
//        	return Response.ok(result).build();
//        else if (result == -2)
//        	return Response.status(Response.Status.NOT_FOUND)
//    				.entity("updateReminder: Reminder with " + reminderId + " not found").build();
//        else
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();   	
//    }
//    
//    @DELETE
//    @Path("/reminder/{reminderId}")
//    @Produces( MediaType.TEXT_PLAIN )
//    public Response deleteReminder(@PathParam("reminderId") int reminderId) {
//    	System.out.println("deteteReminder: Deleting reminder with id: "+ reminderId);
//        int result = people.deleteReminder(reminderId);
//        if (result == -1)
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();
//        else if (result == -2)
//        	return Response.status(Response.Status.NOT_FOUND)
//    				.entity("deleteReminder: Reminder with " + reminderId + " not found").build();
//        else
//        	return Response.status(Response.Status.NO_CONTENT).build();
//    }
//    
//    //***********************MEASURE***********************
//    
//    @GET
//    @Path("/measure")
//    @Produces( MediaType.APPLICATION_JSON )
//    public List<Measure> getMeasure() {
//    	System.out.println("getMeasure: Reading measures for idPerson "+ this.idPerson +"...");
//    	List<Measure> result = people.getMeasure(this.idPerson);
//    	return result;
//    }
//    
//    @POST
//	@Path("/measure")
//    @Produces( MediaType.TEXT_PLAIN )
//    @Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})
//    public Response createMeasure(Measure measure){
//    	System.out.println("New Measure for person" + this.idPerson);
//        System.out.println("createMeasure: Creating new measure...");
//        int id = this.people.createMeasure(measure, this.idPerson);
//        if(id == -1)
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();
//        else
//        	return Response.status(Response.Status.CREATED).entity(id).build();
//    }
//    
//    @PUT
//    @Path("/measure/{measureId}")
//    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//    @Produces( MediaType.TEXT_PLAIN )
//	public Response updateMeasure(Measure measure, @PathParam("measureId") int measureId) throws ParseException_Exception {
//    	System.out.println("updateMeasure: Updating measures with id: "+ measureId);
//    	measure.setIdMeasure(measureId);
//        int result = people.updateMeasure(measure);    
//        if (result >= 0)
//        	return Response.ok(result).build();
//        else if (result == -2)
//        	return Response.status(Response.Status.NOT_FOUND)
//    				.entity("updateMeasure: Measure with " + measureId + " not found").build();
//        else
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();   	
//    }
//    
//    @DELETE
//    @Path("/measure/{measureId}")
//    @Produces( MediaType.TEXT_PLAIN )
//    public Response deleteMeasure(@PathParam("measureId") int measureId) {
//    	System.out.println("deteteMeasure: Deleting Measure with id: "+ measureId);
//        int result = people.deleteMeasure(measureId);
//        if (result == -1)
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();
//        else if (result == -2)
//        	return Response.status(Response.Status.NOT_FOUND)
//    				.entity("deleteMeasure: Measure with " + measureId + " not found").build();
//        else
//        	return Response.status(Response.Status.NO_CONTENT).build();
//    }
//    
}