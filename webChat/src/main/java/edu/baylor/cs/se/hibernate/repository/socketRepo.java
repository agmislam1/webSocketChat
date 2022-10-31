package edu.baylor.cs.se.hibernate.repository;



import edu.baylor.cs.se.hibernate.model.WebSocketData;
import org.springframework.data.repository.CrudRepository;


public interface socketRepo extends CrudRepository<WebSocketData,Long> {
    public WebSocketData findBySession(String session);

}
