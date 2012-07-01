package escom.alejandro.utileria;


public abstract class Transformaciones {
	
	private static float[][] matrizTransformacion = new float[Constantes.DIMENSIONES+1][Constantes.DIMENSIONES+1];
	
	public static void loadIdentity(){
		for(int i = 0 ; i < Constantes.DIMENSIONES+1;i++)
			for(int j = 0 ; j < Constantes.DIMENSIONES+1;j++)
				matrizTransformacion[i][j] = (i==j)?1:0;
		
	}
	
	public static float[][] multiplicaMatrices(float[][] matriz1, float[][] matriz2){
		float[][] matrizResultado = null;
		
		if(matriz2.length != matriz1[0].length)
			return null;
		
		matrizResultado = new float[matriz1.length][matriz2[0].length];
		
		for(int i = 0 ;i < matriz2[0].length;i++){
			for(int j = 0 ;j < matriz1.length;j++){
				for(int k = 0 ; k <matriz2.length;k++)
					matrizResultado[j][i] += matriz2[k][i]*matriz1[j][k];
			}
		}
					
		return matrizResultado;
	} 
	
	private static float[][] creaMatrizTraslacion(float[] trasladar){
		float[][] matrizTraslacion = new float[Constantes.DIMENSIONES+1][Constantes.DIMENSIONES+1];
		
		for(int i = 0 ; i < Constantes.DIMENSIONES+1;i++){
			for(int j = 0 ; j < Constantes.DIMENSIONES+1;j++){
				matrizTraslacion[i][j] = 0;
				if(i == j)
					matrizTraslacion[i][j] = 1;
				if(j == trasladar.length && i != j)
					matrizTraslacion[i][j] = trasladar[i];
			}
		}
		return matrizTraslacion;
	}
	
	private static float[][] creaMatrizReflexionAbscisas(){
	    float[][] matrizReflexion = {new float[]{-1f,0f,0f},
	                                 new float[]{0f,1f,0f},
	                                 new float[]{0f,0f,1f}};

	    return matrizReflexion;
	}
	
	private static float[][] creaMatrizEscalamiento(float[] escalar){
		float[][] matrizEscalamiento = new float[Constantes.DIMENSIONES+1][Constantes.DIMENSIONES+1];
		
		for(int i = 0 ; i < Constantes.DIMENSIONES+1;i++){
			for(int j = 0 ; j < Constantes.DIMENSIONES+1;j++){
				matrizEscalamiento [i][j] = 0;
				if(i == j && j != escalar.length)
					matrizEscalamiento[i][j] = escalar[i];
				if(i == j  && j == escalar.length)
					matrizEscalamiento[i][j] = 1;
			}
		}
		return matrizEscalamiento;
	}
	
	private static float[][] creaMatrizRotacionR2(double escalar){
		float[][] matrizRotacion = new float[Constantes.DIMENSIONES+1][Constantes.DIMENSIONES+1];
		
		for(int i = 0 ; i < Constantes.DIMENSIONES+1;i++){
			for(int j = 0 ; j < Constantes.DIMENSIONES+1;j++){
				matrizRotacion[i][j] = 0;
			}
		}
		
		double enRadianes = Math.toRadians(escalar);
		
		matrizRotacion[0][0] = (float) Math.cos(enRadianes);
		matrizRotacion[1][0] = (float) Math.sin(enRadianes);
		matrizRotacion[0][1] = -1*(float)Math.sin(enRadianes);
		matrizRotacion[1][1] = (float) Math.cos(enRadianes);
		matrizRotacion[2][2] = 1;
			    
		return matrizRotacion;
	}
	
	public static void addT(float tx,float ty){
		float[][] T = creaMatrizTraslacion(new float[]{tx,ty});
		matrizTransformacion = multiplicaMatrices(T, matrizTransformacion);
	}
	
	public static void addS(int Sx,int Sy){
		float[][] T = creaMatrizEscalamiento(new float[]{Sx,Sy});
		matrizTransformacion = multiplicaMatrices(T, matrizTransformacion);
	}
	
	public static void addR(int angulo){
		float[][] T = creaMatrizRotacionR2(angulo);
		matrizTransformacion = multiplicaMatrices(T, matrizTransformacion);
	}
	
	public static void addReflexion(){
		float[][] T = creaMatrizReflexionAbscisas();
		matrizTransformacion = multiplicaMatrices(T, matrizTransformacion);
	}
	
	public static float[] transforma(float[] punto){
		float[][] g = new float[3][1];
		
		g[0][0] = punto[0];
		g[1][0] = punto[1];
		g[2][0] = 1;
		
		g = multiplicaMatrices(matrizTransformacion, g);
		float[] resultado = {g[0][0],g[1][0]};
		
		return resultado; 
	}
	
}
