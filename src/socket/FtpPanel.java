package socket;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Box;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.sun.media.sound.ModelAbstractChannelMixer;
import com.sun.security.jgss.ExtendedGSSContext;

import javafx.scene.transform.Rotate;
import sun.reflect.generics.tree.Tree;

import java.awt.Color;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

public class FtpPanel {

	private static Ftp f;
	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JPasswordField passwordField;
	private JTextField textField_2;
	private JScrollPane scrollPane;
	private static JTextArea textArea;
	private int P;
	private int V;
	private static JTextField textField_3;
	private static JTextField textField_4;
	private static JTextField textField_5;
	private static JTextField textField_6;
	public static DefaultMutableTreeNode root= new DefaultMutableTreeNode(); 
	public static JTree current_node;
	
	public static DefaultMutableTreeNode before_tn=null;
	public static JTree ftp_dir_tree;
	
	public int before_PathCount = 0 ;//上次的目录层次数
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
			f=new Ftp();
			f.start();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FtpPanel window = new FtpPanel();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FtpPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public static void editTextArea(String context) {
		textArea.append(context+"\n");
	}
	public static int getPort() {
		int port=21;
		String port_str=textField_3.getText();
		if(port_str.isEmpty()){
			port_str="21";
			textField_3.setText("21");
			editTextArea("您未设置端口号,将以默认端口21进行连接.");
			return 21;
		}
		else
		{
			port=Integer.valueOf(port_str);
			return port;
		}
	}
	public static String getStartIP() {
		return textField_4.getText();
	}
	public static String getStartIPPort() {
		return textField_6.getText();
	}
	public static String getRange() {
		return textField_5.getText();
	}
	public static void setRange(int range_value) {
		textField_5.setText(Integer.toString(range_value));
		return ;
	}
	// 展开树的所有节点的方法
	public static void set_root(DefaultMutableTreeNode _r) {
		//root = _r;
		
		return;
	}
	

	public static void set_root_child_name(String name) {
//		JTree ftp_dir_tree = new JTree(root);
		DefaultMutableTreeNode cdir=new DefaultMutableTreeNode(name);
		
		root.add(cdir);
		
//		current_node.add(cdir);
		//JTree ftp_dir_tree = new JTree(root);
		
	}
	public static void reset_Tree() {
		
	}
	public static void update_Tree() {
		ftp_dir_tree.expandRow(0);
		ftp_dir_tree.expandRow(1);
		ftp_dir_tree.expandRow(2);
		System.out.println("展开");
//		for (int i=0;i<root.getChildCount();i++) {
//			System.out.println("获得子节点"+root.getChildAt(i).toString());
//		}
		
		
		
		ftp_dir_tree.updateUI();
//		root.reload();
	}
	
	private void initialize() {
		
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 1276, 759);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblHostname = new JLabel("Hostname:");
		lblHostname.setBounds(24, 28, 72, 18);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(24, 58, 72, 18);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(24, 89, 72, 18);
		
		textField = new JTextField();
		textField.setBounds(110, 25, 212, 24);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(110, 55, 212, 24);
		textField_1.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(110, 87, 212, 24);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(350, 86, 113, 27);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username,password;
				String hostname = textField.getText();
				if(hostname.isEmpty()) {
					editTextArea("服务器地址不能为空!");
					JOptionPane.showMessageDialog(null, "友情提示：服务器地址不能为空!，请输入","警告",JOptionPane.ERROR_MESSAGE); 
					return;
				}
				try { 
						
						
					    f.openConnection(hostname); // 控制连接建立,设置为自己的IP. 
					    f.getMsgs(); // 启动接收线程 
					    username=textField_1.getText();
					    password=passwordField.getText();
					    if(username.isEmpty())
					    	username="anonymous";
					    if(password.isEmpty())
					    	password="";
					    f.main_proc(username,password); // ftp 处理
					    //while(true)
					    //{
					    	//加信号量
					    //}
					    //System.exit(0); // 结束程序 
				   } catch (Exception e2) {
					    e2.printStackTrace(); 
					    System.exit(1); 
				   }
			}
		});
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(68, 183, 605, 378);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		JButton btnNewButton = new JButton("LS");
		btnNewButton.setBounds(68, 561, 120, 27);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String hostname = textField.getText();
				String username = textField_1.getText();
				if(hostname.isEmpty()) {
					JOptionPane.showMessageDialog(null, "友情提示：服务器地址不能为空!，请输入","警告",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(username.isEmpty()) {
					JOptionPane.showMessageDialog(null, "友情提示：用户名不能为空!，请输入","警告",JOptionPane.ERROR_MESSAGE);
					return;
				}
				f.doLs();
			}
		});
		
		JButton btnNewButton_1 = new JButton("GET");
		btnNewButton_1.setBounds(262, 561, 111, 27);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = "";
				String loanFile = "";
				fileName = textField_2.getText();
				if(fileName.isEmpty()){
					FtpPanel.editTextArea("下载文件名不能为空!");
				}
				JFileChooser c = new JFileChooser();
				int rVal = c.showSaveDialog(FtpPanel.this.frame);
				if (rVal == JFileChooser.APPROVE_OPTION) {
			        loanFile = c.getSelectedFile().getAbsolutePath();
			      }
				if(fileName.isEmpty()) return;
				f.doGet(fileName,loanFile);
			}
		});
		
		JButton btnNewButton_2 = new JButton("PUT");
		btnNewButton_2.setBounds(68, 606, 120, 27);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName="";
				String localFileName="";
				final JFileChooser fc = new JFileChooser();			
				int returnVal = fc.showOpenDialog(FtpPanel.this.frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            //This is where a real application would open the file.
		            //log.append("Opening: " + file.getName() + "." + newline);
		            fileName=file.getAbsolutePath();
		            localFileName=file.getName();
		            System.out.println(file.getAbsolutePath());
		        } else {
		            //log.append("Open command cancelled by user." + newline);
		        	System.out.println("Open command cancelled by user.");
		        }
				f.doPut(fileName,localFileName);
			}
		});
		
		JButton btnNewButton_3 = new JButton("QUIT");
		btnNewButton_3.setBounds(262, 606, 111, 27);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.doQuit();
			}
		});
		
		textField_2 = new JTextField();
		textField_2.setBounds(478, 562, 96, 24);
		textField_2.setColumns(10);
		
		JButton btnSendRequest = new JButton("Cd");
		btnSendRequest.setBounds(415, 561, 49, 27);
		btnSendRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dirName = textField_2.getText();
				f.doCd(dirName);
				FtpPanel.editTextArea("当前目录已切换至 " + dirName);
			}
		});
		
		JButton btnExit = new JButton("EXIT");
		btnExit.setBounds(415, 606, 65, 27);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.exit(0);
				} catch (Exception e1) {
					e1.printStackTrace();
				} // 关闭连接 
			}
		});
		
		JLabel lblNewLabel = new JLabel("Port:");
		lblNewLabel.setBounds(340, 28, 40, 18);
		
		textField_3 = new JTextField();
		textField_3.setBounds(386, 25, 63, 24);
		textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		textField_3.setText("21");
		textField_3.setColumns(10);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(681, 568, 73, 27);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText(null);
			}
		});
		
		JLabel lblStartIpAddress = new JLabel("Start IP Address:");
		lblStartIpAddress.setBounds(673, 70, 136, 18);
		
		textField_4 = new JTextField();
		textField_4.setBounds(815, 67, 138, 24);
		textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		textField_4.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Range:");
		lblNewLabel_1.setBounds(661, 107, 148, 18);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textField_5 = new JTextField();
		textField_5.setBounds(815, 104, 138, 24);
		textField_5.setHorizontalAlignment(SwingConstants.CENTER);
		textField_5.setColumns(10);
		
		JButton btnNewButton_4 = new JButton("Scan");
		btnNewButton_4.setBounds(998, 96, 65, 27);
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IPScan ips = new IPScan();
//				JOptionPane.showMessageDialog(null, "刘贤弟没有鸡巴","标题",JOptionPane.ERROR_MESSAGE);  
//				setRange(1);
				if(ips.check_input_values_ok()==true) {
					System.out.println("好");
					ips.startScan();
				}else {
					System.out.println("坏了");
				}
			}
		});
		
		JLabel lblIpScan = new JLabel("IP Scan:");
		lblIpScan.setBounds(886, 28, 64, 18);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(988, 70, 40, 18);
		
		textField_6 = new JTextField();
		textField_6.setBounds(1059, 67, 59, 24);
		textField_6.setColumns(10);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(923, 183, 2, 2);
		
		
		
//	    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
//	    root.add(new DefaultMutableTreeNode("A"));
//	    root.add(new DefaultMutableTreeNode("B"));
//	    root.add(new DefaultMutableTreeNode("C"));
//	    JTree tree = new JTree(root);
//		this.set_root_child_name("大");this.set_root_child_name("狗");this.set_root_child_name("逼");
		//JTree ftp_dir_tree = root;
		
//		root = new DefaultMutableTreeNode("当前文件夹");
		root = new DefaultMutableTreeNode("/");

		
		JScrollPane jScrollPane_putree = new JScrollPane();
		jScrollPane_putree.setBounds(727, 183, 412, 369);
		
		ftp_dir_tree =new JTree(root);		
		ftp_dir_tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) ftp_dir_tree.getLastSelectedPathComponent();
				if(node == null){
					return;
				}
				if(node.toString().contains("."))
					return;//That is a file
				System.out.println("路径"+node.toString());
				textField_2.setText(node.toString());//auto fill fileName
				
				System.out.println("这个点击节");
				
				for(TreeNode _t:node.getPath()) {
					System.out.print(_t.toString());
				}
				
			
//				int before_node_depth=ftp_dir_tree;
//				before_tn.getLevel()+before_node_depth
				System.out.println("上次选中节点所在层次"+before_PathCount);
				for(int i=0;i<before_PathCount;i++) {
					f.sys_qr_cd("..");//好了，现在已经退到根节点了
				}
				
				for(TreeNode tn:node.getPath()) {
					System.out.print(tn.toString());
					if (tn!=root)
						f.sys_qr_cd(tn.toString());
					System.out.println("重新进入");
				}
//				
//				for(int i=0;i<node_depth-1;i++) {
//					System.out.println("糖");
//					f.sys_qr_cd("..");.getSelectionPath().getPathCount();
//				}//好了，现在已经退到根节点了
//				

//这里弄个ls获取目录				
				node.removeAllChildren();//先杀了所有孩子
				//这里cd必须必须要绝对路径
				
//				for(TreeNode tn:node.getPath()) {
//					System.out.print(tn.toString());
//					if (tn!=root)
//						System.out.print("/");
//				}

///////////////////////////////////////////////////////////////
				f.sys_qr_cd(node.toString());
///////////////////////////////////////////////////////////////				
				
//				f.sys_qr_cd("/KMS/web");
				
				
				System.out.println("已经切换至"+node.toString()+"目录下");

//////////////////////////////////////////////进入这个文件夹并且ls加节点
				String[] dirnames = f.sys_qr_getdirs();
				if(dirnames.equals(null))
					return;
				for(String dir:dirnames) {
					DefaultMutableTreeNode newChild =new DefaultMutableTreeNode(dir);
//					node.add(newChild);
						boolean is_exist = false;
 				            for (Enumeration e = node.children(); e.hasMoreElements(); ) { 
				                TreeNode n = (TreeNode) e.nextElement(); 
				                if (newChild.toString() == n.toString()) {
				                	is_exist= true;
				                }				               
				            } 
 				           if(!is_exist)node.add(newChild);
					System.out.println("屄"+dir);

				}
//////////////////////////////////////////////
				System.out.println("");
//				System.out.println("pwd开始了");
//
//				f.do_pwd();
//				System.out.println("pwd");

//				ftp_dir_tree.expandRow(node_depth-1);//万恶之源
				ftp_dir_tree.expandRow(ftp_dir_tree.getSelectionPath().getPathCount()-1);

//				DefaultMutableTreeNode newChild =new DefaultMutableTreeNode("屁眼");
//				node.add(newChild);

				
					
//				System.out.println("选中节点所在路径"+node.getPath()[1].toString());
				for(TreeNode tn:node.getPath()) {
					System.out.print(tn.toString());
				}

				//DefaultMutableTreeNode ftp_dir_model  = (DefaultMutableTreeNode) ftp_dir_tree.getModel();
				
//				if(node.isLeaf()){
//					changeSelectedTabbedPane(tabbedPane, paneNameIndexMap.get(node.toString()));
//					System.out.println("nodeName:" + node.toString());
//				}
				
				before_tn = node;
				before_PathCount = ftp_dir_tree.getSelectionPath().getPathCount();
			}
		});
//////////////////////////////////////////////////////		
		
		
		ftp_dir_tree.setBackground(Color.CYAN);
		current_node = ftp_dir_tree;
		
		frame.getContentPane().add(jScrollPane_putree);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(jScrollPane_putree);
		
//		JTree tree = new JTree((TreeNode) null);
//		tree.setBackground(Color.WHITE);
//		jScrollPane_putree.setViewportView(tree);
		
		jScrollPane_putree.setViewportView(ftp_dir_tree);
		
		frame.getContentPane().add(btnNewButton_2);
		frame.getContentPane().add(btnNewButton);
		frame.getContentPane().add(btnNewButton_3);
		frame.getContentPane().add(btnNewButton_1);
		frame.getContentPane().add(btnSendRequest);
		frame.getContentPane().add(textField_2);
		frame.getContentPane().add(btnExit);
		frame.getContentPane().add(scrollPane);
		frame.getContentPane().add(btnClear);
		frame.getContentPane().add(scrollPane_1);
		frame.getContentPane().add(lblUsername);
		frame.getContentPane().add(textField_1);
		frame.getContentPane().add(lblPassword);
		frame.getContentPane().add(passwordField);
		frame.getContentPane().add(btnLogin);
		frame.getContentPane().add(lblStartIpAddress);
		frame.getContentPane().add(lblNewLabel_1);
		frame.getContentPane().add(textField_5);
		frame.getContentPane().add(textField_4);
		frame.getContentPane().add(btnNewButton_4);
		frame.getContentPane().add(lblPort);
		frame.getContentPane().add(textField_6);
		frame.getContentPane().add(lblHostname);
		frame.getContentPane().add(textField);
		frame.getContentPane().add(lblNewLabel);
		frame.getContentPane().add(textField_3);
		frame.getContentPane().add(lblIpScan);
	}
}
