package mongo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectionController {

    @GetMapping(path = "/live")
    public ResponseEntity<String> live() {
        return new ResponseEntity<>("im live now :)", HttpStatus.OK);
    }
}
