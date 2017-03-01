package snake;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
//import java.util.ArrayList;
public class GreedySnake {
	
	public class Dot{
		private int x,y;
		public Dot(int x,int y){
			this.x=x;
			this.y=y;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		public boolean equals(Dot dot){
			if (dot.x==x&&dot.y==y){
				return true;
			}else{
				return false;
			}
		}
	}
	
	JFrame frame;
	private Dot food;	
	private int size;
	private Snake snake;
	MyPanel panel;
	Button startButton;
	public static void main(String args[]){
		
			GreedySnake gs = new GreedySnake();
			gs.go();
	}
	
	public void go(){
		size=30;
		frame = new JFrame ();
		panel = new MyPanel();
		frame.addKeyListener(new KeyAdapter());
		frame.add(panel);
		//界面
		
		snake = new Snake(size);
		food = new Dot((int)Math.random()*size,(int)Math.random()*size);
		frame.setSize(1050, 1050);
		frame.setVisible(true);
		Thread th = new Thread (snake);
		th.start();
	}
	
	public void setFood(){
		boolean isSuccessed=false;
		while(!isSuccessed){
			food.setX((int)(Math.random()*size));
			food.setY((int)(Math.random()*size));
			boolean isSame=false;
			for(Dot snakes :snake.getSnakeBody()){
				if (snakes.equals(food)){
					isSame=true;
				}
			}
			if (!isSame) isSuccessed=true;
		}
	}
	
	public class MyPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		public void paintComponent(Graphics g){
			int red,green,blue;
			Color myColor;
			System.out.println("1");
			red=255;green=255;blue=255;
			myColor = new Color(red,green,blue);
			g.setColor(myColor);
			g.fillRect(50, 50, size*30, size*30);
			
			red=0;green=0;blue=0;
			myColor = new Color(red,green,blue);
			g.setColor(myColor);
			g.drawRect(50, 50, size*30, size*30);
			
			red=40;green=200;blue=12;
			myColor = new Color(red,green,blue);
			g.setColor(myColor);
			Dot head=snake.getSnakeBody().get(0);
			g.fillRect(50+head.getY()*30, 50+head.getX()*30, 30, 30);
			
			red=50;green=180;blue=20;
			myColor = new Color(red,green,blue);
			g.setColor(myColor);
			int width=30,count=1;
			for (int i =1;i<=snake.getSnakeBody().size()-1;i++){
				count++;
				if (count%6==0){
					width-=1;
				}
				Dot dots=snake.getSnakeBody().get(i);
				g.fillRect(50+dots.getY()*30,50+dots.getX()*30, (int)width, (int)width);
			}
			red=255;green=50;blue=50;
			myColor = new Color(red,green,blue);
			g.setColor(myColor);
			g.fillRect(50+food.getY()*30, 50+food.getX()*30, 30, 30);
		}
	}
		
	public class KeyAdapter implements KeyListener{
		public void keyPressed(KeyEvent ke){
//			System.out.println(ke.getKeyCode());
			switch(ke.getKeyCode()){
			case KeyEvent.VK_UP:changeDir(-1,0);break;
			case KeyEvent.VK_LEFT:changeDir(0,-1);break;
			case KeyEvent.VK_DOWN:changeDir(1,0);break;
			case KeyEvent.VK_RIGHT:changeDir(0,1);break;
			case KeyEvent.VK_W:changeDir(-1,0);break;
			case KeyEvent.VK_A:changeDir(0,-1);break;
			case KeyEvent.VK_S:changeDir(1,0);break;
			case KeyEvent.VK_D:changeDir(0,1);break;
			}
			//调整方向
		}
		public void keyTyped(KeyEvent ke){}
		public void keyReleased(KeyEvent ke){}
		public void changeDir(int x,int y){
			if (snake.getDirX()+x==0&&snake.getDirY()+y==0){
			}else{
				snake.setDirX(x);
				snake.setDirY(y);
			}
		}
	}
	
	public class Snake implements Runnable{
		private int dirX,dirY,maxX,maxY;
		private boolean isAlive;
		private ArrayList <Dot> snakeBody;
		private int interruptTime;
		private int length;
		public Snake(int size){
			maxX=maxY=size;
			Dot head = new Dot(size/2,size/2);
			Dot tail = new Dot(size/2+4,size/2);
			snakeBody = new ArrayList <Dot>();
			snakeBody.add(head);
			snakeBody.add(new Dot(size/2+1,size/2));
			snakeBody.add(new Dot(size/2+2,size/2));
			snakeBody.add(new Dot(size/2+3,size/2));
			snakeBody.add(tail);
			length=5;
			dirX=-1;
			dirY=0;
			isAlive=true;
			interruptTime=225;
		}
		
		public void run(){
			setFood();
			while (isAlive){
				panel.repaint();
				try{
					Thread.sleep(interruptTime);
				}catch(InterruptedException ix){
					ix.printStackTrace();
				}
				Dot head = snakeBody.get(0);
				snakeBody.remove(snakeBody.size()-1);
				Dot newhead= new Dot(0,0);
				newhead.setX(head.getX()+dirX);
				newhead.setY(head.getY()+dirY);
				snakeBody.add(0, newhead);
				if (newhead.equals(food)){
					grow();
					setFood();
				}
				checkYourself();
			}
		}
		
		public void checkYourself(){
			Dot nowhead = snakeBody.get(0);
			int x=nowhead.getX(),y=nowhead.getY();
			if (x<0||y<0||x>=size||y>=size){
				isAlive=false;
			}else{
				Dot head = snake.getSnakeBody().get(0);
				int num=0;
				for (Dot dots :snake.getSnakeBody()){
					if (head.equals(dots)){
						num++;
						if (num==2){
							isAlive=false;
							break;
						}
					}
				}
				
			}
		}
		
		public ArrayList<Dot> getSnakeBody() {
			return snakeBody;
		}

		public void grow(){
//			System.out.println("grow");
			Dot tail = snakeBody.get(snakeBody.size()-1);
			Dot tail2 = snakeBody.get(snakeBody.size()-2);
			Dot newtail =new Dot(2*tail.getX()-tail2.getX(),2*tail.getY()-tail2.getY());
			snakeBody.add(newtail);
			tail = snakeBody.get(snakeBody.size()-1);
			tail2 = snakeBody.get(snakeBody.size()-2);
			newtail =new Dot(2*tail.getX()-tail2.getX(),2*tail.getY()-tail2.getY());
			snakeBody.add(newtail);
			length++;
			length++;
			LevelUp();
		}
		
		public void LevelUp(){
//			System.out.println(interruptTime);
			int delta=0;
			if (length<12){
				delta=17;
			}else if (length<18){
				delta=14;
			}else if (length<24){
				delta=7;
			}else if (length<30){
				delta=5;
			}else if (length<40){
				delta=3;
			}else if (length<85){
				delta=1;
			}
			interruptTime-=delta;
		}
		
		public int getDirX() {
			return dirX;
		}
		public void setDirX(int dirX) {
			this.dirX = dirX;
		}
		public int getDirY() {
			return dirY;
		}
		public void setDirY(int dirY) {
			this.dirY = dirY;
		}
		public int getMaxX() {
			return maxX;
		}
		public void setMaxX(int maxX) {
			this.maxX = maxX;
		}
		public int getMaxY() {
			return maxY;
		}
		public void setMaxY(int maxY) {
			this.maxY = maxY;
		}
		public boolean isAlive() {
			return isAlive;
		}
	}
}
