package com.baozun.nebula.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.io.Util;


public class ExecuteCommand {
	public static void execute(String command) {
		Process process = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			process = Runtime.getRuntime().exec(command);

			readWrite(new BufferedInputStream(process.getErrorStream()),new BufferedInputStream(process.getInputStream()), out, System.in, System.out); // 该方法借用common-net的测试例子部分的一个工具类的方法
			
		 } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	      } finally {  
	            try {  
	                if (null != out) {  
	                    out.close();  
	                    out = null;  
	                }  
	                if (null != in) {  
	                    in.close();  
	                    in = null;  
	                }  
	                int value = process.waitFor();  
	                if (null != process)  
	                    process.destroy();  
	            } catch (IOException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            } catch (InterruptedException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }  
	        }  
	
	}


	public static final void readWrite(final InputStream remoteInput,final InputStream errorInput, final OutputStream remoteOutput, final InputStream localInput, final OutputStream localOutput) {
		Thread reader, writer;

		reader = new Thread() {
			@Override
			public void run() {
				int ch;

				try {
					while (!interrupted() && (ch = localInput.read()) != -1) {
						remoteOutput.write(ch);
						remoteOutput.flush();
					}
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		};

		writer = new Thread() {
			@Override
			public void run() {
				try {
					//Util.copyStream(remoteInput, localOutput);
					Util.copyStream(errorInput, localOutput);

				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		};

		writer.setPriority(Thread.currentThread().getPriority() + 1);

		writer.start();
		reader.setDaemon(true);
		reader.start();

		try {
			writer.join();
			reader.interrupt();
		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] args) {

		//ExecuteCommand.execute("svn merge http://10.8.12.100/svn/jumbo/nebula/nebula-repo/branches/5.0.0b-SNAPSHOT-20140718 E:/workspace4.3/nebula-repo ");
		//ExecuteCommand.execute(" cmd.exe /c start copy C:/Users/admin/test/a.txt C:/Users/admin/a.txt");
		
		
		try {
			Process process = Runtime.getRuntime().exec("cmd.exe /c start E:/workspace4.3/protostar/target/svnbat/merge_svn.bat");
			process.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}