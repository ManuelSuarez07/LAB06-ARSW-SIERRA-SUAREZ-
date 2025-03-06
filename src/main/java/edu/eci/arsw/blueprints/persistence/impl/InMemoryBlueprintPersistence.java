package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public final class InMemoryBlueprintPersistence implements BlueprintsPersistence {
    private final Map<String, Set<Blueprint>> blueprints = new HashMap<>();

    public InMemoryBlueprintPersistence() {
        Point[] points1 = {new Point(0, 0), new Point(10, 10)};
        Point[] points2 = {new Point(5, 5), new Point(20, 20)};
        Point[] points3 = {new Point(15, 15), new Point(30, 30)};

        Blueprint bp1 = new Blueprint("Sierra", "Blueprint1", points1);
        Blueprint bp2 = new Blueprint("Suarez", "Blueprint2", points2);
        Blueprint bp3 = new Blueprint("Prueba", "Blueprint3", points3);

        try {
            saveBlueprint(bp1);
            saveBlueprint(bp2);
            saveBlueprint(bp3);
        } catch (BlueprintPersistenceException e) {
            
        }
    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        Set<Blueprint> authorBlueprints = blueprints.get(bp.getAuthor());
        if (authorBlueprints != null) {
            for (Blueprint existingBp : authorBlueprints) {
                if (existingBp.getName().equals(bp.getName())) {
                    throw new BlueprintPersistenceException("El blueprint '" + bp.getName() + "' ya existe.");
                }
            }
        }
        blueprints.computeIfAbsent(bp.getAuthor(), k -> new HashSet<>()).add(bp);
    }

    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        return blueprints.getOrDefault(author, Collections.emptySet())
                .stream()
                .filter(bp -> bp.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new BlueprintNotFoundException("Blueprint no encontrado"));
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        if (!blueprints.containsKey(author)) {
            throw new BlueprintNotFoundException("No existen blueprints para el autor: " + author);
        }
        return blueprints.get(author);
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        Set<Blueprint> allBlueprints = new HashSet<>();
        blueprints.values().forEach(allBlueprints::addAll);
        return allBlueprints;
    }
}