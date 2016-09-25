import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


@SuppressWarnings("serial")
public class MySnake extends Applet implements Runnable , KeyListener ,MouseListener
{
	short dirx[]=new short[100];
	short diry[]=new short[100];
	short length;
	
	final short runfast=90,runfaster=65,runfastest=40;
	final short sizeofsnake=12;
	final short mainstartx=(short) (150/sizeofsnake*sizeofsnake);
	final short mainstarty=(short) (125/sizeofsnake*sizeofsnake);
	final short mainendx=(short) ( (850+mainstartx) /sizeofsnake*sizeofsnake);
	final short mainendy=(short) ( (450+mainstarty) / sizeofsnake*sizeofsnake);
	final short startpointx=(short) (350/sizeofsnake*sizeofsnake)+mainstartx;
	final short startpointy=(short) (200/sizeofsnake*sizeofsnake)+mainstarty;
	
	short runspeed=runfast;
	short snackx,snacky,bigsnackx,bigsnacky,lastx,lasty;
	short oldsnackx[]=new short[5];
	short oldsnacky[]=new short[5];
	short bigscore,score,time;
	byte scoreplus,count,counti;	
	
	double randomx=(mainendx-mainstartx-5)/sizeofsnake;
	double randomy=(mainendy-mainstarty-5)/sizeofsnake;
	
	boolean runningleft,runningright,runningup,runningdown;
	boolean start=true,newgame=true,gameover,pause,snackate,bigsnackate,temp,maze=false;
	
	void newgame()
	{
		length=8;
		time=1000;
		bigscore=score=count=counti=0;
		bigsnackx=bigsnacky=-100;
					
		runningleft=runningup=runningdown=gameover=newgame=pause=bigsnackate=false;
		runningright=snackate=temp=true;

		if(runspeed==runfast)
			scoreplus=7;
		else if(runspeed==runfaster)
			scoreplus=8;
		else
			scoreplus=9;
		
		for(short i=0;i<length;i++)
		{
			dirx[i]=(short) (startpointx-(i*sizeofsnake));
			diry[i]=startpointy;
			if(i<5)
				oldsnackx[i]=oldsnacky[i]=0;
		}
	}
	
	Thread t;
	Font F=new Font("Arial",Font.BOLD,20);
	Label L[]=new Label[5];
	Label S[]=new Label[3];
	
	public void init()
	{
		this.resize(1350,650);
		setBackground(new Color(63,63,63));	//Color.LIGHT_GRAY
		t = new Thread(this);
		t.start();
		
		L[0]=new Label("PAUSE");
		L[1]=new Label("NEW GAME");
		L[2]=new Label("MAZE");
		L[3]=new Label("LEVEL UP 2");
		L[4]=new Label("QUIT");
		
		S[0]=new Label("SNAKE");
		S[1]=new Label("Press Enter");
		S[2]=new Label("BY Brij , Nirav , Hitesh ");
		if(!start)
			menu();
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				while(!gameover&&!pause)
				{
					Thread.sleep(runspeed);
					Shifting();
				
					if(runningleft)
						dirx[0]=(short) (dirx[0]-sizeofsnake);
					else if(runningright)
						dirx[0]=(short) (dirx[0]+sizeofsnake);
					else if(runningup)
						diry[0]=(short) (diry[0]-sizeofsnake);
					else if(runningdown)
						diry[0]=(short) (diry[0]+sizeofsnake);
				
					if(!maze)
						nomaze();
					Eatting();
					if(maze)
						maze();
				
					Gameover();
					repaint();
			
				}
				while(gameover)
				{
					Thread.sleep(200);
					repaint();
				}		
			}
			catch(Exception e)
			{
				System.out.println("In Run:"+e);	
			}
		}//while(true) loop is finished here...
	}

	public void paint(Graphics G)
	{	
		if(newgame)
			newgame();
	
		if(start)
		{
			S[0].setFont(new Font("Times New Roman",Font.ITALIC,300));
			S[1].setFont(new Font("Times New Roman",Font.ITALIC,50));
			S[2].setFont(new Font("Times New Roman",Font.ITALIC,80));
			S[0].setBounds(0,0, 1350, 400);
			S[1].setBounds(0,400,1350,100);
			S[2].setBounds(0,500,1350,150);
			
			for(short i=0;i<3;i++)
			{
				S[i].setBackground(new Color(163,163,163));
				S[i].setAlignment(Label.CENTER);
				add(S[i]);
			}
			
			this.addKeyListener(this);
			requestFocus(true);
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				System.out.println("In Start: "+e);
			}
		}
		
		if(maze)
		{
			G.setColor(new Color(255,0,0));
			G.fill3DRect(mainstartx-5, mainstarty-5, mainendx-mainstartx+10, mainendy-mainstarty+10,false);
		}
		
		G.setColor(new Color(0,0,0));	
		G.fill3DRect(mainstartx, mainstarty, mainendx-mainstartx, mainendy-mainstarty,false);
		
		G.setColor(new Color(255,255,255));
		G.setFont(F);
		if(!gameover)
			G.drawString("Score:"+(score+bigscore)*scoreplus, mainstartx, mainstarty-10);
		if(time<=100&&time>=0)
			G.drawString("Time:"+time/10, mainendx-60, mainstarty-10);
		
		this.addKeyListener(this);
		requestFocus(true);
		
		if(gameover)
		{
			G.drawString("Gameover!!!   Your  Score : "+(score+bigscore)*scoreplus, mainstartx+270, mainstarty-30);
			if(!temp)
				temp=true;
			
			else if(temp)
			{
				temp=false;
				
				G.fill3DRect(dirx[0],diry[0],sizeofsnake+1,sizeofsnake+1,true);
				for(short i=1;i<length;i++)
					G.draw3DRect(dirx[i],diry[i],sizeofsnake,sizeofsnake,true);
			}
		}
		
		else
		{
			G.fill3DRect(dirx[0],diry[0],sizeofsnake+1,sizeofsnake+1,true);
			for(short i=1;i<length;i++)
				G.draw3DRect(dirx[i],diry[i],sizeofsnake,sizeofsnake,true);
		}
		
		Random R=new Random();
		resnake:
		if(snackate)
		{
			snackx=(short) ((int)(R.nextDouble()*randomx)+(mainstartx/sizeofsnake));
			snacky=(short) ((int)(R.nextDouble()*randomy)+(mainstarty/sizeofsnake));
			snackx *= sizeofsnake;
			snacky *= sizeofsnake;
			
			for(short i=0;i<length;i++)
				if(snackx==dirx[i]&&snacky==diry[i])
					break resnake;
			
			snackate=false;
		}
		G.drawArc(snackx, snacky, sizeofsnake, sizeofsnake, 0, 360);
		
		rebigsnake:
		if(bigsnackate)
		{
			time=100;
			bigsnackx=(short) ((int)(R.nextDouble()*randomx)+(mainstartx/sizeofsnake));
			bigsnacky=(short) ((int)(R.nextDouble()*randomy)+(mainstarty/sizeofsnake));
			bigsnackx *= sizeofsnake;
			bigsnacky *= sizeofsnake;
			
			for(short i=0;i<length;i++)
				if(bigsnackx==dirx[i]&&bigsnacky==diry[i])
					break rebigsnake;
			
			bigsnackate=false;
		}
		
		if(time<=100&&time>=0&&(!gameover))
		{
			time--;
			G.fillArc(bigsnackx, bigsnacky, sizeofsnake*2, sizeofsnake*2, 0, 360);
		}
		else
		{
			time=1000;
			bigsnackx=bigsnacky=-100;
		}
		
	}
		
	public void keyPressed(KeyEvent e)
	{
		
		if(!gameover&&!pause)
		{
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:	
				if(runningleft||runningright)
				{
					Shifting();
					diry[0]=(short) (diry[0]-sizeofsnake);
					runningup=true;
					runningleft=runningright=false;
					if(!maze)
						nomaze();
					Eatting();
					repaint();
				}
				break;
			
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				if(runningleft||runningright)
				{
					Shifting();
					diry[0]=(short) (diry[0]+sizeofsnake);
					runningdown=true;
					runningleft=runningright=false;
					if(!maze)
						nomaze();
					Eatting();
					repaint();
				}
				break;
			
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:	
				if(runningup||runningdown)
				{
					Shifting();
					dirx[0]=(short) (dirx[0]+sizeofsnake);
					runningright=true;
					runningup=runningdown=false;
					if(!maze)
						nomaze();
					Eatting();
					repaint();
				}
				break;
			
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				if(runningup||runningdown)
				{
					Shifting();
					dirx[0]=(short) (dirx[0]-sizeofsnake);
					runningleft=true;
					runningup=runningdown=false;
					if(!maze)
						nomaze();
					Eatting();
					repaint();
				}
				break;
			}	//switch statement finished
		
			if(maze)
				maze();
			
			Gameover();
		}				//if(!gameover) finished
		
		
		if(e.getKeyCode()==KeyEvent.VK_C)
			pause=false;
		if(e.getKeyCode()==KeyEvent.VK_P)
			pause=true;
		if(e.getKeyCode()==KeyEvent.VK_N)
		{
			newgame=true;
			pause=false;
		}
		if(e.getKeyCode()==KeyEvent.VK_Q)
			System.exit(0);
		if(e.getKeyCode()==KeyEvent.VK_ENTER&&start)
		{
			start=false;
			menu();
			S[0].setVisible(false);
			S[1].setVisible(false);
			S[2].setVisible(false);
			newgame=true;
			repaint();	
		}
	}
	public void keyReleased(KeyEvent e)
	{
	}
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void menu()
	{
		setLayout(null);
		for(int i=0;i<5;i++)
		{
			L[i].setFont(new Font("Arial",Font.ITALIC,30));
			L[i].setAlignment(Label.CENTER);
			L[i].setBounds(mainendx+80, mainstarty+92*i,250,50);
			add(L[i]);
			L[i].addMouseListener(this);
			requestFocus(true);
		}
	}
	
	public void nomaze()
	{
		if(dirx[0]==mainstartx-sizeofsnake)
			dirx[0]=mainendx-sizeofsnake;
		else if(dirx[0]==mainendx)
			dirx[0]=mainstartx;
		else if(diry[0]==mainstarty-sizeofsnake)
			diry[0]=mainendy-sizeofsnake;
		else if(diry[0]==mainendy)
			diry[0]=mainstarty;
	}
	
	public void maze()
	{
		if(dirx[0]==mainstartx-sizeofsnake || dirx[0]==mainendx || diry[0]==mainstarty-sizeofsnake || diry[0]==mainendy)
			gameover=true;
	}
	
	public void Shifting()
	{
		lastx=dirx[length-1];
		lasty=diry[length-1];
		for(int i=length-1;i>0;i--)
		{
			dirx[i]=dirx[i-1];
			diry[i]=diry[i-1];
		}
	}
		
	public void Eatting()
	{ 
		//big snacks
		if( (dirx[0]==bigsnackx&&diry[0]==bigsnacky) || (dirx[0]==bigsnackx+sizeofsnake&&diry[0]==bigsnacky) || (dirx[0]==bigsnackx&&diry[0]==bigsnacky+sizeofsnake) || (dirx[0]==bigsnackx+sizeofsnake&&diry[0]==bigsnacky+sizeofsnake) )
		{
			bigsnackate=false;
			bigscore= (short) (bigscore+time/10);
			oldsnackx[count]=bigsnackx;
			oldsnacky[count]=bigsnacky;
			count++;
			time=1000;
			if(count==5)
				count=0;
			bigsnackx=bigsnacky=-100;
		}
		
		//snack
		if(dirx[0]==snackx&&diry[0]==snacky)
		{
			snackate=true;
			score++;
			if(score%10==0)
				bigsnackate=true;
			
			oldsnackx[count]=snackx;
			oldsnacky[count]=snacky;
			count++;
			if(count==5)
				count=0;
		}
		//Adding snack
		if(dirx[length-1]==oldsnackx[counti]&&diry[length-1]==oldsnacky[counti])
		{
			oldsnackx[counti]=oldsnacky[counti]=-100;
			length++;
			dirx[length-1]=dirx[length-2];
			diry[length-1]=diry[length-2];
			counti++;
			if(counti==5)
				counti=0;
		}
			
	}

	public void Gameover()
	{
		for(short i=4;i<length;i++)
			if(dirx[0]==dirx[i]&&diry[0]==diry[i])
				gameover=true;
		if(gameover)
		{
			for(int i=1;i<length;i++)
			{
				dirx[i-1]=dirx[i];
				diry[i-1]=diry[i];
			}
			dirx[length-1]=lastx;
			diry[length-1]=lasty;
		}
	}

	public void mouseClicked(MouseEvent e)
	{

		if(e.getSource()==L[0])
		{
			if(!pause)
			{
				pause=true;
				L[0].setText("CONTINUE");
			}
			else
			{
				pause=false;
				L[0].setText("PAUSE");
			}
		}
		
		if(e.getSource()==L[1])
		{
			newgame=true;
			pause=false;
			L[0].setText("PAUSE");
		}
		if(e.getSource()==L[2])
		{
			if(!maze)
			{
				maze=true;
				L[2].setText("NO MAZE");
			}
			else
			{
				maze=false;
				L[2].setText("MAZE");
			}

			newgame=true;
			pause=false;
			L[0].setText("PAUSE");
		}
		if(e.getSource()==L[3])
		{
			if(runspeed==runfast)
			{
				runspeed=runfaster;
				L[3].setText("LEVEL UP 3");
			}
			else if(runspeed==runfaster)
			{
				runspeed=runfastest;
				L[3].setText("LEVEL DOWN 1");
			}
			else
			{
				runspeed=runfast;
				L[3].setText("LEVEL UP 2");
			}
			newgame=true;
			pause=false;
			L[0].setText("PAUSE");
			
		}
		
		if(e.getSource()==L[4])
			System.exit(0);
		
	}
	public void mousePressed(MouseEvent e)
	{
			
	}
	public void mouseReleased(MouseEvent e)
	{
		
	}
	public void mouseEntered(MouseEvent e)
	{
		for(int i=0;i<5;i++)
			if(e.getSource()==L[i])
				L[i].setForeground(new Color(255,255,255));
	}
	public void mouseExited(MouseEvent e)
	{
		for(int i=0;i<5;i++)
			if(e.getSource()==L[i])
				L[i].setForeground(new Color(0,0,0));
	}
	
}
/*
<applet code="MySnake" width="1360" height="650"></applet>
 */


