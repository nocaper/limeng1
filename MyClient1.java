package org.neu.jpcap.kengdie;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient1 {
	private ServerSocket ss;
	private HashMap<String,String> hm;
	public MyClient1(int port) throws IOException{
		ss = new ServerSocket(port);
		ss.setSoTimeout(10000);
		hm = new HashMap<String, String>();
		hm.put("a.txt", "172.16.0.243");
		hm.put("b.txt", "172.16.0.243");
		hm.put("c.txt", "172.16.0.243");
		hm.put("d.txt", "172.16.0.243");
	}
	public void submit() throws UnknownHostException, IOException{
		Socket socket = new Socket("127.0.0.1", 8889);
		DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
		String key = "b.txt";
		dout.write(key.getBytes());
//		dout.close();
		
		InputStream input = socket.getInputStream();
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		String getString;
		getString=reader.readLine();
		input.close();
		System.out.println(getString);
		socket.close();
	    
		
		socket  = new Socket(getString, 9000);
	    OutputStream outstream = socket.getOutputStream();
		outstream.write(key.getBytes());
		input = socket.getInputStream();
		FileOutputStream fos = new FileOutputStream(new File("F:\\"+key));
		int tmp = 0;
		while((tmp=input.read())!=-1){
			fos.write(tmp);
		}
		fos.close();
		outstream.close();
		socket.close();
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Socket server =  ss.accept();
				DataInputStream din = new DataInputStream(server.getInputStream());
				byte buffer[] = new byte[1024];
				
				int tmp = din.read(buffer);
				String key = new String(buffer,0,tmp);
				System.out.println("you have received a request!");
				if(hm.containsKey(key)){
					DataOutputStream dout = new DataOutputStream(server.getOutputStream());
					System.out.println(key);
					dout.write(hm.get(key).getBytes());
					dout.close();
				}
				else{
					DataOutputStream dout = new DataOutputStream(server.getOutputStream());
					String str = "There is no this file here!";
					System.out.println(key);
					dout.write(str.getBytes());
					dout.close();
				}
				
//				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
//				System.out.println("接受超时");
			}
		}
	}
}
