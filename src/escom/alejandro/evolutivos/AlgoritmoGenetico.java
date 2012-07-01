package escom.alejandro.evolutivos;

import escom.alejandro.pojos.Gen;
import escom.alejandro.utileria.Constantes;

public class AlgoritmoGenetico {
	private float tasaMutacion;
	
	public AlgoritmoGenetico(float tasaMutacion){
		this.tasaMutacion = tasaMutacion;
	}
	
	public void evolucionaPoblacion(){
		int gen1,gen2;
		Gen genSiguienteGeneracion;
		
		for(int i = 0 ; i < Evolutivos.poblacionSize;i++){
			if(i < Evolutivos.individuosEliteSize){//Elitismo
				genSiguienteGeneracion = Evolutivos.poblacion.get(i);
			}else if(i >= Evolutivos.individuosEliteSize && i <Evolutivos.individuosEliteSize*2){//Cruza entre individuos de elite
				gen1 = gen2 = Constantes.rand.nextInt(Evolutivos.individuosEliteSize);
				
				while(gen1 == gen2)
					gen1 = Constantes.rand.nextInt(Evolutivos.individuosEliteSize);
				
				genSiguienteGeneracion = Evolutivos.poblacion.get(gen1).cruza(Evolutivos.poblacion.get(gen2));
			}else{//Cruza entre individuos cualesquiera de la poblacion
				gen1 = gen2 = Constantes.rand.nextInt(Evolutivos.poblacionSize);
				
				while(gen1 == gen2)
					gen1 = Constantes.rand.nextInt(Evolutivos.poblacionSize);

				genSiguienteGeneracion = Evolutivos.poblacion.get(gen1).cruza(Evolutivos.poblacion.get(gen2));
			}
			
			//Mutacion
			if(Math.random() < tasaMutacion )
				genSiguienteGeneracion.muta();
			
			Evolutivos.poblacion.set(i, genSiguienteGeneracion);
		}
	}
	
	public static void evolucionaPoblacionDeAjuste(float tasaMutacion){
		int gen1,gen2;
		Gen genSiguienteGeneracion;
		
		for(int i = 0 ; i < AlgoritmosAjuste.poblacionSize;i++){
			
			if(i < AlgoritmosAjuste.individuosElite){//Elitismo
				genSiguienteGeneracion = AlgoritmosAjuste.poblacion.get(i);
			}else if(i >= AlgoritmosAjuste.individuosElite&& i <AlgoritmosAjuste.individuosElite*2){//Cruza entre individuos de elite
				gen1 = gen2 = Constantes.rand.nextInt(AlgoritmosAjuste.individuosElite);
				
				while(gen1 == gen2)
					gen1 = Constantes.rand.nextInt(AlgoritmosAjuste.individuosElite);
				
				genSiguienteGeneracion = AlgoritmosAjuste.poblacion.get(gen1).cruza(AlgoritmosAjuste.poblacion.get(gen2));
			}else{//Cruza entre individuos cualesquiera de la poblacion
				gen1 = gen2 = Constantes.rand.nextInt(AlgoritmosAjuste.poblacionSize);
				
				while(gen1 == gen2)
					gen1 = Constantes.rand.nextInt(AlgoritmosAjuste.poblacionSize);

				genSiguienteGeneracion = AlgoritmosAjuste.poblacion.get(gen1).cruza(AlgoritmosAjuste.poblacion.get(gen2));
			}
			
			if(Math.random() < tasaMutacion )
				genSiguienteGeneracion.muta();
			
			AlgoritmosAjuste.poblacion.set(i, genSiguienteGeneracion);
		}
		
	}
}
