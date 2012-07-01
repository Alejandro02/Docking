package escom.alejandro.evolutivos;

import java.util.ArrayList;

import escom.alejandro.pojos.Gen;
import escom.alejandro.utileria.Constantes;

/**
 *Busqueda de vecindario por Artificial Bee Colony. 
 **/
public class BusquedaVecindarioBA {
	private int sitiosSeleccionados;
	private int mejoresSitios;
	private int individuosParaSitiosSeleccionados;
	private int individuosParaMejoresSitios;
	
	private final int saltoAngulo = 5;
	private final int saltoOffset = 3;
	
	public BusquedaVecindarioBA(int individuosParaSitiosSeleccionados,int individuosParaMejoresSitios){
		sitiosSeleccionados = (int)Evolutivos.poblacionSize/2;
		mejoresSitios = Evolutivos.individuosEliteSize;
		this.individuosParaSitiosSeleccionados = individuosParaSitiosSeleccionados;
		this.individuosParaMejoresSitios = individuosParaMejoresSitios;
	}
	
	public void evolucionaPoblacion(){
		ArrayList<Gen> vecindario = new ArrayList<Gen>();
		Gen mejorIndividuoEnVecindario;
		//Buscando en los mejores sitios
		for(int i = 0 ; i < sitiosSeleccionados;i++){
			vecindario.add(Evolutivos.poblacion.get(i));
			for(int j = 0 ; j < (i<mejoresSitios?individuosParaMejoresSitios:individuosParaSitiosSeleccionados);j++){
                vecindario.add(individuoEnVecindario(Evolutivos.poblacion.get(i),(i<mejoresSitios?true:false)));
            }
			
			mejorIndividuoEnVecindario = mejorIndividuoEnVecindario(vecindario);
			Evolutivos.poblacion.set(i, mejorIndividuoEnVecindario);
			vecindario.clear();
		}
		vecindario = null;
	}
	
	/**
	 * La bandera sirve para indicar el tama�o del salto
	 **/
	private Gen individuoEnVecindario(Gen i,boolean mejoresIndividuos){
		Gen vecino= new Gen();
		int maxSalto;
		
		maxSalto = mejoresIndividuos ? 4:2;
		
		vecino.setAngulo(i.angulo() + (Constantes.rand.nextInt(maxSalto)*saltoAngulo*(Constantes.rand.nextBoolean()?-1:1)));
		
		vecino.setOffsets(new float[]{
							i.offset(Constantes.X_INDEX)+(Constantes.rand.nextInt(maxSalto)*saltoOffset*(Constantes.rand.nextBoolean()?-1:1)),
							i.offset(Constantes.Y_INDEX)+(Constantes.rand.nextInt(maxSalto)*saltoOffset*(Constantes.rand.nextBoolean()?-1:1))
							});
		vecino.setReflejado(Constantes.rand.nextBoolean());
		
		if(Constantes.rand.nextFloat() < 0.2)//Probabilidad baja de utilizar otra particula como pivote
			vecino.setParticulaPivoteRotacion(Constantes.rand.nextInt(Evolutivos.figura1Particulas+1)-1);
		return vecino;
	}
	
	public Gen mejorIndividuoEnVecindario(ArrayList<Gen> vecindario){
		Gen mejorFigura = null;
		
		for(Gen g:vecindario){
			UtileriaPoblacional.evaluaGen(g);
			
			if(mejorFigura == null){
				mejorFigura = g;
			}else{
				if(g.afinidad() > mejorFigura.afinidad())
					mejorFigura = g;
			}
		}
		return mejorFigura;
	}
}
