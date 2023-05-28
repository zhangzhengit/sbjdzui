package com.example.demo.s;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author zhangzhen
 * @date 2020年2月6日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SU implements Serializable {

	private static final long serialVersionUID = 8565683706334724255L;
	private int ms = 1000 * 2;
	private String pId;
	private Integer tId;
	private List<UR> urList;

}
