package main.java.fr.univlille.iutinfo.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
	private List<Observer> observers;

	public Subject() {
		this.observers = new ArrayList<>();
	}

	protected void notifyObservers() {
		for (Observer o: observers) o.update(this);
	}

	protected void notifyObservers(Object data) {
		for (Observer o: observers) o.update(this, data);
	}

	public void attach(Observer observer) {
		this.observers.add(observer);
	}

	public void detach(Observer observer) {
		this.observers.remove(observer);
	}
}
