package com.example.demo.s;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * 玩家出牌的最终结果
 *
 * @author zhangzhen
 * @date 2020年2月6日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RRPU implements Serializable {

	private static final long serialVersionUID = 7694045688852883046L;

	/**
	 * 玩家ID
	 */
	private int id;

	/**
	 * 玩家角色
	 */
	private URE ure;

	/**
	 * 最后要出的牌型
	 */
	private PX px;

	/**
	 * 总局数
	 */
	private long gc;

	/**
	 * 赢局数
	 */
	private long wc;

	/**
	 * 胜率
	 *
	 * @return
	 */
	public double getWR() {
		if (gc <= 0 || wc <= 0) {
			return 0;
		}
		return (double) wc / gc;
	}

	@Override
	public String toString() {
		return "RRPU [id=" + id + ", ure=" + ure + ", gc=" + gc + ", wc=" + wc + ", wr=" + getWR() + ", px=" + px + "]";
	}

}
