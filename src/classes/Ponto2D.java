package classes;
public class Ponto2D {
	
	private double X;
	private double Y;
	private double vX;
	private double vY;
	
	public Ponto2D (double x, double y, double vX, double vY){
		this.X=x;
		this.Y=y;
		this.vX=vX;
		this.vY=vY;
	}
	
	public void setX (double x){
		this.X = x;
	}
	
	public void setY (double y){
		this.Y = y;
	}
	
	public void setvX (double vx){
		this.vX = vx;
	}
	
	public void setvY (double vy){
		this.vY = vy;
	}
	
	public double getX (){
		return this.X;
	}
	
	public double getY (){
		return this.Y;
	}
	
	public double getvX (){
		return this.vX;
	}
	
	public double getvY (){
		return this.vY;
	}
	
}