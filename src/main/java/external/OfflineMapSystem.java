package external;

import java.io.Serializable;
import java.util.Locale;

import com.graphhopper.*;
import com.graphhopper.config.*;
import com.graphhopper.util.*;
import com.graphhopper.util.shapes.*;

import model.TransportMode;

public class OfflineMapSystem implements MapSystem, Serializable {
  private transient GraphHopper hopper;

  public OfflineMapSystem() {
    hopper = new GraphHopper();

    hopper.setOSMFile("scotland-latest.osm.pbf");
    hopper.setGraphHopperLocation(".graphhopper");

    hopper.setProfiles(
        new Profile(TransportMode.CAR.getProfileName()).setVehicle("car").setWeighting("fastest").setTurnCosts(true),
        new Profile(TransportMode.BIKE.getProfileName()).setVehicle("bike").setWeighting("fastest"),
        new Profile(TransportMode.FOOT.getProfileName()).setVehicle("foot").setWeighting("fastest"),
        new Profile(TransportMode.WHEELCHAIR.getProfileName()).setVehicle("wheelchair").setWeighting("fastest"));

    // Enable speed mode for each profile
    hopper.getCHPreparationHandler().setCHProfiles(
        new CHProfile(TransportMode.CAR.getProfileName()),
        new CHProfile(TransportMode.BIKE.getProfileName()),
        new CHProfile(TransportMode.FOOT.getProfileName()),
        new CHProfile(TransportMode.WHEELCHAIR.getProfileName()));

    hopper.importOrLoad();
  }

  public OfflineMapSystem(OfflineMapSystem other) {
    this.hopper = other.hopper;
  }

  public GHPoint convertToCoordinates(String coordinatesString) {
    String[] splitCoords = coordinatesString.split(" ");

    Double lat;
    Double lon;
    try {
      lat = Double.parseDouble(splitCoords[0]);
      lon = Double.parseDouble(splitCoords[1]);
    } catch (NumberFormatException e) {
      throw new RuntimeException("Invalid coordinates");
    }

    if (lat == null | lon == null) {
      throw new RuntimeException("Invalid coordinates");
    }

    return new GHPoint(lat, lon);
  }

  public boolean isPointWithinMapBounds(GHPoint point) {
    return hopper.getBaseGraph().getBounds().contains(point.lat, point.lon);
  }

  public ResponsePath routeBetweenPoints(TransportMode mode, GHPoint start, GHPoint end) {
    GHRequest request = new GHRequest(start, end)
        .setProfile(mode.toString())
        .setLocale(Locale.UK);

    GHResponse response = hopper.route(request);

    if (response.hasErrors())
      throw new RuntimeException(response.getErrors().toString());

    return response.getBest();
  }

  public Translation getTranslation() {
    return hopper.getTranslationMap().getWithFallBack(Locale.UK);
  }

  public void close() {
    hopper.close();
  }
}
