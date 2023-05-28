package com.example.demo.s;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 *
 * @author zhangzhen
 * @date 2020年2月6日
 *
 */
public class FP implements Serializable {

	private static final long serialVersionUID = 5497740660906284987L;

	private int[] array;

	public int[] getArray() {
		return array;
	}

	public void setArray(final int[] array) {
		this.array = array;
	}

	public FP(final int[] array) {
		super();
		this.array = array;
	}

	@Override
	public String toString() {
		return "FP [array=" + Arrays.toString(array) + "]";
	}

	public FP() {
	}

}
