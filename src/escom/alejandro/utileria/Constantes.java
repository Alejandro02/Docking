package escom.alejandro.utileria;

import java.util.Random;

public abstract class Constantes {
	public final static int X_INDEX = 0;
	public final static int Y_INDEX = 1;
	public static int ACTIVITY_HEIGHT = 0;
	public static int ACTIVITY_WIDTH = 0;
	public final static int DIMENSIONES = 2;
	
	public final static int IZ_INDEX = 0;
	public final static int AB_INDEX = 1;
	public final static int AR_INDEX = 2;
	public final static int DE_INDEX = 3;
	
	public static int[] offset = new int[3];
	public static int[] escala = new int[2];
	public static Random rand = new Random();
	public static int INFINITO = 65536;
	public static int TRASLAPE = -1;
	public static int INDICE_CENTRO_FIGURA = -1;
}
