import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;
import javax.swing.border.*;
import mycontrols.*;


public class ClientFrame extends JFrame implements Runnable
{
	private Thread t;
	public JTextArea mainField;
	public JTextField message;
	public JButton send, exit;
	private JPanel south;
	private Border etched, title;
	private SendActionPerformed sap;
	private PlaceHolder place;
	private ImageIcon logo;
	public ClientFrame()
	{
		super("Client Side");
		
		try
		{
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		initAndSetLayout();
		setCustomizedUI();
		addingListeners();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(545,600);
		setResizable(false);
		setVisible(true);
		t.start();
	}
	private void initAndSetLayout()
	{
		t = new Thread(this, "Client Thread");
		sap = new SendActionPerformed(this);
		logo = new ImageIcon("nikhil.png");
		south = new JPanel();
		etched = BorderFactory.createEtchedBorder();
		title = BorderFactory.createTitledBorder(etched, "Conversation");
		mainField = new JTextArea();
		message = new JTextField(25);
		send = new JButton("Send -->");
		exit = new JButton("--X-- Close Connection --X--");
		place = new PlaceHolder(message);
	}
	private void setCustomizedUI()
	{
		mainField.setBorder(title);
		mainField.setEnabled(false);
		mainField.setDisabledTextColor(Color.WHITE);		
	
		south.add(exit);
		south.add(message);
		south.add(send);
		
		add(new JScrollPane(mainField), BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
		//add(new JLabel(logo), BorderLayout.NORTH);
	}
	private void addingListeners()
	{
		message.addActionListener(sap);
		send.addActionListener(sap);
		exit.addActionListener(sap);
		message.addFocusListener(place);

		addWindowFocusListener(new WindowAdapter()
		{
			public void windowGainedFocus(WindowEvent we)
			{
				message.requestFocus();
			}
		});
	}
	public static void main(String[] args) 
	{
		new ClientFrame();
	}
	@Override
	public void run()
	{
		try
		{
			sap.runMethod();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}

class SendActionPerformed implements ActionListener
{
	private static Socket socket;
	private static BufferedReader in;
	private static PrintWriter out;
	private static ClientFrame perform;
	private static String inString, outString;
	private static boolean display = true;
	SendActionPerformed(ClientFrame obj)
	{
		try
		{
			perform = obj;
			socket = new Socket("127.0.0.1", 4444);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);	
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if ( ae.getSource() == perform.send || ae.getSource() == perform.message )
			{	 
				perform.mainField.append("\t\t\t\t\tMe: "+perform.message.getText() + "\n");
				try
				{
					out.println("Client: " + perform.message.getText() + "\n");
					if(!socket.isConnected())
						throw new SocketException();
					perform.message.setText("");	
				}	
				catch(SocketException e)
				{
					socket.close();
					perform.message.setEnabled(false);
					perform.send.setEnabled(false);
					perform.exit.setEnabled(false);
					JOptionPane.showMessageDialog(perform,"Connection Closed");
				}
			}
			/*else if (ae.getSource() == perform.recieve)
			{
				inString = in.readLine();
				if (inString.length() > 0 && display == true) 
				{
					perform.mainField.append(inString + "\n");
					display = false;
					this.actionPerformed(ae);
				}
				else if(display == false)
				{
					display = true;
				}	
			}*/
			else if (ae.getSource() == perform.exit)
			{
				socket.close();
				perform.message.setEnabled(false);
				perform.send.setEnabled(false);
				perform.exit.setEnabled(false);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	public void runMethod() throws Exception
	{
		//System.out.println("Inside Run Method");
		inString = in.readLine();
		if (inString.length() > 0 && display == true) 
		{
			perform.mainField.append(inString + "\n");
			display = false;
			//this.actionPerformed(ae);
		}
		else if(display == false)
		{
			display = true;
		}
		if(socket.isConnected() == true)
			perform.run();
	}
}