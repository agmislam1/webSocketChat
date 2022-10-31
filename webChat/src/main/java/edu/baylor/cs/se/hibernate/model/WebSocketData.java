package edu.baylor.cs.se.hibernate.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class WebSocketData implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String session;

    @Column
    private String username;

    @Column
    private String status;



    public void setUsername(String username){this.username=username;}
    public void setStatus(String status){this.status=status;}
    public void setSession(String session){this.session=session;}

    public String getUsername(){return  this.username;}
    public String getStatus(){return this.status;}
    public String getSession(){return this.session;}




}

