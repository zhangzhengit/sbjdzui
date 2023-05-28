package com.example.demo.s;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * 牌型
 *
 * @author zhangzhen
 * @date 2020年2月6日
 *
 */
// TODO 共有的类放一个项目，依赖进来
@Data
@NoArgsConstructor
public class PX implements Serializable {

	private static final long serialVersionUID = -2741019952935121349L;

	/**
	 * 牌型
	 */
	private PXE pxe;

	/**
	 * 组成这个牌型的单牌
	 */
	private int[] cArray;

	/**
	 * 这个牌型里有几张单牌
	 *
	 * @return
	 */
	public int size() {
		return cArray.length;
	}

	/**
	 * 牌型分数，用于比较两个牌型的大小
	 *
	 * @return
	 */
	public int score() {
		// 王炸int.maxvalue，没有牌要得起
		if (pxe == PXE.Wangzha) {
			return Integer.MAX_VALUE;
		}

		int s = 0;
	 	int index = cArray.length;
	 	// 有带牌的牌型，不算带的牌的分数
		switch (pxe) {
		case Sidaier:
			index = 4;
			break;

		case Sandaiyi:
		case Sandaier:
			index = 3;
			break;

		case Feijichibang:
			index = size() / 5 * 3;
			break;

		default:
			break;
		}

		// 分数按每个单牌值/10累加
		for (int i = 0; i < index; i++) {
			final int c = cArray[i];
			s += c / 10;
		}

		// 炸弹，分数扩大
		if (pxe == PXE.Zhadan) {
			s <<= 10;
		}
		return s;
	}

}
