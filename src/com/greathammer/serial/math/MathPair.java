package com.greathammer.serial.math;

/**
 * 数学Pair.
 * 
 * @author Zheng Chao
 * @version 0.1.2
 * @param <T>
 *            First Value
 * @param <S>
 *            Second Value
 */
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

	@Override
	public String toString() {
		return "First is " + FIRST + "; Second is " + SECOND;
	}

}
