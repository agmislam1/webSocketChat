package edu.baylor.cs.se.hibernate.services;

import edu.baylor.cs.se.hibernate.model.*;
import edu.baylor.cs.se.hibernate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

import static java.util.stream.Collectors.groupingBy;

//Spring annotations, feel free to ignore it
@Repository
@Transactional
public class MyRepository {



    @Autowired
    socketRepo repo;




    public List<WebSocketData>  getallUser(){
        return (List<WebSocketData>) repo.findAll();



    }

}
