package jframe;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import control.ConvertView;
import control.ObjectDirectAccessor;

public class ParamCreateDialog extends JDialog implements ActionListener, WindowListener {
	private static final long serialVersionUID = 2L;

	private JButton btn_ok;
	private JButton btn_cancel;

	public Object resulet;

	private JPanel panelConstract;

	private JLabel LConstract = new JLabel("コンストラクタ");

	private JButton bConstract;
	private JButton bConstractSelect;

	private JComboBox<String> cConstract;

	private JTextField TConstract;
	private JTextField[] paramsConstract;

	private JPanel panelConstractSub;

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btn_cancel) {
			this.setVisible(false);
			this.dispose();
			return;
		}
		else if (e.getSource() == bConstractSelect) {
			viewConstractParam();
			this.setVisible(true);
			return;
		}
		else if (e.getSource() == bConstract) {
			createInstance();
			resulet = accessor.getHavingObject();
			System.out.println(resulet);
			this.setVisible(false);
			this.dispose();
			return;
		}
		else {
			System.out.println("other event");
		}
		
	}

	ObjectDirectAccessor accessor = null;

	public ParamCreateDialog(String targetClassname) {
		this.setModal(true);
		System.out.println("ダイアログ出現：" + targetClassname);
		try {
			accessor = new ObjectDirectAccessor(targetClassname);
		} catch (InstantiationException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		if(accessor ==null || accessor.getConstracter() ==null || accessor.getConstracter().length == 0){
			resulet = accessor.getHavingObject();
			this.setVisible(false);
			this.dispose();
			return;
		}
		
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		// ウインドウが閉じられたら、リソースを自動的に解放する
		//      this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		// フレームのサイズ
		this.setSize(450, 300);
		// フレームのタイトル
		this.setTitle("dlgTestDlg - Dialog Window");

		panelConstract = new JPanel(new GridLayout(6, 1));
		panelConstractSub = new JPanel(new GridLayout(1, 10));
		add(panelConstract);
		LConstract.setText(targetClassname);
		panelConstract.add(LConstract);

		cConstract = new JComboBox<String>();
		panelConstract.add(cConstract);
		//TConstract = new JTextField("");
		bConstractSelect = new JButton("選択");
		bConstractSelect.addActionListener(this);
		bConstract = new JButton("OK");
		bConstract.setEnabled(false);
		bConstract.addActionListener(this);
		panelConstract.add(bConstractSelect);

		panelConstract.add(panelConstractSub);

		panelConstract.add(bConstract);
		for (Constructor<?> constructor : accessor.getConstracter()) {
			cConstract.addItem(ConvertView.convert(constructor));
		}

		btn_cancel = new JButton("キャンセル");
		panelConstract.add(btn_cancel);
		btn_cancel.addActionListener(this);

		panelConstractSub.removeAll();
		panelConstractSub.add(new JLabel(" "));

		this.addWindowListener(this);
		this.setVisible(true);
	}

	Constructor<?> selectedConstracter;

	private void viewConstractParam() {
		selectedConstracter = accessor.getConstracter()[cConstract.getSelectedIndex()];

		System.out.println(selectedConstracter);

		Class[] clazzes = selectedConstracter.getParameterTypes();
		panelConstractSub.setLayout(new GridLayout(1, clazzes.length * 2));
		panelConstractSub.removeAll();
		paramsConstract = new JTextField[clazzes.length];

		System.out.println("clazzes.length " + clazzes.length);

		if (clazzes.length == 0) {
			panelConstractSub.add(new JLabel(""));
		}

		for (int i = 0; i < clazzes.length; i++) {
			Class clazz = clazzes[i];
			panelConstractSub.add(new JLabel(clazz.getName()));

			if (clazz.isPrimitive() || clazz == String.class) {
				paramsConstract[i] = new JTextField();
				panelConstractSub.add(paramsConstract[i]);
			} else {
				paramsConstract[i] = new JTextField();
				paramsConstract[i].setText("default");
				paramsConstract[i].setEditable(false);
				panelConstractSub.add(paramsConstract[i]);
			}

		}

		bConstract.setEnabled(true);
	}

	/**
	 * テキストエリアに与えられたクラス名を検索して、コンストラクタ、フィールド、メソッドをセットする。
	 */
	private void createInstance() {

		if (selectedConstracter.equals(accessor.getConstracter()[cConstract.getSelectedIndex()])) {
			try {

				accessor.createInstance(cConstract.getSelectedIndex(), convertParam(selectedConstracter.getParameterTypes(), paramsConstract));
				
				System.out.println(accessor.getHavingObject());
			} catch (InstantiationException e) {
				System.out.println("?");
				//newInstanceDefault(accessor.getHavingClass());
			} catch (IllegalAccessException e) {
				System.out.println("?");
				//newInstanceDefault(accessor.getHavingClass());
			} catch (IllegalArgumentException e) {
				System.out.println("?");
				//newInstanceDefault(accessor.getHavingClass());
			} catch (InvocationTargetException e) {
				System.out.println("?");
				//newInstanceDefault(accessor.getHavingClass());
			}

		} else {
			System.out.println("?");
		}

	}

	private Object[] convertParam(Class[] paramTypes, JTextField[] params) {

		Object[] paramsObjects = new Object[paramTypes.length];

		for (int i = 0; i < paramTypes.length; i++) {
			Class clazz = paramTypes[i];
			if (clazz.isPrimitive()) {
				if (clazz == int.class) {
					paramsObjects[i] = Integer.valueOf(params[i].getText());
				} else if (clazz == byte.class) {
					paramsObjects[i] = Byte.valueOf(params[i].getText());
				} else if (clazz == short.class) {
					paramsObjects[i] = Short.valueOf(params[i].getText());
				} else if (clazz == long.class) {
					paramsObjects[i] = Long.valueOf(params[i].getText());
				} else if (clazz == float.class) {
					paramsObjects[i] = Float.valueOf(params[i].getText());
				} else if (clazz == double.class) {
					paramsObjects[i] = Double.valueOf(params[i].getText());
				} else if (clazz == char.class) {
					paramsObjects[i] = params[i].getText().toCharArray()[0];
				} else if (clazz == boolean.class) {
					paramsObjects[i] = Boolean.valueOf(params[i].getText());
				} else {
					paramsObjects[i] = params[i].getText();
				}

			} else if (clazz == String.class) {
				paramsObjects[i] = new String(params[i].getText());
			} else {
				paramsObjects[i] = newInstanceDefault(clazz);
			}
		}

		return paramsObjects;
	}

	private Object newInstanceDefault(Class clazz) {
		Object o = null;
		try {
			o = clazz.newInstance();

		} catch (Exception e) {
			for (Constructor<?> c : clazz.getConstructors()) {

				Object[] params = new Object[c.getParameterTypes().length];
				for (int i = 0; i < c.getParameterTypes().length; i++) {
					params[i] = newInstanceDefault(c.getParameterTypes()[i]);
				}

				try {
					o = c.newInstance(params);
					break;
				} catch (InstantiationException e1) {
					continue;
				} catch (IllegalAccessException e1) {
					continue;
				} catch (IllegalArgumentException e1) {
					continue;
				} catch (InvocationTargetException e1) {
					continue;
				}
			}

		}

		return o;
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

}