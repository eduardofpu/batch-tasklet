package com.tasklet.job;
import com.tasklet.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
@Slf4j
public class PersonItemProcessor implements ItemProcessor<Person, Person>{


    //Processamento linha por linha do arquivo csv
    @Override
    public Person process(Person person) throws Exception {

        String greeting = "Hello " + person.getFirstName() + " " + person.getLastName() + "!";
        log.info("converting '{}' into '{}'", person, greeting);
        return person;
    }
}