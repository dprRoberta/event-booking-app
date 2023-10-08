package model;

public enum TransportMode {
  CAR,
  BIKE,
  FOOT,
  WHEELCHAIR;

  public String getProfileName() {
    return this.toString().toLowerCase();
  }
}
