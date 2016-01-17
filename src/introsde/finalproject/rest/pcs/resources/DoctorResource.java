package introsde.finalproject.rest.pcs.resources;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;


import com.fasterxml.jackson.core.JsonFactory;
import com.mongodb.util.JSON;

import introsde.finalproject.rest.generated.DoctorType;

import introsde.finalproject.rest.generated.ListMeasureType;
import introsde.finalproject.rest.generated.MeasureType;
import introsde.finalproject.rest.generated.ReminderType;

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
    
    
    private String creationReminderError(Exception e){
    	return "{ \n \"error\" : \"Error in Process Centric Services creating reminder due to the exception: "+e+"\"}";
    }
    
    
	private String blsErrorMessage(String e){
    	return "{ \n \"error\" : \"Error in external throught SS, due to the exception: "+e+"\"}";
    }
    
	
	private String ssErrorMessage(String e){
    	return "{ \n \"error\" : \"Error in external throught BLS, due to the exception: "+e+"\"}";
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
	@Path("/person/{personId}")
	@Produces( MediaType.APPLICATION_JSON )
	public Response checkPatient(@PathParam("personId") String personId) {

		System.out.println("checkPatient: Start checking idPerson "+ personId +"...");
		String path = "person/"+personId;
		//Response response_getCurrentHealth = serviceBLS.path(path).request().accept(mediaType).get(Response.class);
		//PersonType p = response_getCurrentHealth.readEntity(PersonType.class);

		Response response_currentHealth = serviceBLS.path(path+"/currentHealth").request().accept(mediaType).get(Response.class);
		System.out.println("Response currentHealth: " + response_currentHealth);
		ListMeasureType currentHealth = response_currentHealth.readEntity(ListMeasureType.class);
		List<MeasureType> listMeasures = currentHealth.getMeasure();
		
		
		
		/*
		currentHealth.getMeasure();
		List<MeasureType> listMeasures = currentHealth.getMeasure();
		listMeasures.get(0).getIdMeasure();
		listMeasures.get(0).getValue();
		listMeasures.get(0).getMeasureDefinition().getEndValue();
		listMeasures.get(0).getMeasureDefinition().getStartValue();
		 */

		if(!listMeasures.isEmpty()){

			List<Boolean> z = new ArrayList<Boolean>();
			
			System.out.println("In !listMeasures.isEmpty() , value: " + !listMeasures.isEmpty());


			JSONObject jsonCheck = new JSONObject();

			int idMeasureDefinition;
			int value;
			int endValue;
			int startValue;
			int index;
			String measure_name = "";
			boolean min_good = false;
			boolean max_good = false;
			String min_message = "";
			String max_message = "";



			for(int b=0; b<listMeasures.size();b++){
				idMeasureDefinition = listMeasures.get(b).getMeasureDefinition().getIdMeasureDef().intValue();

				value =  Integer.parseInt(listMeasures.get(b).getValue());

				endValue = Integer.parseInt(listMeasures.get(b).getMeasureDefinition().getEndValue());

				startValue = Integer.parseInt(listMeasures.get(b).getMeasureDefinition().getStartValue());

				String path_check = "person/"+personId+"/check/"+idMeasureDefinition+"/"+value+"/"+endValue+"/"+startValue;
				

				Response response_checkVitalSigns = serviceBLS.path(path_check).request().accept(mediaType).get(Response.class);
				z.add(response_checkVitalSigns.readEntity(Boolean.class));
				System.out.println("Response checkVitalSigns: " + response_checkVitalSigns);
			}

			for(int i=0; i<z.size(); i++){
				JSONObject value_measure = new JSONObject();
				if(z.get(i)){
					value_measure.put(listMeasures.get(i).getMeasureDefinition().getMeasureName(), "OK");
					value_measure.put("Value", listMeasures.get(i).getValue());
					jsonCheck.put(listMeasures.get(i).getMeasureDefinition().getMeasureName(), value_measure);
					
					System.out.println("Jsoncheck in z.get(i): " + jsonCheck.toString());
				}else{
					value_measure.put(listMeasures.get(i).getMeasureDefinition().getMeasureName(), "BAD");
					value_measure.put("Value", listMeasures.get(i).getValue());
					jsonCheck.put(listMeasures.get(i).getMeasureDefinition().getMeasureName(), value_measure);
					System.out.println("Jsoncheck else z.get(i) : " + jsonCheck.toString());
					
					String motivation_path = "person/"+personId;
					String motivation_phrase = getMotivationPhrase(motivation_path);
					System.out.println("Phrase to add in the Reminder: " + motivation_phrase);
					
					String reminder_text = "The " + listMeasures.get(i).getMeasureDefinition().getMeasureName() + " is not good !!! - " + motivation_phrase;
					
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal_created = Calendar.getInstance();
					//System.out.println(dateFormat.format(cal.getTime()));
					String date_created = dateFormat.format(cal_created.getTime());
					//System.out.println(date_created.getClass().getName());

					int years = cal_created.get(Calendar.YEAR);
					int days = cal_created.get(Calendar.DAY_OF_MONTH);
					int month = cal_created.get(Calendar.MONTH);

					//Calendar for expired date with the days setted to 5 days after respect to the creation
					Calendar cal_expired =  cal_created;
					//update a date
					int days_expired = days+5;
					cal_expired.set(years, month, days_expired);
					String date_expired = dateFormat.format(cal_expired.getTime());

					int relevance_value = 3;
					BigInteger relevance = BigInteger.valueOf(relevance_value);

					
					
					
					ReminderType quote_reminder = new ReminderType();
					quote_reminder.setAutocreate(true);
					quote_reminder.setCreateReminder(date_created);
					quote_reminder.setExpireReminder(date_expired);
					quote_reminder.setRelevanceLevel(relevance);
					quote_reminder.setText(reminder_text);
					
					


					System.out.println("Create reminder: " + quote_reminder.getCreateReminder());
					System.out.println("Expire reminder: " + quote_reminder.getExpireReminder());
					System.out.println("Relevance reminder: " + quote_reminder.getRelevanceLevel());
					System.out.println("text reminder: " + quote_reminder.getText());
					System.out.println("Object quote_reminder: " + quote_reminder.toString());

					try{
						System.out.println("insert New Reminder for person "+ personId);
						Response response = serviceBLS.path("person/"+personId+"/reminder").request( MediaType.TEXT_PLAIN)
								.post(Entity.entity(quote_reminder, mediaType), Response.class);
						System.out.println(response);
						
						if(response.getStatus() != 200){
					    	System.out.println("BLS Error response.getStatus() != 200  ");
					     return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
									.entity(blsErrorMessage(response.toString())).build();
					     }else{
					    	 String x = response.readEntity(String.class);
					    	 Response.ok(x).build();
					     }
					    }catch(Exception e){
					    	System.out.println("PCS Error catch creating post reminder response.getStatus() != 200  ");
					    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
							.entity(errorMessage(e)).build();
					    }
					
				}
			}
			System.out.println("Jsoncheck return: " + jsonCheck.toString());
			return Response.ok(jsonCheck.toString()).build();
		}else{
			System.out.println("There are no measures for the personId: "+ personId);
			JSONObject jsonCheckEmpty = new JSONObject();
			jsonCheckEmpty.put("Response", "No measure saved for "+personId +" personId" );
			return Response.ok(jsonCheckEmpty.toString()).build();
		}
		
	}

	private Object errorMessage(Exception e) {
		System.out.println("Error creating the reminder in PCS");
		return e;
	}
	
	
	/**
	 * Returns a motivation phrase
	 * Calls one time the BLS
	 * @return String
	 */
	private String getMotivationPhrase(String path) {
		Response response = serviceBLS.path(path+"/motivation").request().accept(MediaType.TEXT_PLAIN).get(Response.class);
		System.out.println(response);
		return response.readEntity(String.class);
	}
	
	

}

