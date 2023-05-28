package com.example.demo.s;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * 数组操作
 *
 * @author zhangzhen
 * @date 2020年2月6日
 *
 */
public class AR {

	/**
	 * 从cArray 删除chu
	 *
	 * @param cArray
	 * @param chu
	 * @return
	 */
	static public int[] remove(final int[] cArray, final int[] chu) {
		final int dalength = chu.length;
		final int[] ddA = new int[dalength];
		for (int i = 0; i < dalength; i++) {
			ddA[i] = Arrays.binarySearch(cArray, chu[i]);
		}
		final int[] removeAll = ArrayUtils.removeAll(cArray, ddA);
		return removeAll;
	}

}
