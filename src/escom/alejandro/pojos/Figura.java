package escom.alejandro.pojos;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import escom.alejandro.evolutivos.Evolutivos;
import escom.alejandro.utileria.Constantes;
import escom.alejandro.utileria.Transformaciones;

public class Figura {
	private ArrayList<Particula> particulas;
	private float[] centro = new float[Constantes.DIMENSIONES];
	private float[] limites = new float[(int)Math.pow(2, Constantes.DIMENSIONES)];
	private int color;
	public static boolean usarUmbral = false;
	
	/*
	 *Constructores
	 */
	public Figura(int color){
		particulas = new ArrayList<Particula>();
		centro[Constantes.X_INDEX] = centro[Constantes.Y_INDEX] = 0;
		limites[Constantes.IZ_INDEX] = limites[Constantes.AB_INDEX] = limites[Constantes.AR_INDEX] = limites[Constantes.DE_INDEX] = 0;
		this.color = color;
	}
	
	public Figura(){
		particulas = new ArrayList<Particula>();
		centro[Constantes.X_INDEX] = centro[Constantes.Y_INDEX] = 0;
		limites[Constantes.IZ_INDEX] = limites[Constantes.AB_INDEX] = limites[Constantes.AR_INDEX] = limites[Constantes.DE_INDEX] = 0;
		this.color = Color.BLACK;
	}
	/*
	 *Acceso a propiedades de la figura 
	 */
	public int color(){
		return color;
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	public ArrayList<Particula> obtenParticulas(){
		return particulas;
	}
	
	public int particulaSize(){
		return particulas.size();
	}
	
	public void setCentro(float[] nuevoCentro){
		this.centro = nuevoCentro;
	}
	
	public float[] centro(){
		return centro;
	}
	
	public float[] centroNegativo(){
		return new float[]{-centro[0],-centro[1]};
	}
	
	public float[] getCoordenadasParticula(int index){
		return particulas.get(index).centro();
	}
	
	public float[] getCoordenadasNegativasParticula(int index){
		return new float[]{
				-particulas.get(index).centro()[Constantes.X_INDEX],
				-particulas.get(index).centro()[Constantes.Y_INDEX]};
	}
	
	public float[] diametros(){
		float[] diametro = {limites[Constantes.DE_INDEX]-limites[Constantes.IZ_INDEX],
							limites[Constantes.AR_INDEX]-limites[Constantes.AB_INDEX]};
		
		return diametro;
	}
	
	/*
	 *Funcion para agregar una particula, actualiza las demas propiedades si es necesario.
	 */
	public void agregarParticula(Particula p){
		particulas.add(p);
		
		if(particulas.size() == 1){
			limites[Constantes.IZ_INDEX] = p.centro()[Constantes.X_INDEX] - p.radio();
			limites[Constantes.AB_INDEX] = p.centro()[Constantes.Y_INDEX] - p.radio();
			limites[Constantes.AR_INDEX] = p.centro()[Constantes.Y_INDEX] + p.radio();
			limites[Constantes.DE_INDEX] = p.centro()[Constantes.X_INDEX] + p.radio();
		}else{
			if(p.centro()[Constantes.X_INDEX]-p.radio() < limites[Constantes.IZ_INDEX])//Mas a la izquierda
				limites[Constantes.IZ_INDEX] = p.centro()[Constantes.X_INDEX]-p.radio();
			if(p.centro()[Constantes.Y_INDEX]-p.radio() < limites[Constantes.AB_INDEX])//Mas abajo
				limites[Constantes.AB_INDEX] = p.centro()[Constantes.Y_INDEX]-p.radio();
			if(p.centro()[Constantes.Y_INDEX]+p.radio() > limites[Constantes.AR_INDEX])//Mas arriba
				limites[Constantes.AR_INDEX] = p.centro()[Constantes.Y_INDEX]+p.radio();
			if(p.centro()[Constantes.X_INDEX]+p.radio() > limites[Constantes.DE_INDEX])//Mas derecha
				limites[Constantes.DE_INDEX] = p.centro()[Constantes.X_INDEX]+p.radio();
		}
		
		this.centro[Constantes.X_INDEX] = limites[Constantes.IZ_INDEX] + (limites[Constantes.DE_INDEX]-limites[Constantes.IZ_INDEX])/2;
		this.centro[Constantes.Y_INDEX] = limites[Constantes.AB_INDEX] + (limites[Constantes.AR_INDEX]-limites[Constantes.AB_INDEX])/2;
		
	}
	
	/*
	 *Dibuja la figura en un Canvas, particula por particula. 
	 */
	public void dibujaFigura(Canvas c, Paint p){
		p.setColor(color);
		
		for( Particula particle: particulas ){
			particle.dibujaParticula(c, p);
		}
	}
	
	/*
	 * Funciones de movimiento de la figura
	 */
	public void traslada(float[] desplazamiento){
		Transformaciones.loadIdentity();
		
		Transformaciones.addT(desplazamiento[0], desplazamiento[1]);
		
		for(Particula p:particulas){
			float[] nuevoPunto = Transformaciones.transforma(p.centro());
			p.setCentro(nuevoPunto);
		}
		centro = Transformaciones.transforma(centro);
	}
	
	public void rota(int angulo){
		float[] nuevoPunto;
		Transformaciones.loadIdentity();
		Transformaciones.addR(angulo);
		for(Particula p:particulas){
			nuevoPunto = Transformaciones.transforma(p.centro());
			p.setCentro(nuevoPunto);
		}
		
		nuevoPunto = Transformaciones.transforma(this.centro);
		this.setCentro(nuevoPunto);
	}
	
	public void refleja(){
		float centro[];
		centro = centro();
		this.traslada(centroNegativo());
		
		Transformaciones.loadIdentity();
		Transformaciones.addReflexion();
		for(Particula p:particulas){
			float[] nuevoPunto = Transformaciones.transforma(p.centro());
			p.setCentro(nuevoPunto);
		}

		this.traslada(centro);
	}
	
	public int indiceParticulaConMejorAfinidad(Figura f){
		int indiceMejorParticula = Constantes.INDICE_CENTRO_FIGURA;
		float afinidad = 0,mejorAfinidad = Constantes.TRASLAPE;
		float distancia;
		float umbral;
		
		for(int i = 0 ; i < particulaSize();i++){
			afinidad = 0;
			//Log.v("Tengo una particula en ",""+particulas.get(i));
			for(Particula externa:f.obtenParticulas()){
				distancia = Particula.distanciaEuclidiana(particulas.get(i),externa);
				
				if(usarUmbral){
					umbral = (2*externa.radio() +2*particulas.get(i).radio());
					if(distancia <= umbral)
						afinidad += 1/distancia;
				}else{ 
					afinidad += 1/distancia;
				}
				
			}
			if(afinidad > mejorAfinidad && afinidad != Constantes.TRASLAPE){
				mejorAfinidad = afinidad;
				indiceMejorParticula = i;
			}
		}
		
		return indiceMejorParticula;
	}
	
	/*
	 *Funciones de puntaje, de una figura vs otra
	 */
	public float afinidadGeneral(Figura f){
		float afDistancia,afHuecos;
		afDistancia = afinidadPorDistancia(f);
		afHuecos = afinidadPorDistanciaAHueco();
		return afDistancia == Constantes.TRASLAPE ? Constantes.TRASLAPE : afDistancia+afHuecos;
	}
	
	public float  afinidadPorDistancia(Figura f){
		float afinidad = 0;
		float distancia;
		float umbral;
		
		for(Particula propia:obtenParticulas()){
			for(Particula externa:f.obtenParticulas()){
				distancia = Particula.distanciaEuclidiana(propia,externa);
				if(distancia == Constantes.TRASLAPE)//Si hay traslape, dejo de evaluar.
					return Constantes.TRASLAPE;
				if(usarUmbral){
					umbral = (2*externa.radio() +2*propia.radio());
					if(distancia <= umbral)
						afinidad += 1/distancia;
				}else 
					afinidad += 1/distancia;
			}
		}
		return afinidad;
	}
	
	public float afinidadPorDistanciaAHueco(){
		float distancia;
		float afinidad;
		
		afinidad = 0;
		for(Particula propia:particulas){
			for(Particula hueco:Evolutivos.huecos){
			distancia = (float)
						Math.pow(	
							Math.pow(propia.centro()[Constantes.X_INDEX] - hueco.centro()[Constantes.X_INDEX],2) + 
							Math.pow(propia.centro()[Constantes.Y_INDEX] - hueco.centro()[Constantes.Y_INDEX],2)
						,0.5);	
				
			if(distancia  < 20) //La distancia entre las particulas es muy pequeña
				return Evolutivos.coeficienteAtraccionHuecos;
			else
				afinidad += Evolutivos.coeficienteAtraccionHuecos*(1/distancia);
			}
		}
		return afinidad;
	}
	
	/*
	 *Utileria, copia profunda del objeto 
	 */
	public Figura copiaProfunda(){
		Figura nuevaFigura = new Figura(this.color());
		for(Particula p:this.obtenParticulas()){
			nuevaFigura.agregarParticula(p.copiaProfunda());
		}
		return nuevaFigura;
	}
	
	public String toString(){
		return "Centro:"+centro[0]+",:"+centro[1];
	}
	
	/*
	 *Utileria, regresa una nueva figura a partir de un gen 
	 */
	public static Figura obtenFiguraPorGen(Figura figuraOriginal,Gen g){
		Figura f = figuraOriginal.copiaProfunda();
		float[] pivote;
		float[] pivoteNegativo;
		
		if(g.refleja())
			f.refleja();
		
		if(g.angulo() != 0){
			//Si no tiene pivote definido, usemos el centro
			if(g.getIndicePivoteRotacion() == -1){
				pivote = f.centro();
				pivoteNegativo = f.centroNegativo();
			}else{
				pivote = f.getCoordenadasParticula(g.getIndicePivoteRotacion());
				pivoteNegativo = f.getCoordenadasNegativasParticula(g.getIndicePivoteRotacion());
				
			}
			f.traslada(pivoteNegativo);	
			f.rota(g.angulo());
			f.traslada(pivote);
		}
		
		f.traslada(g.offsets());
		
		if(g.genAjuste() != null){
			if(g.genAjuste().refleja())
				f.refleja();
			
			if(g.genAjuste().angulo() != 0){
				//Si no tiene pivote definido, usemos el centro
				if(g.genAjuste().getIndicePivoteRotacion() == -1){
					pivote = f.centro();
					pivoteNegativo = f.centroNegativo();
				}else{
					pivote = f.getCoordenadasParticula(g.genAjuste().getIndicePivoteRotacion());
					pivoteNegativo = f.getCoordenadasNegativasParticula(g.genAjuste().getIndicePivoteRotacion());
					
				}
				f.traslada(pivoteNegativo);	
				f.rota(g.genAjuste().angulo());
				f.traslada(pivote);
			}
			
			f.traslada(g.genAjuste().offsets());
		}
		
		return f;
	}
	
	/*
	 * Modifica la figura actual en base a un gen
	 */
	public void modificaFiguraPorGen(Gen g){
		float[] pivote;
		float[] pivoteNegativo;
		
		if(g == null)
			return;
		
		if(g.refleja()){
			this.refleja();
		}
		
		if(g.angulo() != 0){
			//Bandera para indicar que el pivote debe ser el centro de la fig.
			if(g.getIndicePivoteRotacion() == -1){
				pivote = centro();
				pivoteNegativo = centroNegativo();
			}else{
				pivote = getCoordenadasParticula(g.getIndicePivoteRotacion());
				pivoteNegativo = getCoordenadasNegativasParticula(g.getIndicePivoteRotacion());
			}
			this.traslada(pivoteNegativo);
			this.rota(g.angulo());
			this.traslada(pivote);
		}
		
		this.traslada(g.offsets());
		
	}
	
	/*
	 * Regresa la figura actual a antes de los cambios que hizo el gen 
	 */
	public void reestableceFiguraPorGen(Gen g){
		float[] pivote;
		float[] pivoteNegativo;
		
		if(g == null)
			return;
		
		this.traslada(g.offsetsNegativos());
		
		if(g.angulo() != 0){
			//Si no tiene pivote definido, usemos el centro
			if(g.getIndicePivoteRotacion() == -1){
				pivote = centro();
				pivoteNegativo = centroNegativo();
			}else{
				pivote = getCoordenadasParticula(g.getIndicePivoteRotacion());
				pivoteNegativo = getCoordenadasNegativasParticula(g.getIndicePivoteRotacion());
			}
			this.traslada(pivoteNegativo);	
			this.rota(g.anguloNegativo());
			this.traslada(pivote);
		}
		
		if(g.refleja())
			this.refleja();
	}
}
