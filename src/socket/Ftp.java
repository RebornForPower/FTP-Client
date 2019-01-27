package socket; 
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;


import java.io.*; 
public class Ftp extends Thread{ 
Socket ctrlSocket;// 控制用Socket 
public PrintWriter ctrlOutput;// 控制输出用的流 
public BufferedReader ctrlInput;// 控制输入用的流 
private int CTRLPORT = 21;// ftp 的控制用端口 
ServerSocket serverDataSocket; 
// openConnection方法 
// 由地址和端口号构造Socket，形成控制用的流 

/** 本地字符编码 */
private static String LOCAL_CHARSET = "GBK";
 
// FTP协议里面，规定文件名编码为iso-8859-1
private static String SERVER_CHARSET = "UTF-8";
 

public void openConnection(String host) throws IOException, 
    UnknownHostException { 
   CTRLPORT=FtpPanel.getPort();//端口号获取
   ctrlSocket = new Socket(host, CTRLPORT); 
   ctrlOutput = new PrintWriter(ctrlSocket.getOutputStream()); 
   ctrlInput = new BufferedReader(new InputStreamReader(ctrlSocket 
     .getInputStream())); 
} 
// closeConnection方法 
// 关闭控制用的Socket 
public void closeConnection() throws IOException { 
   ctrlSocket.close(); 
} 
// showMenu方法 
// 输出ftp的命令菜单 
public void showMenu() { 
   System.out.println(">Command?"); 
   System.out.print(" 1:ls"); 
   System.out.print(" 2:cd"); 
   System.out.print(" 3:get"); 
   System.out.print(" 4:put"); 
   System.out.print(" 5:ascii"); 
   System.out.print(" 6:binary"); 
   System.out.println(" 7:quit"); 
   
} 
// getCommand方法 
// 读取用户指定的命令序号 
public String getCommand() { 
   String buf = ""; 
   BufferedReader lineread = new BufferedReader(new InputStreamReader( 
     System.in)); 
   while (buf.length() != 1) { 
    try { 
     buf = lineread.readLine(); 
    } catch (Exception e) { 
     e.printStackTrace(); 
     System.exit(1); 
    } 
   } 
   return (buf); 
} 
public void doLogin(String username,String password) { 
   //String loginName = ""; 
   //String password = ""; 
   //BufferedReader lineread = new BufferedReader(new InputStreamReader( 
     //System.in)); 
//对用户名等 鲁棒检测	
//	if(host.isEmpty())
//		JOptionPane.showMessageDialog(null, "友情提示:Hostname不能为空，请重新输入","警告",JOptionPane.ERROR_MESSAGE); 
//	
   try { 
    //System.out.println("请输入用户名"); 
    //loginName = lineread.readLine(); 
   
    ctrlOutput.println("USER " + username); 
    ctrlOutput.flush(); 
    //System.out.println("请输入口令"); 
    //password = lineread.readLine(); 

	ctrlOutput.println("PASS " + password); 
	ctrlOutput.flush(); 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
   doLs();
} 
public void do_pwd() {
	try { 
	    ctrlOutput.println("PWD "); 
	    ctrlOutput.flush(); 
	    
	   } catch (Exception e) { 
	    System.exit(1); 
	   } 
}
public void doQuit() { 
   try { 
    ctrlOutput.println("QUIT "); 
    ctrlOutput.flush(); 
    ctrlInput.close();
    
   } catch (Exception e) { 
	System.out.println("未连接服务器,没有向Server发送QUIT命令");
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 
public void doCd(String dirName) { 
   //String dirName = ""; 
   BufferedReader lineread = new BufferedReader(new InputStreamReader( 
     System.in)); 
   try { 
    //System.out.println("请输入目录名");  
    //dirName = lineread.readLine(); 
	   if (dirName.isEmpty()) {
		   FtpPanel.editTextArea("请输入目录名");
		   return;
	   }
	
    ctrlOutput.println("CWD " + dirName);// CWD命令 
    ctrlOutput.flush(); 
    boolean dir_ok = false;
	for (int i=0;i<FtpPanel.root.getChildCount();i++) {
//			System.out.println("获得子节点"+FtpPanel.root.getChildAt(i).toString());		
			if(FtpPanel.root.getChildAt(i).toString().equals(dirName)) {
				dir_ok =true;
//				FtpPanel.current =  FtpPanel.root.getChildAt(i).;
			}
	}
	if(dir_ok!=true)
		JOptionPane.showMessageDialog(null, "友情提示：文件名不对，请重新输入","警告",JOptionPane.ERROR_MESSAGE);
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 

public void sys_qr_cd(String dirName) { 
	   BufferedReader lineread = new BufferedReader(new InputStreamReader( 
	     System.in)); 
	   try { 
		   if (dirName.isEmpty()) {
			   return;
		   }
	    ctrlOutput.println("CWD " + dirName);// CWD命令 
	    ctrlOutput.flush(); 
	   } catch (Exception e) { 
	    e.printStackTrace(); 
	    System.exit(1); 
	   } 
	} 


public String[] sys_qr_getdirs() {//sys ls 
	try { 
	    int n; 
	    boolean flag=false;
	    String line="";
	    byte[] buff = new byte[65530]; 
	    Socket dataSocket = dataConnection("NLST "); 
	    BufferedInputStream dataInput = new BufferedInputStream(dataSocket 
	      .getInputStream()); 
	    while ((n = dataInput.read(buff)) > 0) { 
	     System.out.write(buff, 0, n); 
	     if(buff==null) break;
	     
	     line+=new String(buff);
	    } 
	    line = new String(line.getBytes(LOCAL_CHARSET),
	    		SERVER_CHARSET);
	    dataSocket.close(); 
	    String[] arr = line.split("\\s+");
	    String[] dirnames = Arrays.copyOfRange(arr,0,arr.length-1);
//	    for(String a:dirnames) {
//	    	System.out.print("有"+a);
//	    }
	    return dirnames;
	   } catch(IOException e) {
		 return null;
	   }catch (Exception e) { 
		   return null;
	   } 
}


// doLs方法 
// 取得目录信息 

public void doLs() { 
   try { 
    int n; 
    boolean flag=false;
    String line="";
    byte[] buff = new byte[65530]; 
   
    // 建立数据连接 
    Socket dataSocket = dataConnection("NLST "); 
   
    // 准备读取数据用的流 
    BufferedInputStream dataInput = new BufferedInputStream(dataSocket 
      .getInputStream()); 
    // 读取目录信息 
   
    while ((n = dataInput.read(buff)) > 0) { 
     System.out.write(buff, 0, n); 
     if(buff==null) break;
     
     line+=new String(buff);
//     System.out.println(buff+"狗币");
    } 
//服务器utf8 本地gbk 处理乱码问题
    line = new String(line.getBytes(LOCAL_CHARSET),
    		SERVER_CHARSET);
   

//设置目录树节点
 //https://www.rgagnon.com/javadetails/java-0324.html
//    File fList[] = f.listFiles();
//    for(int i = 0; i  < fList.length; i++)
//        getList(child, fList[i]);
//    }
//    
    
    
    
    //ftp.njau.edu.cn
  FtpPanel.editTextArea(line);
    dataSocket.close(); 
    
    String[] arr = line.split("\\s+");
    String[] dirnames = Arrays.copyOfRange(arr,0,arr.length-1);
    for(String a:dirnames) {
    	FtpPanel.set_root_child_name(a);
    }
    FtpPanel.update_Tree();
    

//    JTree ftp_dir_tree = new JTree(root);//root= new DefaultMutableTreeNode(null)
    //按回车切分字符串到具体文件名
//    System.out.println("狗币"+arr[3]);
//    DefaultMutableTreeNode root
//	int tmp=0;
//	for(String a:dirnames) {
//		root.add(new DefaultMutableTreeNode(a,false));
//	}	
//	FtpPanel.set_root_child_names(root);
//    for(String a:dirnames) {
//    		FtpPanel.set_root_child_name(a);
//    }
    //ftp.njau.edu.cn
//    for(String a:arr) {
//    	System.out.println("狗币"+a+"垃圾");
//    }
    
//    FileWriter writer;
//    try {
//        writer = new FileWriter("F:\\code\\java\\狗币.txt");
//        writer.write(line);
//        writer.flush();
//        writer.close();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//文本里的中文显示没毛病
//https://blog.csdn.net/gingerredjade/article/details/62036205 FtpClient中文乱码问题解决
//    https://www.rgagnon.com/javadetails/java-0324.html
//	JOptionPane.showMessageDialog(null, "刘贤弟没有鸡巴","标题",JOptionPane.ERROR_MESSAGE);  

   } catch(IOException e) {
	   System.out.println("输入流出错!");
	   FtpPanel.editTextArea("输入流出错");
   }catch (Exception e) { 
    e.printStackTrace(); 
    //System.exit(1); 
   } 
} 


// dataConnection方法 
// 构造与服务器交换数据用的Socket 
// 再用PORT命令将端口通知服务器 
public Socket dataConnection(String ctrlcmd) { 
   String cmd = "PORT "; // PORT存放用PORT命令传递数据的变量 
   int i; 
   Socket dataSocket = null;// 传送数据用Socket 
   try { 
    // 得到自己的地址 
    byte[] address = InetAddress.getLocalHost().getAddress(); 
    // 用适当的端口号构造服务器 
    serverDataSocket = new ServerSocket(0,1); 
   // serverDataSocket.setSoTimeout(50000000);
    serverDataSocket.setSoTimeout(5000);
    // 准备传送PORT命令用的数据 
    for (i = 0; i < 4; ++i) 
     cmd = cmd + (address[i] & 0xff) + ","; 
    cmd = cmd + (((serverDataSocket.getLocalPort()) / 256) & 0xff) 
      + "," + (serverDataSocket.getLocalPort() & 0xff); 
    // 利用控制用的流传送PORT命令 
    ctrlOutput.println(cmd); 
   
    ctrlOutput.flush(); 
    // 向服务器发送处理对象命令(LIST,RETR,及STOR) 
    ctrlOutput.println(ctrlcmd); 
    ctrlOutput.flush(); 
   
    // 接受与服务器的连接 
    String info = ctrlInput.readLine();
    if(info.isEmpty())
    	return null;
    String[] strArray = info.split(" ");
    System.out.println(info);
    if(strArray[0].trim().equals("550")||strArray[0].trim().equals("553")) {
    	FtpPanel.editTextArea(info);
    	return null;
    }
    dataSocket = serverDataSocket.accept(); 
    serverDataSocket.close(); 
   } catch(SocketTimeoutException s) {
	   System.out.println("超时5s，远程服务器未应答!");
	   FtpPanel.editTextArea("超时5s，远程服务器未应答!");
    }catch (Exception e) { 
    	e.printStackTrace(); 
    	System.exit(1); 
   } 
   return dataSocket; 
} 
// doAscii方法 
// 设置文本传输模式 
public void doAscii() { 
   try { 
    ctrlOutput.println("TYPE A");// A模式 
    ctrlOutput.flush(); 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 
// doBinary方法 
// 设置二进制传输模式 
public void doBinary() { 
   try { 
    ctrlOutput.println("TYPE I");// I模式 
    ctrlOutput.flush(); 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 
// doGet方法 
// 取得服务器上的文件 
public void doGet(String fileName,String loafile) { 
   //String fileName = ""; 
   //String loafile=fileName;
   BufferedReader lineread = new BufferedReader(new InputStreamReader( 
     System.in)); 
   try { 
    int n; 
    byte[] buff = new byte[1024]; 
    // 指定服务器上的文件名 
    System.out.println("远程文件名: "+fileName); 
    //fileName = lineread.readLine(); 
    // 在客户端上准备接收用的文件 
    System.out.println("本地文件: "+loafile); 
    //loafile=lineread.readLine(); 
    File local=new File(loafile); 
    FileOutputStream outfile = new FileOutputStream(local); 
    // 构造传输文件用的数据流 
    Socket dataSocket = dataConnection("RETR " + fileName); 
    if(dataSocket==null)
    	return;
    BufferedInputStream dataInput = new BufferedInputStream(dataSocket 
      .getInputStream()); 
    // 接收来自服务器的数据，写入本地文件 
    while ((n = dataInput.read(buff)) > 0) { 
     outfile.write(buff, 0, n); 
    } 
    FtpPanel.editTextArea("文件已保存到 "+ loafile);
    dataSocket.close(); 
    outfile.close(); 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 
// doPut方法 
// 向服务器发送文件 
public void doPut(String fileName,String localFileName) { 
   //String fileName = ""; 
	if(fileName.isEmpty()) {
		System.out.println("文件名不能为空!");
		FtpPanel.editTextArea("文件名不能为空!");
	}
   BufferedReader lineread = new BufferedReader(new InputStreamReader( 
     System.in)); 
  
   try { 
    int n; 
    byte[] buff = new byte[1024]; 
    FileInputStream sendfile = null; 
    // 指定文件名 
    System.out.println("本地文件: "+fileName); 
    //fileName = lineread.readLine(); 
   
    // 准备读出客户端上的文件 
    //BufferedInputStream dataInput = new BufferedInputStream(new FileInputStream(fileName)); 
    try { 
    
     sendfile = new FileInputStream(fileName); 
    } catch (Exception e) { 
     System.out.println("文件不存在"); 
     return; 
    } 
            System.out.println("远程文件保存为: "+localFileName); 
            String lonfile = localFileName; 
    // 准备发送数据的流 
    Socket dataSocket = dataConnection("STOR " + lonfile); 
    FtpPanel.editTextArea("正在发送中...");
    if(dataSocket==null) {
    	FtpPanel.editTextArea("错误553,此文件远程已存在或本地不存在,不能上传");
    	return;
    }
    OutputStream outstr = dataSocket.getOutputStream(); 
    while ((n = sendfile.read(buff)) > 0) { 
     outstr.write(buff, 0, n); 
    } 
    System.out.println("远程文件以上传至ftp!");
    FtpPanel.editTextArea("本地文件"+fileName+"以上传至ftp服务器");
   
    dataSocket.close(); 
    sendfile.close(); 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 
// execCommand方法 
// 执行与各命令相应的处理 
public boolean execCommand(String command) { 
   boolean cont = true;
   String fileName="";
   String localFileName="null";
   switch (Integer.parseInt(command)) { 
   case 1: // 显示服务器目录信息 
//	ctrlOutput.println("OPTS UTF8 ON "); 
    doLs(); 
    break; 
   case 2: // 切换服务器的工作目录 
    doCd(""); 
    break; 
   case 3: // 取得服务器上的文件 
    doGet(fileName,localFileName); 
    break; 
   case 4: // 向服务器发送文件 
    doPut(fileName,localFileName); 
    break; 
   case 5: // 文件传输模式 
    doAscii(); 
    break; 
   case 6: // 二进制传输模式 
    doBinary(); 
    break; 
   case 7: // 处理结束 
    doQuit(); 
    cont = false; 
    break; 
/////////////////////////////////////////////////
//以上为了正常用户使用，以下为系统接口
//   case 101:
//	   sys_qr_getdirs();
//	   
//	   break;
   default: // 其他输入 
    System.out.println("请选择一个序号"); 
   } 
   return (cont); 
} 
// main_proc方法 
// 输出ftp的命令菜单，调用各种处理方法 
public void main_proc(String username,String password) throws IOException { 
   boolean cont = true; 
   try { 
    doLogin(username,password); 
    //while (cont) { 
   //  showMenu(); 
   //  cont = execCommand(getCommand()); 
    //} 
   } catch (Exception e) { 
	System.err.print(e); 
    System.exit(1); 
   } 
} 
// getMsgs方法 
// 启动从控制流收信的线程 
public void getMsgs() { 
   try { 
    CtrlListen listener = new CtrlListen(ctrlInput); 
    Thread listenerthread = new Thread(listener); 
    listenerthread.start(); 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 
// main方法 
// 建立TCP连接，开始处理 
/*public static void main(String[] arg) { 
  
   try { 
    Ftp f = null; 
    f = new Ftp(); 
    f.openConnection("172.24.168.169"); // 控制连接建立,设置为自己的IP. 
    f.getMsgs(); // 启动接收线程 
    f.main_proc(); // ftp 处理 
    f.closeConnection(); // 关闭连接 
    System.exit(0); // 结束程序 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} */
} 
// 读取控制流的CtrlListen 类 
class CtrlListen implements Runnable { 
BufferedReader ctrlInput = null; 
public CtrlListen(BufferedReader in) { 
   ctrlInput = in; 
}
public void run() { 
   while (true) { 
    try { 
        // 按行读入并输出到标准输出上 
        //System.out.println(ctrlInput.readLine()); 
    	String line=ctrlInput.readLine();
    	String strArr[] = line.split(" ");
    	if(strArr[0].trim().equals("500"))
    		return;
    	else FtpPanel.editTextArea(line);
    	//FtpPanel.editTextArea(line);
     } catch (Exception e) { 
     System.exit(1); 
    } 
   } 
} 
} 