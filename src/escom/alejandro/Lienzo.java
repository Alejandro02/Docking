package escom.alejandro;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import escom.alejandro.evolutivos.Evolutivos;
import escom.alejandro.pojos.Figura;
import escom.alejandro.pojos.Particula;
import escom.alejandro.utileria.Constantes;

public class Lienzo extends Activity {
	
	private GraficoView g;
	
	public Lienzo(){
		Constantes.offset[Constantes.X_INDEX] = Constantes.offset[Constantes.Y_INDEX] = 0;
		Constantes.escala[Constantes.X_INDEX] = Constantes.escala[Constantes.Y_INDEX] = 1;
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		g = new GraficoView(this);
		
		g.setParams(getIntent().getIntExtra("param1", 20),
				getIntent().getFloatExtra("param2", 0.8f),
				getIntent().getIntExtra("param3", 5),
				getIntent().getIntExtra("param4", 0),
				getIntent().getIntExtra("param5", 0),
				getIntent().getBooleanExtra("param6", true));		
		setContentView(g);
	}
}

class GraficoView extends View{
	private Figura figura1;
	private Figura figura2;
	private Paint p;
	private Evolutivos evolutivos;

	private boolean simulacionIniciada = false;
	private final int[] colores = {	Color.RED,
										Color.BLUE,
										Color.GREEN, 
										Color.YELLOW, 
										Color.GRAY,
										Color.MAGENTA,
										Color.DKGRAY,
										Color.CYAN};
	
	private int[] centroCuadrante1;
	private int[] centroCuadrante2;
	private int[] centroCuadrante3;
	private int[] centroCuadrante4;
	
	private int[] centroPantalla;
	private Particula hueco; 

	public GraficoView(Context ctx){
		super(ctx);
		
		/**
		 *Creando las dos figuras 
		 **/
		figura1 = new Figura(Color.RED);
		figura1.agregarParticula(new Particula(new float[]{20,20}, 10, 2, Color.BLACK));
		figura1.agregarParticula(new Particula(new float[]{40,20}, 10, 2, Color.BLACK));
		figura1.agregarParticula(new Particula(new float[]{60,20}, 10, 2, Color.BLACK));
		figura1.agregarParticula(new Particula(new float[]{20,40}, 10, 2, Color.BLACK));
		
		figura2 = new Figura(Color.BLACK);
		figura2.agregarParticula(new Particula(new float[]{195,128}, 11, 2, Color.BLACK));//Bola de abajo der
		figura2.agregarParticula(new Particula(new float[]{150,150}, 11, 2, Color.BLACK));//Segunda fila Arriba
		figura2.agregarParticula(new Particula(new float[]{195,150}, 11, 2, Color.BLACK));
		//Hay un hueco en {172,150}  
		figura2.agregarParticula(new Particula(new float[]{150,172}, 11, 2, Color.BLACK));//Tercera fila Arriba
		figura2.agregarParticula(new Particula(new float[]{195,172}, 11, 2, Color.BLACK));
		figura2.agregarParticula(new Particula(new float[]{128,194}, 11, 2, Color.BLACK));//Cuarta fila Arriba
		figura2.agregarParticula(new Particula(new float[]{150,194}, 11, 2, Color.BLACK));
		figura2.agregarParticula(new Particula(new float[]{172,194}, 11, 2, Color.BLACK));
		figura2.agregarParticula(new Particula(new float[]{194,194}, 11, 2, Color.BLACK));
		figura2.agregarParticula(new Particula(new float[]{216,194}, 11, 2, Color.BLACK));
		figura2.agregarParticula(new Particula(new float[]{172,216}, 11, 2, Color.BLACK));//Quinta fila
		figura2.agregarParticula(new Particula(new float[]{172,238}, 11, 2, Color.BLACK));//Sexta fila
		
		hueco = new Particula(new float[]{172,150}, 0, 0, 0);
		
		hueco.traslada(figura2.centroNegativo());
		
		figura2.traslada(figura2.centroNegativo());
		
		Constantes.offset[Constantes.Y_INDEX] = 0;
		Constantes.offset[Constantes.X_INDEX] = 0;
		
		p = new Paint();
		centroCuadrante1 = new int[2];
		centroCuadrante2 = new int[2];
		centroCuadrante3 = new int[2];
		centroCuadrante4 = new int[2];
		centroPantalla = new int[2];
	}
	
	public void setParams(int poblacion,float mutacion,int individuosElite,int coeficienteAtraccionHaciaHuecos,int umbralDestruccionGemelos,boolean eliminarGemelos){
		ArrayList<Particula> h = new ArrayList<Particula>();
		h.add(hueco);
		evolutivos = new Evolutivos(poblacion, individuosElite, mutacion,figura1,figura2,h,coeficienteAtraccionHaciaHuecos,umbralDestruccionGemelos,eliminarGemelos);
	}
	
	protected void onDraw(Canvas c){
		super.onDraw(c);
		
		p.setColor(Color.WHITE);
		c.drawPaint(p);
		p.setAntiAlias(true);
		if(!simulacionIniciada){
			Constantes.ACTIVITY_HEIGHT = getMeasuredHeight();
			Constantes.ACTIVITY_WIDTH = getMeasuredWidth();
			
			centroCuadrante1[0] = Constantes.ACTIVITY_WIDTH/4;
			centroCuadrante1[1] = 3*Constantes.ACTIVITY_HEIGHT/4;
			
			centroCuadrante2[0] = 3*Constantes.ACTIVITY_WIDTH/4;
			centroCuadrante2[1] = 3*Constantes.ACTIVITY_HEIGHT/4;
			
			centroCuadrante3[0] = Constantes.ACTIVITY_WIDTH/4;
			centroCuadrante3[1] = Constantes.ACTIVITY_HEIGHT/4;
			
			centroCuadrante4[0] = 3*Constantes.ACTIVITY_WIDTH/4;
			centroCuadrante4[1] = Constantes.ACTIVITY_HEIGHT/4;
			
			centroPantalla[0] = Constantes.ACTIVITY_WIDTH/2;
			centroPantalla[1] = Constantes.ACTIVITY_HEIGHT/2;
			
			Constantes.offset = centroPantalla;
			dibujaPoblacion(c, p,true);
			
		}else{
			/*for(int i = 0 ; i < 50;i++){
					evolutivos.siguiente(1);
					evolutivos.siguiente(2);
					evolutivos.siguiente(3);
					evolutivos.siguiente(4);
				
			}
			Constantes.offset = centroPantalla;
			dibujaPoblacion(c, p,false);*/

			
			Constantes.offset = centroCuadrante1;
			evolutivos.siguiente(1);
			dibujaPoblacion(c, p, false);
			
			Constantes.offset = centroCuadrante2;
			evolutivos.siguiente(2);
			dibujaPoblacion(c, p, false);
			
			Constantes.offset = centroCuadrante3;
			evolutivos.siguiente(3);
			dibujaPoblacion(c, p, false);
			
			Constantes.offset = centroCuadrante4;
			evolutivos.siguiente(4);
			dibujaPoblacion(c, p, false);
			
		}
		
	}
	
	private void dibujaPoblacion(Canvas c, Paint p,boolean poblacionCompleta){
		int i = 0;
		ArrayList<Figura> pob;
		
		figura2.dibujaFigura(c, p);
		
		pob = poblacionCompleta ? evolutivos.getPoblacion(): evolutivos.getMejoresFiguras(); 
		
		for(Figura f:pob){
			if(!poblacionCompleta && i < colores.length)
				f.setColor(colores[i++]);
			else
				f.setColor(Color.RED);
			f.dibujaFigura(c, p);
		}
		
	}
	
	public boolean onTouchEvent(MotionEvent evt){
		if(evt.getAction() == MotionEvent.ACTION_DOWN){
			simulacionIniciada = true;
			invalidate();
		}
		return true;
	}
}