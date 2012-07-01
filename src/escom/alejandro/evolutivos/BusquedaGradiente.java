package escom.alejandro.evolutivos;

import java.util.ArrayList;

import escom.alejandro.pojos.Gen;
import escom.alejandro.utileria.Constantes;

public class BusquedaGradiente {
	private final static int saltoOffset = 5;
	private final static int saltoAngulo = 5;
	
	public static void evolucionaPoblacionDeAjuste(){
		Gen  g;
		for(int i = 0 ; i < AlgoritmosAjuste.poblacionSize;i++){
			g = gradienteAjusteDeAngulo(AlgoritmosAjuste.poblacion.get(i));
			
			if(g != null)
				AlgoritmosAjuste.poblacion.set(i, g);
		}
	}
	
	private static Gen gradienteAjusteDeAngulo(Gen i){
		int mejorParticula;
		Gen deltaAngulo,gradiente = null;
		ArrayList<Gen> vecindario = new ArrayList<Gen>();
		
		mejorParticula = UtileriaPoblacional.buscaMejorPivote(i);
		i.genAjuste().setParticulaPivoteRotacion(mejorParticula);
		
		vecindario.add(i);
		
		for(int c = 0 ; c < 3;c++){
			deltaAngulo = i.copia();
			deltaAngulo.genAjuste().setAngulo(saltoAngulo*(c+1));
			vecindario.add(deltaAngulo);
			
			deltaAngulo = null;
			
			deltaAngulo = i.copia();
			deltaAngulo.genAjuste().setAngulo(-saltoAngulo*(c+1));
			vecindario.add(deltaAngulo);
			
			deltaAngulo = null;
		}

		for(Gen g:vecindario){
			UtileriaPoblacional.evaluaGen(g);
			
			if(gradiente == null)
				gradiente = g;
			else
				gradiente = g.afinidad() > gradiente.afinidad()? g:gradiente;
		}
		
		if(!gradiente.esIgual(i) && !gradiente.genAjuste().esIgual(i.genAjuste())){
			return gradiente;
		}
		return null;
	}
	
	public void evolucionaPoblacion(){
		Gen  g;
		for(int i = 0 ; i < Evolutivos.individuosEliteSize;i++){
			gradienteOffset(Evolutivos.poblacion.get(i), Constantes.X_INDEX);
			
			gradienteOffset(Evolutivos.poblacion.get(i), Constantes.Y_INDEX);
			
			g = gradienteAngulo(Evolutivos.poblacion.get(i));
			
			if(g != null)
				Evolutivos.poblacion.set(i, g);
		}
	}
	
	private Gen gradienteAngulo(Gen i){
		Gen deltaAnguloPositivo,deltaAnguloNegativo,deltaAngulo,gradiente = null;
		ArrayList<Gen> vecindario = new ArrayList<Gen>();
		
		vecindario.add(i);
		
		deltaAngulo = i.copia();
		deltaAngulo.setAngulo(i.angulo()+saltoAngulo);
		vecindario.add(deltaAngulo);
		
		deltaAngulo = null;
		
		deltaAngulo = i.copia();
		deltaAngulo.setAngulo(i.angulo()-saltoAngulo);
		vecindario.add(deltaAngulo);
		
		deltaAngulo = null;

		for(int j = -1 ; j < Evolutivos.figura1Particulas;j++ ){
			deltaAnguloPositivo = i.copia();
			deltaAnguloPositivo.setAngulo(i.angulo()+saltoAngulo);
			deltaAnguloPositivo.setParticulaPivoteRotacion(j);
			vecindario.add(deltaAnguloPositivo);
			
			deltaAnguloPositivo = null;
			
			deltaAnguloNegativo = i.copia();
			deltaAnguloNegativo.setAngulo(i.angulo()-saltoAngulo);
			deltaAnguloNegativo.setParticulaPivoteRotacion(j);
			vecindario.add(deltaAnguloNegativo);
			
			deltaAnguloNegativo = null;
		}
		
		for(Gen g:vecindario){
			UtileriaPoblacional.evaluaGen(g);
			
			if(gradiente == null)
				gradiente = g;
			else
				gradiente = g.afinidad() > gradiente.afinidad()? g:gradiente;
		}
		
		if(!gradiente.esIgual(i)){
			return gradiente;
			
		}
		return null;
	}
	
	private void gradienteOffset(Gen i, int offsetIndex){
		float value;
		
		i.setOffset(i.offset(offsetIndex)+saltoOffset,offsetIndex);
		
		Evolutivos.temporal.modificaFiguraPorGen(i);
		value = Evolutivos.temporal.afinidadGeneral(Evolutivos.figura2);
		Evolutivos.temporal.reestableceFiguraPorGen(i);
		
		if(value > i.afinidad()){
			i.setAfinidad(value);
			return;
		}
		
		//Quitando un salto extra para regresar al offset original.
		i.setOffset(i.offset(offsetIndex)-2*saltoOffset,offsetIndex);
		
		Evolutivos.temporal.modificaFiguraPorGen(i);
		value = Evolutivos.temporal.afinidadGeneral(Evolutivos.figura2);
		Evolutivos.temporal.reestableceFiguraPorGen(i);
		
		if(value > i.afinidad()){
			i.setAfinidad(value);
			return;
		}
		
		//Si ninguno de los dos offsets da beneficio, regreso el Gen a su estado original
		i.setOffset(i.offset(offsetIndex)+saltoOffset,offsetIndex);
	}
}
