package rest;

import dto.HobbyDTO;
import dto.PersonDTO;
import entities.Address;
import entities.Hobby;
import entities.Person;
import errorhandling.ExceptionDTO;
import utils.EMF_Creator;
import facades.PersonFacade;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;


@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/EksamTest1",
            "dev",
            "ax2",
            EMF_Creator.Strategy.CREATE);
    private static final PersonFacade FACADE = PersonFacade.getFacadeExample(EMF);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PersonDTO getPersonInfo(@PathParam("id") int personID) {
        if (FACADE.getPerson(personID) == null) {
            throw new WebApplicationException("Person not found", 404);
        } else {
            PersonDTO pDTO = FACADE.getPerson(personID);
            return pDTO;
        }
    }

//    Get information about a person (address, hobbies etc) given a phone number
    @GET
    @Path("phone/{phoneNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public PersonDTO getPersonInfoByPhoneNumber(@PathParam("phoneNumber") String phoneNumber) {
            PersonDTO pOut = FACADE.getPersonByPhoneNumber(phoneNumber);
            return pOut;
    }

//    Get all persons with a given hobby
    @GET
    @Path("hobby/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PersonDTO> getAllPersonsInfoByHobby(@PathParam("hobby") String hobby) {
        List<PersonDTO> p = new ArrayList();
        FACADE.getAllPersonsWithHobby(hobby);
        return p;
    }


    //    Get all persons with a given hobby(i.e. golf)
//    @GET
//    @Path("hobby/{hobby}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<PersonOutDTO> getPersonsInfoByHobby(@PathParam("hobby") String hobby) {
//        if (hobby != null && hobby.equals("golf")) {
//            // for test
//            List<PersonOutDTO> persons = new ArrayList<>();
//            persons.add(new PersonOutDTO(new Person("info@simonskodebiks.dk", "Gũnther", "Steiner", new Address("Street", "addInfo", new CityInfo(123, "KBH")))));
//            return persons;
//        } else {
//            // here should be something real :-)
//            List<PersonOutDTO> persons = new ArrayList<>();
//            persons.add(new PersonOutDTO(new Person("info@simonskodebiks.dk", "Gũnther", "Steiner", new Address("Street", "addInfo", new CityInfo(123, "KBH")))));
//            return persons;
//        }
//    }

    //@PUT
    public void editPersonAddress() {
    }

    @PUT
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PersonDTO editPersonCoreInformation(PersonDTO person) {
        if (person.getFirstName() == null || person.getLastName() == null || person.getEmail() == null) {
            throw new WebApplicationException("Not all required arguments included", 400);
        }

        return FACADE.editPersonCoreInfo(person);
//        //dummy data
//        PersonOutDTO p = new PersonOutDTO(new Person("info@simonskodebiks.dk", "Gũnther", "Steiner", new Address("Street", "addInfo", new CityInfo(123, "KBH"))));
//        HobbyDTO h1 = new HobbyDTO(new Hobby("fodbold", "hver tirsdag"));
//        HobbyDTO h2 = new HobbyDTO(new Hobby("fodbold", "hver onsdag"));
//        ArrayList<HobbyOutDTO> hobbies = new ArrayList<>();
//        hobbies.add(h1);
//        hobbies.add(h2);
//        p.setHobbies(hobbies);
//        return p;
    }

    //@POST
    @POST
    @Path("hobby/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public HobbyDTO createHobby(HobbyDTO hDTO) {
        return FACADE.createHobby(hDTO);
    }

    //@DELETE
    public void deleteHobby() {
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PersonDTO createPerson(PersonDTO person) {
        if (person.getFirstName() == null || person.getLastName() == null || person.getEmail() == null || person.getAddress().getStreet() == null || person.getAddress().getZip() == null) {
            throw new WebApplicationException("Not all required arguments included", 400);
        }
        return FACADE.createPerson(person.getFirstName(), person.getLastName(), person.getEmail(), person.getPhone(),
                person.getAddress().getStreet(), person.getAddress().getCity(), person.getAddress().getZip());
    }

    //@DELETE
    public void deletePerson() {
    }

    //    fill db with data
    @GET
    @Path("fill")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFilling() {
        return FACADE.fillUp();
    }

    //    empty db
    @GET
    @Path("empty")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDeleting() {
        return FACADE.emptyDB();
    }
    
}
