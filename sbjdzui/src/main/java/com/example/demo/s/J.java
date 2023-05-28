package com.example.demo.s;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

/**
 *
 * 界面，启动类
 *
 * @author zhangzhen
 * @date 2020年2月6日
 *
 */
public class J extends JFrame {

	private static final String D = "20";

	private static final long serialVersionUID = 7493187857979139978L;

	/**
	 * 牌的最大列数
	 */
	public static final int MAX_C = 21;

	/**
	 * 接口地址
	 */
	// TODO 启动时弹框输入，或放在配置里
	public static final String API = "http://localhost:8080";

	public static void main(final String[] args) throws MalformedURLException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new J();
				} catch (final MalformedURLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private final JPanel zPanel = new JPanel();
	private final JPanel zuoshang = new JPanel(new GridLayout(1, MAX_C));
	private final JPanel youshang = new JPanel(new GridLayout(1, MAX_C));
	private final JPanel wo = new JPanel(new GridLayout(1, MAX_C));
	private final JPanel anniu = new JPanel(new GridLayout(1, 3));

	private final JButton chupai = new JButton("出牌");
	private final JButton buchu = new JButton("不出");
	private final JButton quxiao = new JButton("取消");
	private final JButton tishi = new JButton("提示");

	private JLabel xinxi = null;
	// TODO 放一个文本框，记录每次出的牌

	public static final Set<String> c = new HashSet<>();


	private J() throws MalformedURLException {
		setTitle("斗地主");
		setSize(1516, 766);
		setLocation(100, 100);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


		xinxi = new JLabel("轮到地主出牌", SwingConstants.CENTER);
		xinxi.setFont(new Font("Default", Font.BOLD, 30));

		addPanel();

		addListener();

//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//				| UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}
		
		setResizable(false);
		setVisible(true);

		// 初始化
		final ChushihuaSW chushihuaSW = new ChushihuaSW(zPanel);
		chushihuaSW.execute();
		s();

	}

	void s() {
		String showInputDialog = i();
		if (StringUtils.isBlank(showInputDialog)) {
			showInputDialog = D;
		}
		final int p = p(showInputDialog);
		if (p > 100 || p <= 0) {
			s();
		} else {
			Cache.ms = p;
		}
	}

	private int p(final String showInputDialog) {
		try {
			final int n = Integer.parseInt(showInputDialog);
			return n;
		} catch (final Exception e) {
			return -1;
		}
	}

	private String i() {
		final String showInputDialog = JOptionPane.showInputDialog(wo, "电脑出牌时间(1到100)：", D);
		return showInputDialog;
	}

	private void addPanel() {
		zPanel.setLayout(new GridLayout(4, 1));

		zuoshang.setBorder(BorderFactory.createTitledBorder("农民一（电脑）"));
		zuoshang.setBackground(new Color(106, 133, 196));
		zuoshang.setName("zuoshang");
		zPanel.add(zuoshang);

		youshang.setBackground(new Color(106, 196, 166));
		youshang.setBorder(BorderFactory.createTitledBorder("农民二（电脑）"));
		youshang.setName("youshang");
		zPanel.add(youshang);

		wo.setBackground(Color.PINK);
		wo.setBorder(BorderFactory.createTitledBorder("地主（玩家）"));
		wo.setName("wo");
		zPanel.add(wo);

		anniu.setBackground(Color.WHITE);
		anniu.setBorder(BorderFactory.createTitledBorder("操作"));
		anniu.add(chupai);
		anniu.add(buchu);
		anniu.add(xinxi);
		anniu.add(quxiao);
		anniu.add(tishi);
		zPanel.add(anniu);

		getContentPane().add(zPanel);

	}

	private void addListener() {
		// 出牌
		chupai.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (!e.getComponent().isEnabled()) {
					return;
				}
				final Component b = e.getComponent();
				final JButton button = (JButton) b;
				if (!button.isEnabled()) {
					return;
				}
				if (c.isEmpty()) {
					return;
				}


				final int[] a = new int[c.size()];
				int k = 0;
				for (final String kx : c) {
					a[k++] = Integer.parseInt(kx);
				}
				Arrays.sort(a);
				// 判定选中的牌是否符合牌型规则
				final boolean ok = f(a);
				if (!ok) {
					JOptionPane.showMessageDialog(wo, "不符合牌型规则！");
					return;
				}
				final boolean y = y(a);
				if (!y) {
					JOptionPane.showMessageDialog(wo, "要不起农民的牌！");
					return;
				}

				final List<UR> urList = Cache.su.getUrList();
				final UR w = urList.stream().filter(ur -> ur.getUre().equals("L")).findAny().get();
				w.setPreviousCArray(a);
				final int[] ca = w.getcArray();
				final int[] ddA = new int[c.size()];
				int n = 0;
				for (final int i : a) {
					ddA[n++] = Arrays.binarySearch(ca, i);
				}
				final int[] removeAll = ArrayUtils.removeAll(ca, ddA);
				w.setcArray(removeAll);

				// 出的牌放到最后
				final Component[] cs = wo.getComponents();
				ChupaiSW.clear(cs, wo);
				for (final int k1 : a) {
					for (final Component component : cs) {
						if (!(component instanceof JButton)) {
							continue;
						}
						if (component.getName().equals(String.valueOf(k1))) {
							wo.remove(component);
							wo.add(component);
						}
					}
				}
				c.clear();
				wo.updateUI();
				beep();

				if (ArrayUtils.isEmpty(w.getcArray())) {
					JOptionPane.showMessageDialog(wo, "地主胜利!");
				} else {
					pct();
				}

			}



		});

		// 取消选中
		quxiao.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (!e.getComponent().isEnabled()) {
					return;
				}
				if (c.isEmpty()) {
					return;
				}
				final Component[] cs = wo.getComponents();
				for (final String name : c) {
					final Component c2 = Arrays.stream(cs).filter(c -> c.getName().equals(name)).findAny().get();
					final JButton button = (JButton) c2;
					button.setBorder(BorderFactory.createEmptyBorder());
				}
				c.clear();
				beep();
			}

		});

		// 不出
		buchu.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(final MouseEvent e) {
				if (!e.getComponent().isEnabled()) {
					return;
				}

				final List<UR> urList = Cache.su.getUrList();
				final List<UR> fl = urList.stream().filter(ur -> ur.getUre().equals("F")).collect(Collectors.toList());
				if (fl.stream().allMatch(ur -> ArrayUtils.isEmpty(ur.getPreviousCArray()))) {
					JOptionPane.showMessageDialog(wo, "轮到主动出牌，不能不出！");
					return;
				}
				final UR w = urList.stream().filter(ur -> ur.getUre().equals("L")).findAny().get();
				w.setPreviousCArray(null);
				ChupaiSW.clear(wo.getComponents(), wo);
				final ImageIcon readIcon = ChushihuaSW.readIcon(ChupaiSW.BUCHU);
				wo.remove(wo.getComponentCount() - 1);
				wo.add(new JLabel(readIcon));
				beep();
				pct();

			};
		});

	}

	/**
	 * 电脑出牌
	 */
	private void pct() {
		// 禁用几个按钮
		Arrays.stream(anniu.getComponents())
			.filter(c -> c instanceof JButton)
			.forEach(b -> b.setEnabled(false));

		// 电脑一开始出牌
		xinxi.setText("农民一正在思考...");
		final ChupaiSW chupaiSW = new ChupaiSW(zuoshang, 2);
		chupaiSW.execute();

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (chupaiSW.getState() != SwingWorker.StateValue.DONE) {
					try {
						Thread.sleep(30);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(50);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
				// 电脑二开始出牌
				xinxi.setText("农民二正在思考...");
				final ChupaiSW chupaiSW2 = new ChupaiSW(youshang, 3);
				chupaiSW2.execute();
				while (chupaiSW2.getState() != SwingWorker.StateValue.DONE) {
					try {
						Thread.sleep(30);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
				xinxi.setText("轮到地主出牌");
				// 启用几个按钮
				Arrays.stream(anniu.getComponents())
					.filter(c -> c instanceof JButton)
					.forEach(b -> b.setEnabled(true));
			}
		});
		thread.start();
	}

	private void beep() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				getToolkit().beep();
			}
		});
	}

	private boolean f(final int[] array){
		final FP fp = new FP(array);
		final String json = JSON.toJSONString(fp);

		final HttpResponse response = HttpRequest.post(J.API + "/f").body(json).execute();
		final String body = response.body();

		final R<Boolean> r = JSON.parseObject(body, new TypeReference<R<Boolean>>() {});
		final Boolean ok = r.getData();
		return ok;
	}

	private boolean y(final int[] array){
		final List<UR> urList = Cache.su.getUrList();
		final UR n2 = urList.stream().filter(ur -> ur.getId().equals(3)).findAny().get();
		int[] pa = n2.getPreviousCArray();
		if (ArrayUtils.isEmpty(pa)) {
			pa = urList.stream().filter(ur -> ur.getId().equals(2)).findAny().get().getPreviousCArray();
		}
		if (ArrayUtils.isEmpty(pa)) {
			return true;
		}
		final Y y = new Y(pa, array);
		final String json = JSON.toJSONString(y);
		final HttpResponse response = HttpRequest.post(J.API + "/y").body(json).execute();
		final String body = response.body();

		final R<Boolean> r = JSON.parseObject(body, new TypeReference<R<Boolean>>() {});
		final Boolean ok = r.getData();
		return ok;
	}

}
