package escom.alejandro.evolutivos;

import java.util.ArrayList;

import escom.alejandro.pojos.Gen;

public class AlgoritmosAjuste {
	public static ArrayList<Gen> poblacion;
	public static int individuosElite;
	public static int poblacionSize;
	public static int[] maxDesplazamientosEnAjuste = new int[]{30,30};//TODO: Eliminar Hard code
	
	public AlgoritmosAjuste (){
		poblacion = new ArrayList<Gen>();
		individuosElite = Evolutivos.individuosEliteSize;
		poblacionSize = Evolutivos.poblacionSize/2;
	}	
	
	public void evolucionaPoblacion(){
		Gen g,gradiente;
		Gen individuoOriginal;
		
		//Para los individuos de elite
		for(int j = 0 ; j < Evolutivos.individuosEliteSize;j++){
			individuoOriginal = Evolutivos.poblacion.get(j);
			
			if(individuoOriginal.genAjuste() == null)
				individuoOriginal.setGenAjuste(new Gen());
			
			poblacion.clear();
			
			poblacion.add(individuoOriginal);
			//Primero, creamos offsets de ajuste aleatorios
			for(int i = 0 ; i < poblacionSize;i++){
				g = individuoOriginal.copia();
				g.setGenAjuste(new Gen());
				g.genAjuste().creaValoresAleatoriosParaAjuste();
				poblacion.add(g);
				g = null;
			}
			//A cada individuo creado le buscamos su particula de mejor ajuste y probamos 6 angulos.
			BusquedaGradiente.evolucionaPoblacionDeAjuste();
			
			UtileriaPoblacional.ordenaPoblacionAjuste();
			//AlgoritmoGenetico.evolucionaPoblacionDeAjuste(0.2f);
			//UtileriaPoblacional.ordenaPoblacionAjuste();
			
			gradiente = poblacion.get(0);
			individuoOriginal.setGenAjuste(gradiente.genAjuste());
			
		}
		
	}
}
