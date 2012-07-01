package escom.alejandro.evolutivos;

import java.util.ArrayList;

import escom.alejandro.pojos.Figura;
import escom.alejandro.pojos.Gen;
import escom.alejandro.pojos.Particula;
import escom.alejandro.utileria.Constantes;

public class Evolutivos {
	
	private UtileriaPoblacional UP;
	private AlgoritmoGenetico AG;
	private BusquedaVecindarioBA BA;
	private BusquedaGradiente BG;
	private AlgoritmosAjuste AA;
	
	public static ArrayList<Particula> huecos;
	
	public static int umbralGemelos;
	public static int coeficienteAtraccionHuecos;
	private boolean eliminarGemelos;
	
	public static ArrayList<Gen> poblacion;
	public static int poblacionSize;
	public static int[] maxDesplazamientos;
	public static int individuosEliteSize;
	private static ArrayList<Figura> individuosElite;

	public static int figura1Particulas;
	
	private static Figura figura1;
	public static Figura figura2;
	public static Figura temporal;
	
	public Evolutivos(int poblacionSize,int individuosEliteSize,float tasaMutacion,Figura f1,Figura f2,ArrayList<Particula> huecos,int coeficienteAtraccionHuecos,int umbralGemelos,boolean eliminarGemelos){
		Gen g;
		float[] diametrosFigura1;
		float[] diametrosFigura2;
		float maxDistanciaFigura1;
		
		this.eliminarGemelos = eliminarGemelos;
		Evolutivos.poblacionSize = poblacionSize;
		Evolutivos.individuosEliteSize = individuosEliteSize;
		Evolutivos.figura1 = f1;
		Evolutivos.figura2 = f2;
		Evolutivos.figura1Particulas = figura1.particulaSize();
		Evolutivos.coeficienteAtraccionHuecos = coeficienteAtraccionHuecos;
		Evolutivos.umbralGemelos = umbralGemelos;

		diametrosFigura1 = figura1.diametros();
		diametrosFigura2 = figura2.diametros();
		
		if(diametrosFigura1[Constantes.X_INDEX] > diametrosFigura1[Constantes.Y_INDEX])
			maxDistanciaFigura1 = diametrosFigura1[Constantes.X_INDEX];
		else 
			maxDistanciaFigura1 = diametrosFigura1[Constantes.Y_INDEX];
		
		
		Evolutivos.maxDesplazamientos = new int[Constantes.DIMENSIONES];
		Evolutivos.maxDesplazamientos[Constantes.X_INDEX] = (int)( maxDistanciaFigura1 + diametrosFigura2[Constantes.X_INDEX]/2);
		Evolutivos.maxDesplazamientos[Constantes.Y_INDEX] = (int)( maxDistanciaFigura1 + diametrosFigura2[Constantes.Y_INDEX]/2);
		
		temporal =  figura1.copiaProfunda();
		temporal.traslada(temporal.centroNegativo());
		temporal.traslada(f2.centro());
		
		poblacion = new ArrayList<Gen>();
		individuosElite = new ArrayList<Figura>();
		Evolutivos.huecos = new ArrayList<Particula>(huecos);
		
		AG = new AlgoritmoGenetico(tasaMutacion);
		BA = new BusquedaVecindarioBA(8, 16);
		BG = new BusquedaGradiente();
		UP = new UtileriaPoblacional();
		AA = new AlgoritmosAjuste();
		
		for(int i = 0 ; i < Evolutivos.poblacionSize;i++){
			g = new Gen();
			g.creaValoresAleatorios();
			
			if(Math.random() > 0.5){
				g.setParticulaPivoteRotacion((Constantes.rand.nextInt(figura1Particulas)+1)-1);
			}
			poblacion.add(g);
			g = null;
		}
		UtileriaPoblacional.ordenaPoblacion();
	}
	
	
	public void siguienteIteracion(){
		UP.sustituyeIndividuosTraslapados();
		AG.evolucionaPoblacion();
		UtileriaPoblacional.ordenaPoblacion();
		if(eliminarGemelos){
			UP.eliminaGemelos();
			UtileriaPoblacional.ordenaPoblacion();
		}
		BA.evolucionaPoblacion();
		BG.evolucionaPoblacion();
		
	}
	
	public void siguiente(int x){
		switch(x){
			case 1:
				UP.sustituyeIndividuosTraslapados();
				AG.evolucionaPoblacion();
				if(eliminarGemelos)
					UP.eliminaGemelos();
				UtileriaPoblacional.ordenaPoblacion();
			break;
			case 2:
				BA.evolucionaPoblacion();
			break;
			case 3:
				BG.evolucionaPoblacion();
			break;
			case 4:
				AA.evolucionaPoblacion();
			break;
		}
	}
	
	private void actualizaFigurasDeIndividuosDeElite(){
		individuosElite.clear();
		for(int i = 0 ; i < individuosEliteSize;i++){
			individuosElite.add(Figura.obtenFiguraPorGen(temporal, poblacion.get(i)));
		}	
	}
	
	public ArrayList<Figura> getMejoresFiguras(){
		actualizaFigurasDeIndividuosDeElite();
		return individuosElite;
	}
	
	public ArrayList<Figura> getPoblacion(){
		ArrayList<Figura> figurasPoblacion = new ArrayList<Figura>();
		for(Gen g:poblacion){
			figurasPoblacion.add(Figura.obtenFiguraPorGen(temporal, g));
		}
		return figurasPoblacion;
	}

	
}
