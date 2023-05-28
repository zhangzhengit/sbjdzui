package com.example.demo.s;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

/**
 * 调用初始化接口，初始化三个玩家的牌
 *
 * @author zhangzhen
 * @date 2020年2月6日
 *
 */
public class ChushihuaSW extends SwingWorker<R<SU>, String> {

	private final JPanel zPanel;

	public ChushihuaSW(final JPanel zPanel) {
		super();
		this.zPanel = zPanel;
	}

	@Override
	protected R<SU> doInBackground() throws Exception {
		final HttpResponse response = HttpRequest.get(J.API + "/s").execute();
		final String body = response.body();
		final R<SU> r = JSON.parseObject(body, new TypeReference<R<SU>>() {});
		Cache.su = r.getData();
		return r;
	}

	@Override
	protected void done() {
		try {
			final R<SU> r = get();
			final SU su = r.getData();
			final List<UR> urList = su.getUrList();
			final Component[] cs = zPanel.getComponents();
			addPAI(urList, cs, 1, "wo");
			addPAI(urList, cs, 2, "zuoshang");
			addPAI(urList, cs, 3, "youshang");
			addListerner();
			zPanel.updateUI();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		super.done();
	}

	private void addPAI(final List<UR> urList, final Component[] cs, final int id, final String cName) {
		final Optional<UR> urO = urList.stream().filter(ur -> ur.getId().equals(id)).findAny();

		final UR dizhuur = urO.get();
		final Optional<Component> findAny = Arrays.stream(cs).filter(c -> c.getName().equals(cName)).findAny();
		final Component dizhuc = findAny.get();
		final JPanel dizhuP = (JPanel) dizhuc;
		final int[] dizhuCA = dizhuur.getcArray();
		final int geshu = dizhuCA.length;
		for (final int i : dizhuCA) {
			final JButton bb = new JButton(readIcon(i));
			bb.setName(String.valueOf(i));
			bb.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
			dizhuP.add(bb);
		}

		final int buch = J.MAX_C - geshu;
		for (int i = 0; i < buch; i++) {
			dizhuP.add(new JLabel());
		}

		Arrays.stream(cs).filter(c -> c instanceof JButton).map(c -> (JLabel) c)
				.forEach(label -> label.setBounds(1, 1, 54, 30));

	}

	public static ImageIcon readIcon(final int k) {
		final URL url = ChushihuaSW.class.getClassLoader().getResource("icon/" + k + ".jpg");
		final ImageIcon imageIcon = new ImageIcon(url);
		final Image scaledInstance = imageIcon.getImage().getScaledInstance(70, 158, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledInstance);
	}

	private void addListerner() {

		final Component[] cs = zPanel.getComponents();
		final Component woc = Arrays.stream(cs).filter(c -> c.getName().equals("wo")).findAny().get();
		final JPanel woPanel = (JPanel) woc;
		final Component[] wopcs = woPanel.getComponents();
		for (final Component component : wopcs) {
			if (!(component instanceof JButton)) {
				continue;
			}
			final JButton button = (JButton) component;
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					final Component ec = e.getComponent();
					final JButton button = (JButton) ec;
					final String name = button.getName();
					final boolean contains = J.c.contains(name);
					if (contains) {
						button.setBorder(BorderFactory.createEmptyBorder());
						J.c.remove(name);
					} else {
//						button.setBorder(BorderFactory.createTitledBorder("✔"));
						button.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
						J.c.add(name);
					}
				};
			});
		}
	}
}
