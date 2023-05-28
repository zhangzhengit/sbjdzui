package com.example.demo.s;

import java.util.EnumSet;

import com.google.common.collect.ImmutableList;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 牌型枚举
 *
 * @author zhangzhen
 * @date 2020年2月6日
 *
 */
@Getter
@AllArgsConstructor
public enum PXE {

	Dan(1,"单张"),
	Duzi(2,"对子"),
	Sanzhang(3,"三张"),
	Zhadan(4,"炸弹"),
	Feiji(5,"飞机"),
	Sandaiyi(6,"三带一"),
	Sandaier(7,"三带对子"),
	Liandui(8,"连对"),
	Sidaier(9,"四带对子"),
	Feijichibang(10,"飞机带翅膀"),
	Wangzha(11,"王炸"),
	Shunzi(12,"顺子"),

	// TODO 四带两个对子牌型还没写
	// TODO 可以自定义牌型，具体生成规则在 G里写

	;

	private int index;
	private String v;


	/**
	 * 牌型中单牌张数相同才能比较大小的牌型
	 */
	public static final EnumSet<PXE> geshuxiangtong = EnumSet.of(PXE.Feiji, PXE.Feijichibang, PXE.Wangzha, PXE.Liandui);

	public static final ImmutableList<PXE> VL = ImmutableList.copyOf(PXE.values());
}
