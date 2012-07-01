package escom.alejandro.evolutivos;

import java.util.ArrayList;

import escom.alejandro.pojos.Gen;

public class UtileriaPoblacional {
	
	public void sustituyeIndividuosTraslapados(){
		for(int i = Evolutivos.poblacionSize-1;i > 0;i--){
			if(Evolutivos.poblacion.get(i).afinidad() < 0){
				Evolutivos.poblacion.get(i).creaValoresAleatorios();
			}else{
				break;
			}
		}
	}
	
	public void eliminaGemelos(){
		Gen g1,g2;
		for(int i = 1 ; i < Evolutivos.individuosEliteSize-1;i++){
			for(int j = i+1 ; j < Evolutivos.individuosEliteSize;j++){
				g1 = Evolutivos.poblacion.get(i);
				g2 = Evolutivos.poblacion.get(j);
				if(g1.estaCercano(g2, Evolutivos.umbralGemelos)){
					Evolutivos.poblacion.get(j).muta();
				}
			}	
		}
	}
	
	public static int buscaMejorPivote(Gen g){
		int mejorParticula;
		Evolutivos.temporal.modificaFiguraPorGen(g);
		mejorParticula = Evolutivos.temporal.indiceParticulaConMejorAfinidad(Evolutivos.figura2);
		Evolutivos.temporal.reestableceFiguraPorGen(g);
		return mejorParticula;
	}
	
	/*
	 *Establece la afinidad de un gen 
	 */
	public static void  evaluaGen(Gen g){
		float afinidad;
		Evolutivos.temporal.modificaFiguraPorGen(g);
		Evolutivos.temporal.modificaFiguraPorGen(g.genAjuste());
		afinidad = Evolutivos.temporal.afinidadGeneral(Evolutivos.figura2);
		g.setAfinidad(afinidad);
		Evolutivos.temporal.reestableceFiguraPorGen(g.genAjuste());
		Evolutivos.temporal.reestableceFiguraPorGen(g);
	}
	
	public static void ordenaPoblacion(){
		ArrayList<Gen> poblacionOrdenada = new ArrayList<Gen>();
		Gen actual;
		
		//Primero calculamos las afinidades de cada gen
		for(Gen g:Evolutivos.poblacion){
			UtileriaPoblacional.evaluaGen(g);
		}
		
		//Metiendo en una nueva lista ordenada
		for(int i = 0 ; i < Evolutivos.poblacionSize;i++){
			actual = Evolutivos.poblacion.get(i);
			if(i == 0){
				poblacionOrdenada.add(actual);
			}else{
				//La solucion actual es mejor que la mejor solucion de la poblacion hasta el momento
				if(actual.afinidad() >= poblacionOrdenada.get(0).afinidad()){
					poblacionOrdenada.add(0,actual);
				}
				//La solucion actual es peor que la peor solucion de la poblacion hasta el momento
				else if(actual.afinidad() <= poblacionOrdenada.get(poblacionOrdenada.size()-1).afinidad() ){
					poblacionOrdenada.add(poblacionOrdenada.size(),actual);
				}
				//La solucion actual esta en algun punto medio
				else{
					for(int j = 0; j < poblacionOrdenada.size()-1;j++){
						if(	poblacionOrdenada.get(j).afinidad() >= actual.afinidad() && 
							poblacionOrdenada.get(j+1).afinidad() <= actual.afinidad()){
							poblacionOrdenada.add(j+1,actual);
							break;
						}
					}
				}
			}
		}
		
		Evolutivos.poblacion.clear();

		for(Gen g:poblacionOrdenada){
			Evolutivos.poblacion.add(g);
		}
	}
	
	public static void ordenaPoblacionAjuste(){
		ArrayList<Gen> poblacionOrdenada = new ArrayList<Gen>();
		Gen actual;
		
		//Primero calculamos las afinidades de cada gen
		for(Gen g:AlgoritmosAjuste.poblacion){
			UtileriaPoblacional.evaluaGen(g);
		}
		//Metiendo en una nueva lista ordenada
		for(int i = 0 ; i < AlgoritmosAjuste.poblacionSize;i++){
			actual = AlgoritmosAjuste.poblacion.get(i);
			if(i == 0){
				poblacionOrdenada.add(actual);
			}else{
				//La solucion actual es mejor que la mejor solucion de la poblacion hasta el momento
				if(actual.afinidad() >= poblacionOrdenada.get(0).afinidad()){
					poblacionOrdenada.add(0,actual);
				}
				//La solucion actual es peor que la peor solucion de la poblacion hasta el momento
				else if(actual.afinidad() <= poblacionOrdenada.get(poblacionOrdenada.size()-1).afinidad() ){
					poblacionOrdenada.add(poblacionOrdenada.size(),actual);
				}
				//La solucion actual esta en algun punto medio
				else{
					for(int j = 0; j < poblacionOrdenada.size()-1;j++){
						if(	poblacionOrdenada.get(j).afinidad() >= actual.afinidad() && 
							poblacionOrdenada.get(j+1).afinidad() <= actual.afinidad()){
							poblacionOrdenada.add(j+1,actual);
							break;
						}
					}
				}
			}
		}
		
		AlgoritmosAjuste.poblacion.clear();

		for(Gen g:poblacionOrdenada){
			AlgoritmosAjuste.poblacion.add(g);
		}
	}
	
}
