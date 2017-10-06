import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JSlider;

import com.fazecast.jSerialComm.*;
public class SerialData {
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	public static void main(String[] args){
		//Getting Current Time
		LocalDateTime now=LocalDateTime.now();
		//Graphical Interface 
		/*JFrame window=new JFrame();
		JSlider slider=new JSlider();
		slider.setMaximum(150);
		window.add(slider);
		window.pack();
		window.setVisible(true);*/
		
		//Prepare File to write into
		String Filename="Output Data.txt";
		
		
		//Serial Comm
		SerialPort[] ports = SerialPort.getCommPorts();
		System.out.println("Select a port:");
		int i = 1;
		for(SerialPort port : ports)
			System.out.println(i++ +  ": " + port.getSystemPortName());
		Scanner s = new Scanner(System.in);
		int chosenPort = s.nextInt();

		SerialPort serialPort = ports[chosenPort - 1];
		if(serialPort.openPort())
			System.out.println("Port opened successfully.");
		else {
			System.out.println("Unable to open the port.");
			return;
		}
		serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

		Scanner data = new Scanner(serialPort.getInputStream());
		int value = 0;
		try {
			PrintWriter OutputStream=new PrintWriter(Filename);
			while(data.hasNextLine()){
				try{value = Integer.parseInt(data.nextLine());}catch(Exception e){}
				OutputStream.println(dtf.format(now)+"  "+value);
				System.out.println(data.hasNextLine());	
			}
			OutputStream.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Done.");
	}
}
