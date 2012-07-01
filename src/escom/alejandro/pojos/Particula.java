package escom.alejandro.pojos;

import android.graphics.Canvas;
import android.graphics.Paint;
import escom.alejandro.utileria.Constantes;
import escom.alejandro.utileria.Transformaciones;

public class Particula {
	private float[] centro;
	private int radio;
	private float carga;
	private int color;
	private float afinidad;
	
	public void traslada(float[] desplazamiento){
		Transformaciones.loadIdentity();
		
		Transformaciones.addT(desplazamiento[0], desplazamiento[1]);
		float[] nuevoPunto = Transformaciones.transforma(this.centro());
		this.setCentro(nuevoPunto);
		
	}
	
	public float afinidad(){
		return afinidad;		
	}
	
	public void setAfinidad(float afinidad){
		this.afinidad = afinidad;
	}
	
	public float[] centro() {
		return centro;
	}

	public void setCentro(float[] centro) {
		this.centro = centro;
	}

	public int radio() {
		return radio;
	}

	public void setRadio(int radio) {
		this.radio = radio;
	}

	public float carga() {
		return carga;
	}

	public void setCarga(float carga) {
		this.carga = carga;
	}

	public int  color() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Particula(float[] centro,int radio, float carga, int color){
		this.centro = centro;
		this.radio = radio;
		this.carga = carga;
		this.color = color;
	}
	public void dibujaParticula(Canvas c,Paint p){
		//p.setColor(this.color());
	    c.drawCircle(Constantes.offset[Constantes.X_INDEX] + centro[Constantes.X_INDEX]*Constantes.escala[Constantes.X_INDEX],
	    			 Constantes.ACTIVITY_HEIGHT - (Constantes.offset[Constantes.Y_INDEX] + centro[Constantes.Y_INDEX]*Constantes.escala[Constantes.Y_INDEX]),
					 radio, p);//Falta la escala en las dos dimensiones
	}
	
	public static float[] areaParticula(Particula p){
		float[] area = new float[4];
		area[Constantes.IZ_INDEX] = p.centro()[Constantes.X_INDEX] - p.radio();
		area[Constantes.AB_INDEX] = p.centro()[Constantes.Y_INDEX] - p.radio();
		area[Constantes.AR_INDEX] = p.centro()[Constantes.Y_INDEX] + p.radio();
		area[Constantes.DE_INDEX] = p.centro()[Constantes.X_INDEX] + p.radio();
		
		return area;
	}
	
	public static float distanciaEuclidiana(Particula p1,Particula p2){
		float[] area1 = areaParticula(p1);
		float[] area2 = areaParticula(p2);
		
		float distancia = 0;
		
		if( (area2[Constantes.IZ_INDEX] <= area1[Constantes.DE_INDEX]) && 
			(area2[Constantes.IZ_INDEX] >= area1[Constantes.IZ_INDEX]) &&
			(area2[Constantes.AB_INDEX] >= area1[Constantes.AB_INDEX]) &&
			(area2[Constantes.AB_INDEX] <= area1[Constantes.AR_INDEX]))
	        return Constantes.TRASLAPE;

	    if( (area2[Constantes.IZ_INDEX] <= area1[Constantes.DE_INDEX]) &&
	    	(area2[Constantes.IZ_INDEX] >= area1[Constantes.IZ_INDEX]) &&
	    	(area2[Constantes.AR_INDEX] >= area1[Constantes.AB_INDEX]) &&
	    	(area2[Constantes.AR_INDEX] <= area1[Constantes.AR_INDEX]))
	    	return Constantes.TRASLAPE;

	    if( (area2[Constantes.DE_INDEX] <= area1[Constantes.DE_INDEX]) &&
	    	(area2[Constantes.DE_INDEX] >= area1[Constantes.IZ_INDEX]) &&
	    	(area2[Constantes.AB_INDEX] >= area1[Constantes.AB_INDEX]) &&
	    	(area2[Constantes.AB_INDEX] <= area1[Constantes.AR_INDEX]))
	    	return Constantes.TRASLAPE;

	    if( (area2[Constantes.DE_INDEX] <= area1[Constantes.DE_INDEX]) &&
	    	(area2[Constantes.DE_INDEX] >= area1[Constantes.IZ_INDEX]) &&
	    	(area2[Constantes.AR_INDEX] >= area1[Constantes.AB_INDEX]) &&
	    	(area2[Constantes.AR_INDEX] <= area1[Constantes.AR_INDEX]))
	    	return Constantes.TRASLAPE;
	        
	    //Comprobando puntos de fig 1 dentro de la 2
		if ((area1[Constantes.IZ_INDEX] <= area2[Constantes.DE_INDEX]) &&
			(area1[Constantes.IZ_INDEX] >= area2[Constantes.IZ_INDEX]) &&
			(area1[Constantes.AB_INDEX] >= area2[Constantes.AB_INDEX]) &&
			(area1[Constantes.AB_INDEX] <= area2[Constantes.AR_INDEX]))
			return Constantes.TRASLAPE;

		if ((area1[Constantes.IZ_INDEX] <= area2[Constantes.DE_INDEX]) &&
			(area1[Constantes.IZ_INDEX] >= area2[Constantes.IZ_INDEX]) &&
			(area1[Constantes.AR_INDEX] >= area2[Constantes.AB_INDEX]) &&
			(area1[Constantes.AR_INDEX] <= area2[Constantes.AR_INDEX]))
			return Constantes.TRASLAPE;

		if ((area1[Constantes.DE_INDEX] <= area2[Constantes.DE_INDEX]) &&
			(area1[Constantes.DE_INDEX] >= area2[Constantes.IZ_INDEX]) &&
			(area1[Constantes.AB_INDEX] >= area2[Constantes.AB_INDEX]) &&
			(area1[Constantes.AB_INDEX] <= area2[Constantes.AR_INDEX]))
			return Constantes.TRASLAPE;

		if ((area1[Constantes.DE_INDEX] <= area2[Constantes.DE_INDEX]) &&
			(area1[Constantes.DE_INDEX] >= area2[Constantes.IZ_INDEX]) &&
			(area1[Constantes.AR_INDEX] >= area2[Constantes.AB_INDEX]) &&
			(area1[Constantes.AR_INDEX] <= area2[Constantes.AR_INDEX]))
			return Constantes.TRASLAPE;
	    
	    
	    distancia = (float)Math.pow(	Math.pow(p1.centro[Constantes.X_INDEX] - p2.centro[Constantes.X_INDEX],2) + 
	    								Math.pow(p1.centro[Constantes.Y_INDEX] - p2.centro[Constantes.Y_INDEX],2)
	    						,0.5) ;
	    return distancia;
		
	}
	
	public Particula copiaProfunda(){
		return new Particula(this.centro,this.radio,this.carga,this.color);
	}
	
	public String toString(){
		return "Centro:["+centro[Constantes.X_INDEX]+","+centro[Constantes.Y_INDEX]+"]";
	}
}
