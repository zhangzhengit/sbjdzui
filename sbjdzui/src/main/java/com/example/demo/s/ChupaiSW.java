package com.example.demo.s;

import java.awt.Component;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.apache.commons.lang3.ArrayUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

/**
 *
 * 电脑调用出牌接口
 *
 * @author zhangzhen
 * @date 2020年2月6日
 *
 */
public class ChupaiSW extends SwingWorker<R<RRPU>, Void> {

	public static final int BUCHU = 11111;

	private final JPanel dPanle;

	/**
	 * 轮到出牌的ID
	 */
	private final Integer tId;

	public ChupaiSW(final JPanel dPanle, final Integer tId) {
		super();
		this.dPanle = dPanle;
		this.tId = tId;
	}

	@Override
	protected R<RRPU> doInBackground() throws Exception {
		Cache.su.setMs(Cache.ms * 100);
		Cache.su.setTId(tId);
		final String josn = JSON.toJSONString(Cache.su);
//		System.out.println("请求出牌接口,json= " + josn);
		final HttpResponse response = HttpRequest.post(J.API + "/p0").body(josn).execute();
		final String body = response.body();
//		System.out.println("请求出牌接口,body= " + body);
		final R<RRPU> r = JSON.parseObject(body, new TypeReference<R<RRPU>>() {
		});
		return r;
	}

	@Override
	protected void done() {
		try {
			updateDN1();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void updateDN1() throws InterruptedException, ExecutionException {
		final R<RRPU> r = get();
		final Component[] cs = dPanle.getComponents();

		final RRPU dat = r.getData();
		final PX px = dat.getPx();
		final List<UR> urList = Cache.su.getUrList();
		final UR pur = urList.stream().filter(ur -> ur.getId().equals(tId)).findAny().get();
		if (px == null) {
			pur.setPreviousCArray(null);
			clear(cs, dPanle);
			dPanle.remove(cs.length - 1);
			final ImageIcon readIcon = ChushihuaSW.readIcon(BUCHU);
			dPanle.add(new JLabel(readIcon));
			dPanle.updateUI();
			beep();
			return;
		}
		final int[] array = px.getCArray();

		// 更新牌
		pur.setPreviousCArray(array);
		final int[] ca2 = AR.remove(pur.getcArray(), array);
		pur.setcArray(ca2);

		clear(cs, dPanle);
		dPanle.updateUI();

		// 出掉的牌移动到最后面
		for (final Component component : cs) {
			if (!(component instanceof JButton)) {
				continue;
			}
			final String name = component.getName();
			for (final int i : array) {
				if (name.equals(String.valueOf(i))) {
					dPanle.remove(component);
					dPanle.add(component);
				}
			}
			dPanle.updateUI();
		}
		beep();
		if (ArrayUtils.isEmpty(pur.getcArray())) {
			JOptionPane.showMessageDialog(dPanle, "农民胜利！");
		}
	}

	private void beep() {
		dPanle.getToolkit().beep();
	}

	public static void clear(final Component[] cs, final JPanel dPanle) {
		// 从前往后，从第一个不是button的全设为JLable
		int e = 0;
		while (e < cs.length && cs[e] instanceof JButton) {
			e++;
		}

		for (int i = e; i < cs.length; i++) {
			dPanle.remove(cs[i]);
		}
		for (int i = e; i < cs.length; i++) {
			dPanle.add(new JLabel());
		}
	}


}
