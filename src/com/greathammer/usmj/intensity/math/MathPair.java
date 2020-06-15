package com.greathammer.usmj.intensity.math;

public class MathPair<T, S> {

	final private T FIRST;
	final private S SECOND;

	public MathPair(T first, S second) {
		FIRST = first;
		SECOND = second;
	}

	// public MathPair(MathPair<T, S> e) {
	// FIRST = e.getFirst();
	// SECOND = e.getSecond();
	// }

	public T getFirst() {
		return FIRST;
	}

	public S getSecond() {
		return SECOND;
	}

}
