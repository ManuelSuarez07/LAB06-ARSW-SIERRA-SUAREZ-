package edu.eci.arsw.blueprints;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

@SpringBootApplication
public class BlueprintsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlueprintsApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(BlueprintsServices blueprintsServices) {
        return args -> {

            Blueprint bp1 = new Blueprint("Manuel", "Plano1", new Point[]{new Point(10, 10), new Point(10, 10), new Point(20, 20), new Point(20, 20), new Point(30, 30)});
            Blueprint bp2 = new Blueprint("Yeka", "Plano2", new Point[]{new Point(30, 30), new Point(40, 40)});

            blueprintsServices.addNewBlueprint(bp1);
            blueprintsServices.addNewBlueprint(bp2);

            System.out.println("Después de agregar: " + blueprintsServices.getAllBlueprints());

            // Consultar planos
            System.out.println("Todos los planos: " + blueprintsServices.getAllBlueprints());
            System.out.println("Planos de Manuel: " + blueprintsServices.getBlueprintsByAuthor("Manuel"));
            System.out.println("Plano específico: " + blueprintsServices.getBlueprint("Manuel", "Plano1"));
        };
    }
}