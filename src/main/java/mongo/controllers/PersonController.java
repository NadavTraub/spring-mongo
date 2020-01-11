package mongo.controllers;

import mongo.entities.Person;
import mongo.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mongo.repositories.PersonRepository;
import mongo.services.SequenceGeneratorService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping
@RestController
public class PersonController {

    private PersonRepository personRepository;

    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public PersonController(PersonRepository personRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.personRepository = personRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @GetMapping(path = "/persons")
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @GetMapping(path = "/persons/{id}")
    public ResponseEntity< Person > getPersonById(@PathVariable(value = "id") Long personId)
            throws ResourceNotFoundException {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + personId));
        return ResponseEntity.ok().body(person);
    }

    @PostMapping(path = "/persons")
    public Person createPerson(@Valid @RequestBody Person person) {
        person.setId(sequenceGeneratorService.generateSequence(Person.SEQUENCE_NAME));
        return personRepository.save(person);
    }

    @PutMapping(path = "/persons/{id}")
    public ResponseEntity < Person > updatePerson(@PathVariable(value = "id") Long personId,
                                                      @Valid @RequestBody Person personDetails) throws ResourceNotFoundException {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + personId));

        person.setEmailId(personDetails.getEmailId());
        person.setLastName(personDetails.getLastName());
        person.setFirstName(personDetails.getFirstName());
        final Person updatedPerson = personRepository.save(person);
        return ResponseEntity.ok(updatedPerson);
    }

    @DeleteMapping(path = "/persons/{id}")
    public Map< String, Boolean > deletePerson(@PathVariable(value = "id") Long personId)
            throws ResourceNotFoundException {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + personId));

        personRepository.delete(person);
        Map < String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
