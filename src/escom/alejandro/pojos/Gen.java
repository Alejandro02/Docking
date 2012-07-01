package escom.alejandro.pojos;

import escom.alejandro.evolutivos.AlgoritmosAjuste;
import escom.alejandro.evolutivos.Evolutivos;
import escom.alejandro.utileria.Constantes;

/*
 * Representa un individuo
 */
public class Gen {
	private boolean reflejado;
	private int angulo;
	private float[] offsets;
	private int indiceParticulapivoteRotacion;
	private float afinidad;
	private Gen genAjuste;
	
	public Gen(){
		reflejado = false;
		angulo = 0;
		offsets = new float[Constantes.DIMENSIONES];
		indiceParticulapivoteRotacion = -1;
		afinidad = 0;
		offsets[Constantes.X_INDEX] = offsets[Constantes.Y_INDEX] = 0;
		genAjuste = null;
	}
	
	public Gen genAjuste(){
		return genAjuste;
	}
	
	public void setGenAjuste(Gen g){
		genAjuste =g;
	}
	
	public void creaValoresAleatorios(){
		reflejado = Constantes.rand.nextBoolean();
		angulo = Constantes.rand.nextInt(360);
		
		for(int i = 0 ; i < Constantes.DIMENSIONES;i++)
			offsets[i] = Constantes.rand.nextInt(Evolutivos.maxDesplazamientos[i])*(Constantes.rand.nextBoolean()?1:-1);
		
	}
	
	public void creaValoresAleatoriosParaAjuste(){
		reflejado = Constantes.rand.nextBoolean();
		
		for(int i = 0 ; i < Constantes.DIMENSIONES;i++)
			offsets[i] = Constantes.rand.nextInt(AlgoritmosAjuste.maxDesplazamientosEnAjuste[i])*
			(Constantes.rand.nextBoolean()?1:-1);
	}

	public void setReflejado(boolean reflejado) {
		this.reflejado = reflejado;
	}

	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	public void setOffsets(float[] offsets) {
		this.offsets = offsets;
	}

	public void setParticulaPivoteRotacion(int pivoteRotacion) {
		this.indiceParticulapivoteRotacion = pivoteRotacion;
	}

	public boolean refleja() {
		return reflejado;
	}

	public int angulo() {
		return angulo;
	}
	
	public int anguloNegativo(){
		return -angulo;
	}

	public float[] offsets() {
		return offsets;
	}
	
	public float[] offsetsNegativos() {
		return new float[]{-offsets[0],-offsets[1]};
	}
	
	public float offset(int index){
		return offsets[index];
	}
	
	public void setOffset(float value,int index){
		this.offsets[index] = value;
	}
	

	public int getIndicePivoteRotacion() {
		return indiceParticulapivoteRotacion;
	}
	
	public float afinidad(){
		return afinidad;
	}
	
	public void setAfinidad(float afinidad){
		this.afinidad = afinidad;
	}
	
	public Gen cruza(Gen otroGen){
		Gen nuevoGen = new Gen();
		
		nuevoGen.setAngulo(otroGen.angulo());
		nuevoGen.setReflejado(this.refleja());
		nuevoGen.setOffsets(new float[]{this.offset(Constantes.X_INDEX),otroGen.offset(Constantes.Y_INDEX)});
		nuevoGen.setParticulaPivoteRotacion(this.getIndicePivoteRotacion());
		
		return nuevoGen;
	}
	
	public void muta(){
		reflejado = Constantes.rand.nextBoolean();
		
		angulo = Constantes.rand.nextInt(360);
		
		for(int i = 0 ; i < Constantes.DIMENSIONES;i++)
			offsets[i] = Constantes.rand.nextInt(Evolutivos.maxDesplazamientos[i]+1);
	}
	
	public String toString(){
		return "Reflejado:"+reflejado+"," +
				"Angulo:"+angulo+"," +
				"Offsets:"+offsets[0]+","+offsets[1]+"," +
				"Tiene Pivote"+((indiceParticulapivoteRotacion== -1)? "No": "Si"+indiceParticulapivoteRotacion)+
				",Afinidad"+afinidad+",Gen de ajuste"+genAjuste;
	}
	
	public boolean estaCercano(Gen g,int umbralDistancia){
		float distancia;
		distancia = (float) Math.pow(	
				Math.pow(g.offset(Constantes.X_INDEX) - this.offset(Constantes.X_INDEX),2) + 
				Math.pow(g.offset(Constantes.Y_INDEX) - this.offset(Constantes.Y_INDEX),2),0.5);
		return distancia < umbralDistancia ? true:false;
		
	}
	
	public boolean esIgual(Gen i){
		if(	this.offsets[Constantes.X_INDEX] == i.offset(Constantes.X_INDEX) &&
			this.offsets[Constantes.Y_INDEX] == i.offset(Constantes.Y_INDEX) &&
			this.angulo == i.angulo() &&
			this.reflejado == i.refleja() &&
			this.indiceParticulapivoteRotacion == i.getIndicePivoteRotacion())
			
			return true;
		return false;
	}
	
	public Gen copia(){
		Gen vecino= new Gen();
		Gen ajuste;
		
		vecino.setAngulo(this.angulo());
		vecino.setOffsets(this.offsets());
		vecino.setParticulaPivoteRotacion(this.getIndicePivoteRotacion());
		vecino.setReflejado(this.refleja());
		if(this.genAjuste() != null){
			ajuste = this.genAjuste().copia();
			vecino.setGenAjuste(ajuste);
		}
		return vecino;
	}

}
