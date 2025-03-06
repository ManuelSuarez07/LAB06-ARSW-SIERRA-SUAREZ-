package edu.eci.arsw.blueprints.Filter;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("Subsampling")
public class SubsamplingFilter implements BlueprintFilter {

    @Override
    public void filter(Blueprint bp) {
        List<Point> originalPoints = new ArrayList<>(bp.getPoints()); 
        List<Point> filteredPoints = new ArrayList<>();

        for (int i = 0; i < originalPoints.size(); i += 2) { 
            filteredPoints.add(originalPoints.get(i)); 
        }

        try {
            bp.getPoints().clear();
            bp.getPoints().addAll(filteredPoints);
        } catch (UnsupportedOperationException e) {
            System.out.println("⚠️ La lista de puntos es inmutable, considerar modificar Blueprint.");
        }
    }
}
