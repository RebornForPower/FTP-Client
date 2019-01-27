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
Socket ctrlSocket;// ������Socket 
public PrintWriter ctrlOutput;// ��������õ��� 
public BufferedReader ctrlInput;// ���������õ��� 
private int CTRLPORT = 21;// ftp �Ŀ����ö˿� 
ServerSocket serverDataSocket; 
// openConnection���� 
// �ɵ�ַ�Ͷ˿ںŹ���Socket���γɿ����õ��� 

/** �����ַ����� */
private static String LOCAL_CHARSET = "GBK";
 
// FTPЭ�����棬�涨�ļ�������Ϊiso-8859-1
private static String SERVER_CHARSET = "UTF-8";
 

public void openConnection(String host) throws IOException, 
    UnknownHostException { 
   CTRLPORT=FtpPanel.getPort();//�˿ںŻ�ȡ
   ctrlSocket = new Socket(host, CTRLPORT); 
   ctrlOutput = new PrintWriter(ctrlSocket.getOutputStream()); 
   ctrlInput = new BufferedReader(new InputStreamReader(ctrlSocket 
     .getInputStream())); 
} 
// closeConnection���� 
// �رտ����õ�Socket 
public void closeConnection() throws IOException { 
   ctrlSocket.close(); 
} 
// showMenu���� 
// ���ftp������˵� 
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
// getCommand���� 
// ��ȡ�û�ָ����������� 
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
//���û����� ³�����	
//	if(host.isEmpty())
//		JOptionPane.showMessageDialog(null, "������ʾ:Hostname����Ϊ�գ�����������","����",JOptionPane.ERROR_MESSAGE); 
//	
   try { 
    //System.out.println("�������û���"); 
    //loginName = lineread.readLine(); 
   
    ctrlOutput.println("USER " + username); 
    ctrlOutput.flush(); 
    //System.out.println("���������"); 
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
	System.out.println("δ���ӷ�����,û����Server����QUIT����");
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 
public void doCd(String dirName) { 
   //String dirName = ""; 
   BufferedReader lineread = new BufferedReader(new InputStreamReader( 
     System.in)); 
   try { 
    //System.out.println("������Ŀ¼��");  
    //dirName = lineread.readLine(); 
	   if (dirName.isEmpty()) {
		   FtpPanel.editTextArea("������Ŀ¼��");
		   return;
	   }
	
    ctrlOutput.println("CWD " + dirName);// CWD���� 
    ctrlOutput.flush(); 
    boolean dir_ok = false;
	for (int i=0;i<FtpPanel.root.getChildCount();i++) {
//			System.out.println("����ӽڵ�"+FtpPanel.root.getChildAt(i).toString());		
			if(FtpPanel.root.getChildAt(i).toString().equals(dirName)) {
				dir_ok =true;
//				FtpPanel.current =  FtpPanel.root.getChildAt(i).;
			}
	}
	if(dir_ok!=true)
		JOptionPane.showMessageDialog(null, "������ʾ���ļ������ԣ�����������","����",JOptionPane.ERROR_MESSAGE);
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
	    ctrlOutput.println("CWD " + dirName);// CWD���� 
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
//	    	System.out.print("��"+a);
//	    }
	    return dirnames;
	   } catch(IOException e) {
		 return null;
	   }catch (Exception e) { 
		   return null;
	   } 
}


// doLs���� 
// ȡ��Ŀ¼��Ϣ 

public void doLs() { 
   try { 
    int n; 
    boolean flag=false;
    String line="";
    byte[] buff = new byte[65530]; 
   
    // ������������ 
    Socket dataSocket = dataConnection("NLST "); 
   
    // ׼����ȡ�����õ��� 
    BufferedInputStream dataInput = new BufferedInputStream(dataSocket 
      .getInputStream()); 
    // ��ȡĿ¼��Ϣ 
   
    while ((n = dataInput.read(buff)) > 0) { 
     System.out.write(buff, 0, n); 
     if(buff==null) break;
     
     line+=new String(buff);
//     System.out.println(buff+"����");
    } 
//������utf8 ����gbk ������������
    line = new String(line.getBytes(LOCAL_CHARSET),
    		SERVER_CHARSET);
   

//����Ŀ¼���ڵ�
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
    //���س��з��ַ����������ļ���
//    System.out.println("����"+arr[3]);
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
//    	System.out.println("����"+a+"����");
//    }
    
//    FileWriter writer;
//    try {
//        writer = new FileWriter("F:\\code\\java\\����.txt");
//        writer.write(line);
//        writer.flush();
//        writer.close();
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//�ı����������ʾûë��
//https://blog.csdn.net/gingerredjade/article/details/62036205 FtpClient��������������
//    https://www.rgagnon.com/javadetails/java-0324.html
//	JOptionPane.showMessageDialog(null, "���͵�û�м���","����",JOptionPane.ERROR_MESSAGE);  

   } catch(IOException e) {
	   System.out.println("����������!");
	   FtpPanel.editTextArea("����������");
   }catch (Exception e) { 
    e.printStackTrace(); 
    //System.exit(1); 
   } 
} 


// dataConnection���� 
// ��������������������õ�Socket 
// ����PORT����˿�֪ͨ������ 
public Socket dataConnection(String ctrlcmd) { 
   String cmd = "PORT "; // PORT�����PORT��������ݵı��� 
   int i; 
   Socket dataSocket = null;// ����������Socket 
   try { 
    // �õ��Լ��ĵ�ַ 
    byte[] address = InetAddress.getLocalHost().getAddress(); 
    // ���ʵ��Ķ˿ںŹ�������� 
    serverDataSocket = new ServerSocket(0,1); 
   // serverDataSocket.setSoTimeout(50000000);
    serverDataSocket.setSoTimeout(5000);
    // ׼������PORT�����õ����� 
    for (i = 0; i < 4; ++i) 
     cmd = cmd + (address[i] & 0xff) + ","; 
    cmd = cmd + (((serverDataSocket.getLocalPort()) / 256) & 0xff) 
      + "," + (serverDataSocket.getLocalPort() & 0xff); 
    // ���ÿ����õ�������PORT���� 
    ctrlOutput.println(cmd); 
   
    ctrlOutput.flush(); 
    // ����������ʹ����������(LIST,RETR,��STOR) 
    ctrlOutput.println(ctrlcmd); 
    ctrlOutput.flush(); 
   
    // ����������������� 
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
	   System.out.println("��ʱ5s��Զ�̷�����δӦ��!");
	   FtpPanel.editTextArea("��ʱ5s��Զ�̷�����δӦ��!");
    }catch (Exception e) { 
    	e.printStackTrace(); 
    	System.exit(1); 
   } 
   return dataSocket; 
} 
// doAscii���� 
// �����ı�����ģʽ 
public void doAscii() { 
   try { 
    ctrlOutput.println("TYPE A");// Aģʽ 
    ctrlOutput.flush(); 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 
// doBinary���� 
// ���ö����ƴ���ģʽ 
public void doBinary() { 
   try { 
    ctrlOutput.println("TYPE I");// Iģʽ 
    ctrlOutput.flush(); 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 
// doGet���� 
// ȡ�÷������ϵ��ļ� 
public void doGet(String fileName,String loafile) { 
   //String fileName = ""; 
   //String loafile=fileName;
   BufferedReader lineread = new BufferedReader(new InputStreamReader( 
     System.in)); 
   try { 
    int n; 
    byte[] buff = new byte[1024]; 
    // ָ���������ϵ��ļ��� 
    System.out.println("Զ���ļ���: "+fileName); 
    //fileName = lineread.readLine(); 
    // �ڿͻ�����׼�������õ��ļ� 
    System.out.println("�����ļ�: "+loafile); 
    //loafile=lineread.readLine(); 
    File local=new File(loafile); 
    FileOutputStream outfile = new FileOutputStream(local); 
    // ���촫���ļ��õ������� 
    Socket dataSocket = dataConnection("RETR " + fileName); 
    if(dataSocket==null)
    	return;
    BufferedInputStream dataInput = new BufferedInputStream(dataSocket 
      .getInputStream()); 
    // �������Է����������ݣ�д�뱾���ļ� 
    while ((n = dataInput.read(buff)) > 0) { 
     outfile.write(buff, 0, n); 
    } 
    FtpPanel.editTextArea("�ļ��ѱ��浽 "+ loafile);
    dataSocket.close(); 
    outfile.close(); 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 
// doPut���� 
// ������������ļ� 
public void doPut(String fileName,String localFileName) { 
   //String fileName = ""; 
	if(fileName.isEmpty()) {
		System.out.println("�ļ�������Ϊ��!");
		FtpPanel.editTextArea("�ļ�������Ϊ��!");
	}
   BufferedReader lineread = new BufferedReader(new InputStreamReader( 
     System.in)); 
  
   try { 
    int n; 
    byte[] buff = new byte[1024]; 
    FileInputStream sendfile = null; 
    // ָ���ļ��� 
    System.out.println("�����ļ�: "+fileName); 
    //fileName = lineread.readLine(); 
   
    // ׼�������ͻ����ϵ��ļ� 
    //BufferedInputStream dataInput = new BufferedInputStream(new FileInputStream(fileName)); 
    try { 
    
     sendfile = new FileInputStream(fileName); 
    } catch (Exception e) { 
     System.out.println("�ļ�������"); 
     return; 
    } 
            System.out.println("Զ���ļ�����Ϊ: "+localFileName); 
            String lonfile = localFileName; 
    // ׼���������ݵ��� 
    Socket dataSocket = dataConnection("STOR " + lonfile); 
    FtpPanel.editTextArea("���ڷ�����...");
    if(dataSocket==null) {
    	FtpPanel.editTextArea("����553,���ļ�Զ���Ѵ��ڻ򱾵ز�����,�����ϴ�");
    	return;
    }
    OutputStream outstr = dataSocket.getOutputStream(); 
    while ((n = sendfile.read(buff)) > 0) { 
     outstr.write(buff, 0, n); 
    } 
    System.out.println("Զ���ļ����ϴ���ftp!");
    FtpPanel.editTextArea("�����ļ�"+fileName+"���ϴ���ftp������");
   
    dataSocket.close(); 
    sendfile.close(); 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} 
// execCommand���� 
// ִ�����������Ӧ�Ĵ��� 
public boolean execCommand(String command) { 
   boolean cont = true;
   String fileName="";
   String localFileName="null";
   switch (Integer.parseInt(command)) { 
   case 1: // ��ʾ������Ŀ¼��Ϣ 
//	ctrlOutput.println("OPTS UTF8 ON "); 
    doLs(); 
    break; 
   case 2: // �л��������Ĺ���Ŀ¼ 
    doCd(""); 
    break; 
   case 3: // ȡ�÷������ϵ��ļ� 
    doGet(fileName,localFileName); 
    break; 
   case 4: // ������������ļ� 
    doPut(fileName,localFileName); 
    break; 
   case 5: // �ļ�����ģʽ 
    doAscii(); 
    break; 
   case 6: // �����ƴ���ģʽ 
    doBinary(); 
    break; 
   case 7: // ������� 
    doQuit(); 
    cont = false; 
    break; 
/////////////////////////////////////////////////
//����Ϊ�������û�ʹ�ã�����Ϊϵͳ�ӿ�
//   case 101:
//	   sys_qr_getdirs();
//	   
//	   break;
   default: // �������� 
    System.out.println("��ѡ��һ�����"); 
   } 
   return (cont); 
} 
// main_proc���� 
// ���ftp������˵������ø��ִ����� 
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
// getMsgs���� 
// �����ӿ��������ŵ��߳� 
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
// main���� 
// ����TCP���ӣ���ʼ���� 
/*public static void main(String[] arg) { 
  
   try { 
    Ftp f = null; 
    f = new Ftp(); 
    f.openConnection("172.24.168.169"); // �������ӽ���,����Ϊ�Լ���IP. 
    f.getMsgs(); // ���������߳� 
    f.main_proc(); // ftp ���� 
    f.closeConnection(); // �ر����� 
    System.exit(0); // �������� 
   } catch (Exception e) { 
    e.printStackTrace(); 
    System.exit(1); 
   } 
} */
} 
// ��ȡ��������CtrlListen �� 
class CtrlListen implements Runnable { 
BufferedReader ctrlInput = null; 
public CtrlListen(BufferedReader in) { 
   ctrlInput = in; 
}
public void run() { 
   while (true) { 
    try { 
        // ���ж��벢�������׼����� 
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