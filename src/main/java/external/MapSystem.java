package external;

import com.graphhopper.ResponsePath;
import com.graphhopper.util.Translation;
import com.graphhopper.util.shapes.GHPoint;

import model.TransportMode;

public interface MapSystem extends AutoCloseable {
    GHPoint convertToCoordinates(String address);

    boolean isPointWithinMapBounds(GHPoint point);

    ResponsePath routeBetweenPoints(TransportMode mode, GHPoint start, GHPoint end);

    Translation getTranslation();
}