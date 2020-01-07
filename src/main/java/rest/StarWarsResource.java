package rest;

import dto.StarWarsDTO;
import facades.ApiFacade;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * @author lam@cphbusiness.dk
 */
@Path("sw")
public class StarWarsResource {
    
    ApiFacade facade = ApiFacade.getApiFacade();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello star wars\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("data")
    @RolesAllowed({"user", "admin"})
    public List<String> getData() throws IOException, ProtocolException, ExecutionException, InterruptedException {
        List<String> data = facade.getAllDataInParallelWithQueue();
        return data;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("datadto")
    @RolesAllowed({"user", "admin"})
    public List<StarWarsDTO> getDataDTO() throws IOException, ProtocolException, ExecutionException, InterruptedException {
        List<StarWarsDTO> data = facade.getAllDataInParallelWithQueueAndDTO();
        return data;
    }

}
