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

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class DoctorResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int idDoctor;
    private WebTarget serviceBLS = null;
	private WebTarget serviceSS = null;
    private String mediaType = null;
    private String path = null;


    public DoctorResource(UriInfo uriInfo, Request request,int id, String mediaType2) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idDoctor = id;
        this.path = "doctor/"+this.idDoctor;
    }

    public DoctorResource(UriInfo uriInfo, Request request,int id, WebTarget serviceBLS, WebTarget serviceSS, String mediatype) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idDoctor = id;
        this.serviceBLS = serviceBLS;
        this.serviceSS = serviceSS;
        this.mediaType = mediatype;
        this.path = "doctor/"+this.idDoctor;
    }
    
    
    /**
     * II° Integration Logic: checkPatient(idUser)
     * 		BLS getCurrentHealth()
     * 		BLS checkVitalSigns()
     * 		SS getMotiviationPhrase() (the phrase changes based on the result of checkVitalSigns)
	 * 		BLS setReminder()
     * @return
     */
    @GET
   	@Path("/person/{personId}/")
   	@Produces( MediaType.APPLICATION_JSON )
   	public Response checkPatient(@PathParam("personId") String personId) {
   		System.out.println("checkPatient: Start checking idPerson "+ personId +"...");
   		return null;
   	}
}