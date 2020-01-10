package facades;

import dto.AddressDTO;
import dto.HobbyDTO;
import dto.PersonDTO;
import entities.Address;
import entities.Hobby;
import entities.Person;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;

/**
 *
 */
public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public PersonDTO getPerson(long personID) {
        EntityManager em = emf.createEntityManager();
        try {
            Person p = em.find(Person.class, personID);
            PersonDTO pDTO = new PersonDTO(p.getFirstName(), p.getLastName(), p.getPhone(), p.getEmail());
            pDTO.setId(p.getId());
            return pDTO;
        } finally {
            em.close();
        }
    }

    // Get information about a person (address, hobbies etc) given a phone number
    public PersonDTO getPersonByPhoneNumber(String phoneNumber) {
        EntityManager em = emf.createEntityManager();
        Person person = null;
        try {
            TypedQuery<Person> query
                    = (TypedQuery<Person>) em.createQuery("SELECT p FROM Person p JOIN p.phones ph WHERE ph.phoneNumber = :phoneNumber").setParameter("phoneNumber", phoneNumber);
            if (query.getResultList().size() > 0) {
                person = query.getResultList().get(0);
            } else {
                System.out.println("Couldn't find person");
            }
            PersonDTO pDTO = new PersonDTO(person.getFirstName(), person.getLastName(), person.getPhone(), person.getEmail());

            TypedQuery<HobbyDTO> queryHobby
                    = (TypedQuery<HobbyDTO>) em.createQuery("SELECT h FROM Hobby h JOIN h.persons p WHERE p.personID = :personID");
            queryHobby.setParameter("personID", person.getId());
            List<HobbyDTO> hob = queryHobby.getResultList();
            pDTO.setHobbies(hob);

            TypedQuery<AddressDTO> queryAddress
                    = (TypedQuery<AddressDTO>) em.createQuery("SELECT a FROM Address a JOIN a.persons p WHERE p.personID = :personID");
            queryHobby.setParameter("personID", person.getId());
            pDTO.setAddress(queryAddress.getSingleResult());

//            String address = "ToDogade 2"; //queryAddress.getResultList().get(0);
//            ArrayList<HobbyOutDTO> hobOUT = new ArrayList();
//            for (Hobby hobby : queryHobby.getResultList()) {
//                hobOUT.add(new HobbyOutDTO(hobby));
//            }
//            List<PersonHobbyOutDTO> results = new ArrayList();
//            results.add(pOUT);
            return pDTO;
        } finally {
            em.close();
        }
    }

    // Get all persons with a given hobby
    public void getAllPersonsWithHobby(String hobby) {

    }

//    // Get all persons living in a given city (i.e. 2800 Lyngby)
//    public List<Person> getAllPersonsWithZipCode(int zipcode) {
//        EntityManager em = emf.createEntityManager();
//        try {
//            TypedQuery<Person> query
//                    = em.createQuery("SELECT p from Person p JOIN p.address a JOIN a.cityInfo c WHERE c.zipCode = :zipCode", Person.class).setParameter("zipCode", zipcode);
//            return query.getResultList();
//        } finally {
//            em.close();
//        }
//    }

    // Get the count of people with a given hobby
    public int getCountPeopleWithHobby() {
        EntityManager em = emf.createEntityManager();
        try {
            int renameMeCount = (int) em.createQuery("SELECT COUNT(r) FROM Person r where").getSingleResult();
            return renameMeCount;
        } finally {
            em.close();
        }
    }

    // empty production database
    public String emptyDB() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return "{\"status\":\"emptied\"}";
    }

    // editPersonCoreInformation
    public PersonDTO editPersonCoreInfo(PersonDTO person) {
        EntityManager em = emf.createEntityManager();
        try {
            Person personToEdit = em.find(Person.class, person.getId());

            personToEdit.setFirstName(person.getFirstName());
            personToEdit.setLastName(person.getLastName());
            personToEdit.setEmail(person.getEmail());

            em.getTransaction().begin();
            em.merge(personToEdit);
            em.getTransaction().commit();

            return person;
        } finally {
            em.close();
        }
    }

    public String fillUp() {
        EntityManager em = emf.createEntityManager();
        Person p1, p2;
        Hobby hobby1, hobby2, hobby3;
        Address address1, address2;

        hobby1 = new Hobby("Cykling", "Cykling på hold");
        hobby2 = new Hobby("Fodbold", "Spark til bold");
        hobby3 = new Hobby("Håndbold", "Kast med bold");

//        List<HobbyDTO> hobbyList1 = new ArrayList();
//        hobbyList1.add(hobby1);
//        hobbyList1.add(hobby2);
//        List<HobbyDTO> hobbyList2 = new ArrayList();
//        hobbyList2.add(hobby2);
//        hobbyList2.add(hobby3);

        address1 = new Address("BalladeStræde", "Balladerup", "2750");
        address2 = new Address("Herlevhovedgade", "Herlev", "1234");

        try {
            p1 = new Person("Gurli", "Mogensen", "email@email.com", "44556677");
            p2 = new Person("Gunnar", "Hjorth", "mail@mail.com", "11223344");
            p1.setHobby(hobby1);
            p1.setHobby(hobby2);
            p1.setAddress(address1);
            p2.setHobby(hobby3);
            p2.setAddress(address2);
            em.getTransaction().begin();
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return "{\"status\":\"filled\"}";
    }

    public PersonDTO createPerson(String firstName, String lastName, String email, String phone, String street, String city, String zip) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Address a = new Address(street, city, zip);
            em.persist(a);

            Person p = new Person(firstName, lastName, email, phone);
            em.persist(p);
            em.getTransaction().commit();

            PersonDTO pDTO = new PersonDTO(firstName, lastName, email, phone);
            pDTO.setId(p.getId());
            return pDTO;
        } finally {
            em.close();
        }
    }

    public HobbyDTO createHobby(HobbyDTO hDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            HobbyDTO hobbyDTO = new HobbyDTO(hDTO.getName(), hDTO.getDescription());
            em.getTransaction().begin();
            em.persist(hobbyDTO);
            em.getTransaction().commit();
            return hobbyDTO;
        } finally {
            em.close();
        }
    }

//    public List<PersonDTO> getAllPersons(){
//        EntityManager em = emf.createEntityManager();
//        try {
//            TypedQuery<PersonDTO> query
//                    = em.createQuery("Select p from Person p", PersonDTO.class);
//            List<PersonDTO> persons = query.getResultList();
//            List<PersonDTO> pDTOs = new ArrayList<>();
//            for (PersonDTO person : persons) {
//                PersonDTO pDTO = new PersonDTO(person.getFirstName(), person.getLastName(), person.getEmail(), person.getPhone(), person.getId(), person.getAddress());
////                pDTO.setHobbies(hobbies);
//                pDTO.setId(person.getId());
//                pDTOs.add(pDTO);
//                
//            }
//            return pDTOs;
//        } finally {
//            em.close();
//        }
//    }
    
//    public static void main(String[] args) {
//        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
//        PersonFacade pf = PersonFacade.getFacadeExample(emf);
//        pf.fillUp();
//    }
}
