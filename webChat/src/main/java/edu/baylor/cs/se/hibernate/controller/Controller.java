package edu.baylor.cs.se.hibernate.controller;

import edu.baylor.cs.se.hibernate.model.WebSocketData;
import edu.baylor.cs.se.hibernate.repository.socketRepo;
import edu.baylor.cs.se.hibernate.services.MyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//Ignore this as it is Spring and not Java EE (Jax-RS) controller
@RestController
@CrossOrigin("http://localhost:3010")
//@RequestMapping(value = "/heroes")

public class Controller {


    private MyRepository repository;
    //private MyRepository repo;

    @Autowired
    public Controller(MyRepository repository){
        this.repository = repository;
    }

    @RequestMapping(value = "/getall",method = RequestMethod.GET)
    public  ResponseEntity<WebSocketData>  getall(){

        //MyRepository repository = new MyRepository(repo);

        return new ResponseEntity(repository.getallUser(),HttpStatus.OK);

    }

}
