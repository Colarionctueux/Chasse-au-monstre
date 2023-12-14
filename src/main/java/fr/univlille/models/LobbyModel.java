package fr.univlille.models;

public class LobbyModel {
  // If the host is hunter,
  // then the client is monster,
  // and vice-versa
  private boolean hostIsHunter = true;

  public void invertRoles() {
    hostIsHunter = !hostIsHunter;
  }

  public void setIsHostHunter(boolean v) {
    hostIsHunter = v;
  }

  public boolean isHostHunter() {
    return hostIsHunter;
  }
}
