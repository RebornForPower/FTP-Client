package socket;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

//ip扫描类,用于多线程扫描IP地址
public class IPScan {
	private String StartIP;
	protected String StartIPPort;
	private String Range;
	private int threadNum;
	private int fetchedNum;
	public static int Port;
	private Queue<String> allIp; //任务队列,存放IP地址用
	public void startScan() {
		fetchedNum=0;//任务队列取得的任务数
		threadNum=1000;//线程数
		StartIP = FtpPanel.getStartIP();
		StartIPPort = FtpPanel.getStartIPPort();
		Range = FtpPanel.getRange();
		if(StartIP.isEmpty()||StartIPPort.isEmpty()||Range.isEmpty()) {
			FtpPanel.editTextArea("您有一个或多个参数为空，请正确填入!");
			return;
		}
		
		//检测StartIP是否为IP地址 [finished]
		System.out.println(StartIP);
		//判断StartIPPort是否为整数 [finished]
		
		//range fanwei <10000 [finished]
		
			
		//StartIPPort范围是0~65535 [finished]
		if(Integer.parseInt(StartIPPort)<0||Integer.parseInt(StartIPPort)>65535) {
			FtpPanel.editTextArea("不在0~65535范围内!");
		// ok, it will never be executed.
			return;
		}
		
		Port = Integer.parseInt(StartIPPort);
		//组建任务队列
		allIp = new LinkedList<String>();
		/*for (int i = 220; i < 245; i++) {
            for (int j = 0; j < 256; j++) {
                allIp.offer("202.195."+i+"."+j);
                }//202.195.244.88
            }*/
		String ip;
		ip = this.StartIP;
		long ip_long=0;
		for(int i=0;i<Integer.parseInt(Range);i++) {
			allIp.offer(ip);
			long[] lip=new long[4];
			//先找到IP地址字符串中.的位置
			int position1=ip.indexOf(".");
			int position2=ip.indexOf(".",position1+1);
			int position3=ip.indexOf(".",position2+1);
			//将每个.之间的字符串转换成整型
			lip[0]=Long.parseLong(ip.substring(0,position1));
			lip[1]=Long.parseLong(ip.substring(position1 + 1,position2));
			lip[2]=Long.parseLong(ip.substring(position2 + 1,position3));
			lip[3]=Long.parseLong(ip.substring(position3 + 1));
			ip_long = (lip[0] << 24) + (lip[1] << 16) + (lip[2] << 8) + lip[3];

			ip_long++;
			//数值的ip转字符串
			StringBuffer sb = new StringBuffer("");
			// 直接右移24位
			sb.append(String.valueOf((ip_long >>> 24)));
			sb.append(".");
			// 将高8位置0，然后右移16位
			sb.append(String.valueOf((ip_long & 0x00FFFFFF) >>> 16));
			sb.append(".");
			// 将高16位置0，然后右移8位
			sb.append(String.valueOf((ip_long & 0x0000FFFF) >>> 8));
			sb.append(".");
			sb.append(String.valueOf((ip_long & 0x000000FF)));
			// 将高24位置0
			ip = sb.toString();
		}
			
        //线程池开始工作
		startTest();
		System.out.println(StartIP);
	}
	//创建线程池,多任务工作
//	192.168.243.77
	public boolean check_input_values_ok() {
	    //System.out.println(ipAdd.isIP(oneAddress));  
		StartIP = FtpPanel.getStartIP();
		StartIPPort = FtpPanel.getStartIPPort();
		Range = FtpPanel.getRange();
		if(this.isIP(StartIP)==false) {
			System.out.println(StartIP+" ip无效");
			JOptionPane.showMessageDialog(null, "友情提示：起始IP "+StartIP+"不是一个合法的地址，请重新输入","警告",JOptionPane.ERROR_MESSAGE); 
			return false;
		}
		if(isNumeric(Range)!=true) {
			System.out.println(Range+"非数字");
			JOptionPane.showMessageDialog(null, "友情提示：Range "+StartIP+"非数字，请重新输入","警告",JOptionPane.ERROR_MESSAGE); 
			return false;
		}
		if(isNumeric(StartIPPort)!=true) {
			System.out.println(StartIPPort+"非数字");
			JOptionPane.showMessageDialog(null, "友情提示：端口 "+StartIP+"非数字，请重新输入","警告",JOptionPane.ERROR_MESSAGE); 
			return false;
		}
		if(Integer.parseInt(this.Range)>10000) {
			FtpPanel.setRange(10000);
			Range = String.valueOf(10000);
			JOptionPane.showMessageDialog(null, "友情提示：您输入的线程数过多，已为您重置为10000","警告",JOptionPane.WARNING_MESSAGE); 

		}
		if(Integer.parseInt(StartIPPort)<0||Integer.parseInt(StartIPPort)>65535) {
			FtpPanel.setRange(10000);
			Range = String.valueOf(10000);
			JOptionPane.showMessageDialog(null, "友情提示：端口"+Range+"不在0~65535范围内!，请重新输入","警告",JOptionPane.WARNING_MESSAGE); 
		}
		
		return true;
	}
	
	//判断oneAddress是否是IP  
    //System.out.println(ipAdd.isIP(oneAddress)); 
	public boolean isIP(String addr)  
    {  
        if(addr.length() < 7 || addr.length() > 15 || "".equals(addr))  {  
            return false;  
        }  
        /** 
         * 判断IP格式和范围 
         */  
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";            
        Pattern pat = Pattern.compile(rexp);    
        Matcher mat = pat.matcher(addr);              
        boolean ipAddress = mat.find();  
        return ipAddress;  
    }  

	public static boolean isNumeric(String str)
	{
	  try{
		  	Integer.parseInt(str);
	   		return true;
	  }catch(NumberFormatException e)
	  {
		  JOptionPane.showMessageDialog(null, "友情提示："+str+"是非法字符，请重新输入","警告",JOptionPane.ERROR_MESSAGE); 
		  return false;
	  }
	}
	public void startTest() {
		
		threadNum = Integer.parseInt(this.Range);
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        
        for (int i = 0; i < threadNum; i++) {
            executor.execute(new PingRunner());
        }
        executor.shutdown();
        try {
            while (!executor.isTerminated()) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Fetched num is "+fetchedNum);
	}
	
	private class PingRunner implements Runnable {
        private String taskIp = null;
        private Socket connect;
        public void run() {
            try {
            	connect = new Socket();
                while ((taskIp = getIp()) != null) {
                	connect.connect(new InetSocketAddress(taskIp, Integer.parseInt(IPScan.this.StartIPPort)),5000);
                	boolean res = connect.isConnected();//InetAddress addr = InetAddress.getByName(taskIp);
                	PrintResult(res);
                    connect.close();
                }
            } catch (SocketTimeoutException e) {
                System.out.println("host ["+taskIp+"] Timeout");
               // FtpPanel.editTextArea("host ["+taskIp+"] Timeout");
                //e.printStackTrace();
            } catch (ConnectException e) {
                System.out.println("host ["+taskIp+"] Connection refused");
               // FtpPanel.editTextArea("host ["+taskIp+"] Connection refused");
                e.printStackTrace();
            }catch (SocketException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public String getIp() {
            String ip = null;
            synchronized (allIp) {
                ip = allIp.poll();
            }
            if (ip != null) {
                fetchedNum++;
            }
            return ip;
        }
        public synchronized void PrintResult(boolean res) {
        	if (res) {//测试5s内是否有链接
                System.out.println("host ["+taskIp+"] is reachable");
                FtpPanel.editTextArea("host ["+taskIp+"] is reachable");
            } else {
                System.out.println("host ["+taskIp+"] is not reachable");
                FtpPanel.editTextArea("host ["+taskIp+"] is not reachable");
            }
        }
    }
}


