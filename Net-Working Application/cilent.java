import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class cilent extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Cilent Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    public cilent()
    {
        try {
            System.out.println("Request is sending to server");
            socket = new Socket("127.0.0.1",7778);
            System.err.println("Connection Done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream()); 

            
             startReading();
             startWriting();
             createGUI();
             handleEvents();

            
        } catch (Exception e) {
        }
            

    }
    private void handleEvents(){


        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e)
            {

            }

            @Override
            public void keyPressed(KeyEvent e)
            {

            }
            @Override
            public void keyReleased(KeyEvent e)
            {
                System.out.println("Key released "+e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                    String contentToSend = messageInput.getText();  
                    messageArea.append("Me: "+contentToSend +"\n");
                    out.println(contentToSend); 
                    out.flush();   
                    messageInput.setText(""); 
                    messageInput.requestFocus();         
                }
            }
            
        });

    }

    private void createGUI()
    {
        this.setTitle("Cilent Messager[End]");
        this.setSize(700,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
    
        //coding for component
        
    
        heading.setFont(font);
       

        messageArea.setFont(font);
        messageInput.setFont(font);
       
        heading.setIcon(new ImageIcon("cicon.png"));
       
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        
        
       
        
        //settign layout
        this.setLayout(new BorderLayout());
        //adding component to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);

        

        

        
        

    }
    
    public void startReading()
    {
        //tread - read krky dega
        Runnable r1= () -> {
            System.out.println("reader started..");
            try {
            while (true) { 
                
                    
               
                String msg=br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this, "server terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                //System.out.println("Server: "+msg);
                messageArea.append("Server: "+msg+"\n");
            } 
                
            }
            catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };
        new Thread(r1).start();

    }
    public void startWriting()
    {
        //user se data lega aur send kreyga cilent tk
        Runnable r2= () -> {
            System.out.println("writer started..");

            try{
            while (true && !socket.isClosed()) {
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                        
                    }


                    
                } 
            }
            catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Connection is closed");
            }
            System.out.println("Connection is closed");
            
        };
        new Thread(r2).start();

    }

   

    public static void main(String[] args) {
        new cilent();
        
    }
    
}
