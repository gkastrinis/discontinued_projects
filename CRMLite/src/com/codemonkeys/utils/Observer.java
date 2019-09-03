package com.codemonkeys.utils;

import java.util.HashSet;
import java.util.Set;


public class Observer<M> {

	Set<Observable<? super M>> registered = new HashSet<>();

	public void register(Observable<? super M> o) {
		registered.add(o);
	}

	public void unregister(Observable<? super M> o) {
		registered.remove(o);
	}

	public void update(M msg) {
		for (Observable<? super M> o : registered)
			o.update(msg);
	}

}
