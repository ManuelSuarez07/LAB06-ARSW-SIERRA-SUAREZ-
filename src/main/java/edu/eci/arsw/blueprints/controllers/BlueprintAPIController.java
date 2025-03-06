package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    @Autowired
    private BlueprintsServices blueprintsServices;

    private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprints() {
        try {
            Set<Blueprint> blueprints = blueprintsServices.getAllBlueprints();
            return new ResponseEntity<>(blueprints, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error al obtener los blueprints", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{author}", method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable String author) {
        try {
            Set<Blueprint> blueprints = blueprintsServices.getBlueprintsByAuthor(author);
            return new ResponseEntity<>(blueprints, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("No se encontraron blueprints para el autor: " + author, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{author}/{bpname}", method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprintByAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            Blueprint blueprint = blueprintsServices.getBlueprint(author, bpname);
            return new ResponseEntity<>(blueprint, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("No se encontró el blueprint '" + bpname + "' para el autor: " + author, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> manejadorPostRecursoBlueprints(@RequestBody Blueprint blueprint) {
        try {
            blueprintsServices.addNewBlueprint(blueprint);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error al registrar el blueprint", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/{author}/{bpname}", method = RequestMethod.PUT)
    public ResponseEntity<?> manejadorPutRecursoBlueprints(
            @PathVariable String author,
            @PathVariable String bpname,
            @RequestBody Blueprint blueprint) {
        String key = author + "-" + bpname;
        locks.putIfAbsent(key, new Object());
        synchronized (locks.get(key)) {
            try {
                if (!blueprint.getAuthor().equals(author) || !blueprint.getName().equals(bpname)) {
                    return new ResponseEntity<>("El autor o el nombre del blueprint no coinciden", HttpStatus.BAD_REQUEST);
                }
                blueprintsServices.updateBlueprint(author, bpname, blueprint);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception ex) {
                Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
                return new ResponseEntity<>("Error al actualizar el blueprint", HttpStatus.NOT_FOUND);
            } finally {
                locks.remove(key);
            }
        }
    }
}