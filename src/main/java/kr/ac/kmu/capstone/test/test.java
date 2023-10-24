package kr.ac.kmu.capstone.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    @Autowired
    private ExampleRepository exampleRepository;

    @GetMapping("Hello.World")
    public String testHelloWorld() {

        exampleRepository.save(new Example());

        Example example = exampleRepository.findById(0); // SELECT where ID = 0

        return "Hello World!";
    }
    
    @GetMapping("CD_CI_Test")
    public String cd_ci() {
        return "this is CD CI Test.";
    }
}
