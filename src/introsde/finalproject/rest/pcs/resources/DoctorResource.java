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
    int idPerson;
    private WebTarget service = null;
    private String mediaType = null;


    public DoctorResource(UriInfo uriInfo, Request request,int id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idPerson = id;
    }

    public DoctorResource(UriInfo uriInfo, Request request,int id, WebTarget service, String mediatype) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.idPerson = id;
        this.service = service;
        this.mediaType = mediatype;
    }
}