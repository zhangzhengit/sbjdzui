package com.example.demo.s;

import java.io.Serializable;

/**
 *
 *
 * @author zhangzhen
 * @date 2020年2月6日
 *
 */
public class Y implements Serializable {

	private static final long serialVersionUID = -2572590394987192807L;

	/**
	 * 上家出的牌
	 */
	private int[] array1;

	/**
	 * 地主出的牌
	 */
	private int[] array2;

	public Y(final int[] array1, final int[] array2) {
		super();
		this.array1 = array1;
		this.array2 = array2;
	}

	public int[] getArray1() {
		return array1;
	}

	public void setArray1(final int[] array1) {
		this.array1 = array1;
	}

	public int[] getArray2() {
		return array2;
	}

	public void setArray2(final int[] array2) {
		this.array2 = array2;
	}

	public Y() {
	}
}
