package mycontrols;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class PlaceHolder implements FocusListener
{
	private JTextField place;
	private String text;
	private String message;
	public PlaceHolder(JTextField tf, String msg)
	{
		place = tf;
		message = msg;
	}
	public PlaceHolder(JPasswordField tf, String msg)
	{
		place = tf;
		message = msg;
	}
	public PlaceHolder(JTextField tf)
	{
		place = tf;
		message = "Enter the message"; 
	}
	public void focusLost(FocusEvent fe)
	{
		text = place.getText();
		if( text == null || text.length() < 1 )
			place.setText(message);
		else if(text.length() > 1 && text.equals(message) == false)
			place.setText(text);
		else
			place.setText("");
	}
	public void focusGained(FocusEvent fe)
	{
		text = place.getText();
		if( text.equals(message))
			place.setText("");
		else
			place.setText(text);	
	}	
}