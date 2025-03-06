package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import java.util.Set;

public interface BlueprintsPersistence {
    
    /**
     * Guarda un nuevo blueprint.
     * @param bp el nuevo blueprint
     * @throws BlueprintPersistenceException si ya existe un blueprint con el mismo nombre
     */
    void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;
    
    /**
     * Obtiene un blueprint específico por autor y nombre.
     * @param author autor del blueprint
     * @param bprintname nombre del blueprint
     * @return el blueprint correspondiente
     * @throws BlueprintNotFoundException si no se encuentra el blueprint
     */
    Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException;

    /**
     * Obtiene todos los blueprints almacenados.
     * @return un conjunto con todos los blueprints
     */
    Set<Blueprint> getAllBlueprints();

    /**
     * Obtiene todos los blueprints de un autor específico.
     * @param author autor de los blueprints
     * @return conjunto de blueprints del autor
     * @throws BlueprintNotFoundException si el autor no tiene blueprints registrados
     */
    Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;
}
