package fr.univlille.models;

public class LobbyModel {
  private boolean host_is_hunter = true;

  public void invertRoles() {
    host_is_hunter = !host_is_hunter;
  }

  public void setIsHostHunter(boolean v) {
    host_is_hunter = v;
  }

  public boolean isHostHunter() {
    return host_is_hunter;
  }
}
