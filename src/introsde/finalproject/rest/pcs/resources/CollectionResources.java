package introsde.finalproject.rest.pcs.resources;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import javax.ejb.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.glassfish.jersey.client.ClientConfig;

@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
@Path("/")
public class CollectionResources {

    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    
    private static String uriServer = "https://ss-serene-hamlet-9690.herokuapp.com/sdelab"; //StorageService
	private static String mediaType = MediaType.APPLICATION_JSON;

	private Client client = null;
	private WebTarget service = null;
	private ClientConfig clientConfig = null;
	
	public CollectionResources() throws MalformedURLException{
		clientConfig = new ClientConfig();
		client = ClientBuilder.newClient(clientConfig);
		service = client.target(getBaseURI(uriServer));
	}
	
	private static URI getBaseURI(String uriServer) {
		return UriBuilder.fromUri(uriServer).build();
	}
    
	public void reloadUri(){
		service = null;
		service = client.target(getBaseURI(uriServer));
	}
	
	//***********************Person***********************

	 @Path("person/{personId}")
	 public PersonResource getPerson(@PathParam("personId") int id) {
		 return new PersonResource(uriInfo, request, id, service, mediaType);
	 }
	
	 @Path("doctor/{doctorId}")
	 public DoctorResource getDoctor(@PathParam("doctorId") int id) {
		 return new DoctorResource(uriInfo, request, id, service, mediaType);
	 }
	 
//	@GET
//	@Path("person")
//	@Produces( MediaType.APPLICATION_JSON )
//	public Response getPeopleList() {
//		System.out.println("getPeopleList: Getting list of person...");
//		return Response.ok(people.getPeopleList()).build();
//	}
//    
//	@POST
//	@Path("person")
//	@Produces(MediaType.APPLICATION_JSON)
//    @Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})  
//    public Response createPerson(Person person) throws IOException {
//		System.out.println("New Person: "+person.getFirstname()+" "+person.getLastname());
//        System.out.println("createPerson: Creating new person...");
//        int id = this.people.createPerson(person);
//        if(id == -1)
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();
//        else
//        	return Response.status(Response.Status.CREATED).entity(id).build();
//    }
//	
//	 /**
//     * returns the number of people to get the total number of records
//     * @return a string representing the number of people
//     */
//    @GET
//    @Path("person/count")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String getCount() {
//        System.out.println("getCount: Getting count...");
//        List<Person> people = this.people.getPeopleList();
//        int count = people.size();
//        return String.valueOf(count);
//    }
//    
//    /** Defines that the next path parameter after the base url is
//    * treated as a parameter and passed to the PersonResources
//    * Allows to type http://localhost:599/base_url/1
//    * 1 will be treaded as parameter todo and passed to PersonResource
//    */
//    @Path("person/{personId}")
//    public PersonResource getPerson(@PathParam("personId") int id) {
//        return new PersonResource(uriInfo, request, id, people);
//    }
//    
//    //***********************Doctor***********************
//    
//    @POST
//	@Path("doctor")
//	@Produces(MediaType.APPLICATION_JSON)
//    @Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})  
//    public Response createDoctor(Doctor doctor) throws IOException {
//		System.out.println("New Doctor: "+doctor.getFirstname()+" "+doctor.getLastname());
//        System.out.println("createDoctor: Creating new doctor...");
//        int id = this.people.createDoctor(doctor);
//        if(id == -1)
//        	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//    				.entity("Error in LocalDatabaseService").build();
//        else
//        	return Response.status(Response.Status.CREATED).entity(id).build();
//    }
//    
//    @Path("doctor/{doctorId}")
//    public DoctorResource getDoctor(@PathParam("doctorId") int id) {
//        return new DoctorResource(uriInfo, request, id, people);
//    }
//    
//    //***********************MeasureDefinition***********************
//    
//    @GET
//    @Path("measureDefinition")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<MeasureDefinition> getMeasureDefinition() {
//        System.out.println("getMeasureDefinition: Reading measure definitions...");
//        List<MeasureDefinition> result = this.people.getMeasureDefinition();
//        return result;
//    }
}