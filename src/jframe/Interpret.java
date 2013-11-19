package jframe;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import control.ConvertView;
import control.ObjectDirectAccessor;

public class Interpret extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	ObjectDirectAccessor accessor;

	private JPanel panel1;
	private JTextArea textArea;

	private JPanel panelClassname;
	private JPanel panelConstract;
	private JPanel panelField;
	private JPanel panelFieldSub;
	private JPanel panelMethodSub;
	private JPanel panelMethod;

	private JLabel LClassname = new JLabel("クラス名");
	private JLabel LConstract = new JLabel("コンストラクタ");
	private JLabel LField = new JLabel("フィールド");
	private JLabel LMethod = new JLabel("メソッド");

	private JButton bClassname;
	private JButton bConstract;
	private JButton bConstractSelect;
	private JButton bField;
	private JButton bFieldSub;
	private JButton bMethod;

	private JTextField TClassname;
	private JComboBox<String> cConstract;
	private JComboBox<String> cField;
	private JComboBox<String> cMethod;
	private JTextField TFieldSub;
	private JTextField TConstract;
	private JTextField TMethodSub;

	private JPanel panelConstractSub;

	private JButton bMethodSelect;

	private static final int FRAME_HEIGHT = 600;
	private static final int FRAME_WIDTH = 800;

	private static final String FRAME_TITLE = "Interpret";

	private JTextField[] paramsConstract;
	private JButton[] paramsConstractButton;

	private JTextField[] paramsMethods;
	private JButton[] paramsMethodButton;

	Constructor<?> selectedConstracter;

	Method selectedMethod;

	public Interpret() {
		setTitle(FRAME_TITLE);
		setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Container container = getContentPane();
		container.setLayout(new GridLayout(1, 2));

		panel1 = new JPanel(new GridLayout(4, 1));

		panelClassname = new JPanel(new GridLayout(5, 1));
		panelConstract = new JPanel(new GridLayout(5, 1));
		panelConstractSub = new JPanel(new GridLayout(1, 10));
		panelMethodSub = new JPanel(new GridLayout(1, 10));
		panelField = new JPanel(new GridLayout(5, 1));
		panelFieldSub = new JPanel(new GridLayout(1, 3));
		panelMethod = new JPanel(new GridLayout(5, 1));

		panel1.add(panelClassname);
		panel1.add(panelConstract);
		panel1.add(panelField);
		panel1.add(panelMethod);

		createClassName();
		createConstracter();
		createField();
		createMethod();

		container.add(panel1);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		container.add(textArea);

		setVisible(true);
	}

	private void createClassName() {
		panelClassname.add(LClassname);
		TClassname = new JTextField("java.awt.Frame");
		panelClassname.add(TClassname);
		bClassname = new JButton("検索");
		bClassname.addActionListener(this);
		panelClassname.add(bClassname);
		panelClassname.add(new JLabel(" "));
		panelClassname.add(new JLabel(" "));

	}

	private void createConstracter() {
		/* コンストラクタ */

		panelConstract.add(LConstract);

		cConstract = new JComboBox<String>();
		panelConstract.add(cConstract);
		TConstract = new JTextField("");
		bConstractSelect = new JButton("選択");
		bConstractSelect.addActionListener(this);
		bConstractSelect.setEnabled(false);
		bConstract = new JButton("生成");
		bConstract.setEnabled(false);
		bConstract.addActionListener(this);
		panelConstract.add(bConstractSelect);

		panelConstract.add(panelConstractSub);

		panelConstract.add(bConstract);
	}

	private void createField() {
		/* フィールド */

		panelField.add(LField);
		cField = new JComboBox<String>();
		panelField.add(cField);
		bField = new JButton("選択");
		bField.setEnabled(false);
		bField.addActionListener(this);
		panelField.add(bField);

		TFieldSub = new JTextField("");
		panelField.add(TFieldSub);

		bFieldSub = new JButton("変更");
		bFieldSub.setEnabled(false);
		bFieldSub.addActionListener(this);
		panelField.add(bFieldSub);

	}

	private void createMethod() {
		/* メソッド */
		panelMethod.add(LMethod);

		cMethod = new JComboBox<String>();
		panelMethod.add(cMethod);

		bMethodSelect = new JButton("選択");
		bMethodSelect.setEnabled(false);
		bMethodSelect.addActionListener(this);
		panelMethod.add(bMethodSelect);

		panelMethod.add(panelMethodSub);

		bMethod = new JButton("実行");
		bMethod.setEnabled(false);
		bMethod.addActionListener(this);
		panelMethod.add(bMethod);

	}

	public static void main(String[] args) {

		new Interpret();

	}

	private Object o;

	public void callDialog(String targetClassName) {
		ParamCreateDialog dlg = new ParamCreateDialog(targetClassName);
		o = dlg.resulet;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		Object source = null;
		try {
			source = ae.getSource();
			System.out.println(source.toString());
			if (source == bClassname) {
				textArea.setText("クラス名を検索します...");
				searchClass();
				//callDialog(TClassname.getText());
			} else if (source == bConstractSelect) {
				textArea.setText("コンストラクタの引数を取得します...");
				viewConstractParam();
			} else if (source == bConstract) {
				textArea.setText("コンストラクタの生成をします...");
				createInstance();
			} else if (source == bField) {
				textArea.setText("フィールドの選択がされました...");
				selectedField();
			} else if (source == bFieldSub) {
				textArea.setText("指定されたフィールドに値を入れようとしています...");
				settingField();
			} else if (source == bMethodSelect) {
				textArea.setText("メソッドの選択がされました...");
				viewMethodParam();
			} else if (source == bMethod) {
				textArea.setText("指定されたメソッドを実行しようとしています...");
				execMethod();
			} else {
				if (paramsConstractButton != null) {
					for (int i = 0; i < paramsConstractButton.length; i++) {
						if (source == paramsConstractButton[i]) {
							ParamCreateDialog pDialog = new ParamCreateDialog(selectedConstracter.getParameterTypes()[i].getName());
							ConstractParamObjects[i] = pDialog.resulet;
							if (pDialog.resulet != null) {
								paramsConstractButton[i].setText("SETTED");
							} else {
								paramsConstractButton[i].setText("DEFAULT");
							}
						}
					}
				}
			    if (paramsMethodButton != null) {
					System.out.println("paramsMethodButton ; " + paramsMethodButton.length);
					for (int i = 0; i < paramsMethodButton.length; i++) {
						if (source == paramsMethodButton[i]) {
							ParamCreateDialog pDialog = new ParamCreateDialog(selectedMethod.getParameterTypes()[i].getName());
							MethodParamObjects[i] = pDialog.resulet;
							if (pDialog.resulet != null) {
								paramsMethodButton[i].setText("SETTED");
							} else {
								paramsMethodButton[i].setText("DEFAULT");
							}
						}
					}
			    }
			}
			setVisible(true);

		} catch (Exception e) {
			textArea.setText("エラー " + e.toString());
			e.printStackTrace();
		}

	}

	private JTextArea addText(JTextArea textArea, String text) {
		textArea.setText(textArea.getText() + text);
		return textArea;
	}

	/**
	 * テキストエリアに与えられたクラス名を検索して、コンストラクタ、フィールド、メソッドをセットする。
	 * 検索する前にすべての値をリセットする。
	 */
	private void searchClass() {
		bConstract.setEnabled(false);
		bMethod.setEnabled(false);
		bField.setEnabled(false);
		bFieldSub.setEnabled(false);
		bConstractSelect.setEnabled(false);
		bMethodSelect.setEnabled(false);

		cField.removeAllItems();
		cConstract.removeAllItems();
		cMethod.removeAllItems();
		panelConstractSub.removeAll();
		panelConstractSub.add(new JLabel(""));
		panelMethodSub.removeAll();
		panelMethodSub.add(new JLabel(""));

		if (TClassname == null || TClassname.getText() == null || TClassname.getText().equals("")) {
			textArea = addText(textArea, "クラス名を入力してください");
		} else {
			try {
				accessor = new ObjectDirectAccessor(TClassname.getText());

				for (Constructor<?> constructor : accessor.getConstracter()) {
					cConstract.addItem(ConvertView.convert(constructor));
				}

				for (Method method : accessor.getMethodList()) {
					cMethod.addItem(ConvertView.convert(method));
				}

				for (Field field : accessor.getFieldList()) {
					cField.addItem(ConvertView.convert(field));
				}

				textArea = addText(textArea, "\n" + TClassname.getText() + "の情報を取得しました。");

				if (!accessor.getHavingClass().isInterface()) {
					bConstractSelect.setEnabled(true);
				} else {
					textArea = addText(textArea, "\n" + TClassname.getText() + "これはインターフェースです。");
				}

			} catch (InstantiationException e) {
				// no action
			} catch (ClassNotFoundException e) {
				textArea.setText("存在しないクラス名です。");
			}
		}
	}

	Object[] ConstractParamObjects;
	Object[] MethodParamObjects;

	private void viewConstractParam() {
		selectedConstracter = accessor.getConstracter()[cConstract.getSelectedIndex()];
		Class[] clazzes = selectedConstracter.getParameterTypes();
		panelConstractSub.setLayout(new GridLayout(1, clazzes.length * 2));
		panelConstractSub.removeAll();
		paramsConstract = new JTextField[clazzes.length];
		ConstractParamObjects = new Object[clazzes.length];
		paramsConstractButton = new JButton[clazzes.length];

		if (clazzes.length == 0) {
			panelConstractSub.add(new JLabel(""));
		} else {
			bConstract.setEnabled(true);
		}

		for (int i = 0; i < clazzes.length; i++) {
			Class clazz = clazzes[i];
			panelConstractSub.add(new JLabel(clazz.getName()));

			if (clazz.isPrimitive() || clazz == String.class) {
				paramsConstract[i] = new JTextField();
				panelConstractSub.add(paramsConstract[i]);
			} else {
				paramsConstract[i] = new JTextField();
				paramsConstractButton[i] = new JButton();
				paramsConstractButton[i].addActionListener(this);
				paramsConstractButton[i].setText(" ");

				panelConstractSub.add(paramsConstractButton[i]);
			}
		}
	}

	private void viewMethodParam() {
		selectedMethod = accessor.getMethodList()[cMethod.getSelectedIndex()];
		Class[] clazzes = selectedMethod.getParameterTypes();
		panelMethodSub.setLayout(new GridLayout(1, clazzes.length * 2));
		panelMethodSub.removeAll();
		MethodParamObjects = new Object[clazzes.length];
		paramsMethods = new JTextField[clazzes.length];
		paramsMethodButton = new JButton[clazzes.length];

		if (clazzes.length == 0) {
			panelMethodSub.add(new JLabel(""));
		}

		for (int i = 0; i < clazzes.length; i++) {
			Class clazz = clazzes[i];
			panelMethodSub.add(new JLabel(clazz.getName()));

			if (clazz.isPrimitive() || clazz == String.class) {
				paramsMethods[i] = new JTextField();
				panelMethodSub.add(paramsMethods[i]);
			} else {
				paramsMethods[i] = new JTextField();
				paramsMethodButton[i] = new JButton();
				paramsMethodButton[i].addActionListener(this);
				paramsMethodButton[i].setText(" ");

				panelMethodSub.add(paramsMethodButton[i]);
			}
		}
		bMethod.setEnabled(true);

	}

	private Object convertParam(Class paramType, JTextField param, Object paramObject) {
		Object object;
		if (paramType.isPrimitive()) {
			if (paramType == int.class) {
				object = Integer.valueOf(param.getText());
			} else if (paramType == byte.class) {
				object = Byte.valueOf(param.getText());
			} else if (paramType == short.class) {
				object = Short.valueOf(param.getText());
			} else if (paramType == long.class) {
				object = Long.valueOf(param.getText());
			} else if (paramType == float.class) {
				object = Float.valueOf(param.getText());
			} else if (paramType == double.class) {
				object = Double.valueOf(param.getText());
			} else if (paramType == char.class) {
				object = param.getText().toCharArray()[0];
			} else if (paramType == boolean.class) {
				object = Boolean.valueOf(param.getText());
			} else {
				object = param.getText();
			}

		} else if (paramType == String.class) {
			object = new String(param.getText());
		} else {
			if (paramObject == null) {
				object = newInstanceDefault(paramType);
			} else {
				object = paramObject;
			}
		}
		return object;
	}

	private Object[] convertParam(Class[] paramTypes, JTextField[] params, Object[] paramObject) {

		Object[] paramsObjects = new Object[paramTypes.length];

		for (int i = 0; i < paramTypes.length; i++) {
			Class clazz = paramTypes[i];
			paramsObjects[i] = convertParam(paramTypes[i], params[i], paramObject[i]);
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

	/**
	 * テキストエリアに与えられたクラス名を検索して、コンストラクタ、フィールド、メソッドをセットする。
	 */
	private void createInstance() {

		if (selectedConstracter.equals(accessor.getConstracter()[cConstract.getSelectedIndex()])) {
			try {

				accessor.createInstance(cConstract.getSelectedIndex(), convertParam(selectedConstracter.getParameterTypes(), paramsConstract, ConstractParamObjects));

				textArea = addText(textArea, "\n生成しました");
				bMethodSelect.setEnabled(true);
				bField.setEnabled(true);

			} catch (InstantiationException e) {
				textArea = addText(textArea, "\n生成に失敗しました : " + e.toString());
			} catch (IllegalAccessException e) {
				textArea = addText(textArea, "\n生成に失敗しました : " + e.toString());
			} catch (IllegalArgumentException e) {
				textArea = addText(textArea, "\n生成に失敗しました : " + e.toString());
			} catch (InvocationTargetException e) {
				textArea = addText(textArea, "\n生成に失敗しました : " + e.toString());
			}

		} else {
			textArea = addText(textArea, "\n生成に失敗しました :「選択」してから「生成」してください");
		}

	}

	/**
	 * 選択されたフィールドの今の値を表示する
	 */
	private void selectedField() {
		TFieldSub.setText("");
		try {
			Object object = accessor.getFieldValue(cField.getSelectedIndex());
			textArea = addText(textArea, "\nフィールドの値を取得しました");
			TFieldSub.setText(object.toString());
			bFieldSub.setEnabled(true);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			textArea = addText(textArea, "\nフィールドの値の取得に失敗しました" + e.toString());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			textArea = addText(textArea, "\nフィールドの値の取得に失敗しました" + e.toString());
		}
	}

	/**
	 * 選択されたフィールドの値を変更する
	 */
	private void settingField() {
		try {
			accessor.chengeField(cField.getSelectedIndex(), TFieldSub.getText());

			textArea = addText(textArea, "\nフィールドの値の変更しました");
		} catch (NumberFormatException e) {
			textArea = addText(textArea, "\nフィールドの値の変更に失敗しました" + e.toString());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			textArea = addText(textArea, "\nフィールドの値の変更に失敗しました" + e.toString());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			textArea = addText(textArea, "\nフィールドの値の変更に失敗しました。Static + finalの値は変更できません" + e.toString());
			e.printStackTrace();
		}

	}

	/**
	 * 選択されたメソッドを実行する。
	 */
	private void execMethod() {

		if (selectedMethod.equals(accessor.getMethodList()[cMethod.getSelectedIndex()])) {

			try {
				Object result = accessor.callMethod(cMethod.getSelectedIndex(), convertParam(selectedMethod.getParameterTypes(), paramsMethods, MethodParamObjects));
				if (result == null) {
					textArea = addText(textArea, "\nメソッドを実行しました");
				} else {
					textArea = addText(textArea, "\nメソッドを実行しました : " + result);
				}

			} catch (SecurityException e) {
				textArea = addText(textArea, "\nメソッド実行に失敗しました。" + e.toString());
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				textArea = addText(textArea, "\nメソッド実行に失敗しました。" + e.toString());
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				textArea = addText(textArea, "\nメソッド実行に失敗しました。" + e.toString());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				textArea = addText(textArea, "\nメソッド実行に失敗しました。" + e.toString());
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				textArea = addText(textArea, "\nメソッド実行に失敗しました。" + e.toString());
				e.printStackTrace();
			}
		} else {
			textArea = addText(textArea, "\n実行に失敗しました :「選択」してから「実行」してください");
		}
	}
}
