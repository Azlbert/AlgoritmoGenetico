package algoritmogenetico;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgoritmoGenetico {
    public static void main(String[] args) {
        Poblacion poblacion = new Poblacion(60);
        poblacion.imprimeGneracion();
        System.out.println("");
        
        //Bucle para crear las generaciones de la poblacion
        for (int i = 0; i < 20; i++) {
            poblacion.crearGeneracion();
            poblacion.imprimeGneracion();
            System.out.println("");
        }
        
     
    }
    
    public static float GetFenotipo(byte genotipo){
        return -(4.0f - (genotipo * 0.2f));
    }
    
    public static float Funcion(byte genotipo){
        float fenotipo = GetFenotipo(genotipo);
        float pot2 = fenotipo * fenotipo;
        float pot3 = pot2 * fenotipo;
        float pot4 = pot3 * fenotipo;
        
        return pot4 + 5*pot3 + 4*pot2 - 4*fenotipo + 1;
    }
}

class Poblacion{
    private static Random r = new Random();
    private List<List<Individuo>> generaciones = new ArrayList<>();
    private List<Individuo> padres;
    private List<Individuo> hijos;
    private final int tamanoPoblacion;
    
    public Poblacion(int tamanoPoblacion){
        if(tamanoPoblacion % 2 != 0){
            throw new java.lang.Error("La poblacion debe de ser par");
        }
        this.tamanoPoblacion = tamanoPoblacion;
        AdanYEva();
    }
    
    private void AdanYEva(){                   //Creamos la primera generacion
        List<Individuo> generacion = new ArrayList<>();
        generaciones.add(generacion);
        float fitnessTotal = 0.0f;
        Individuo individuo;
        for (int i = 0; i < tamanoPoblacion; i++) {
            individuo = new Individuo();
            generacion.add(individuo);                //Poblamos la generacion aleatoriamente
            fitnessTotal += individuo.getFitness();
        }
        setPorcentaje(generacion, fitnessTotal);
        padres = hijos = generacion;
    }
    
    public void crearGeneracion(){
        List<Individuo> generacion = new ArrayList<>();
        generaciones.add(generacion);
        hijos = generacion;
        Individuo padre = null;
        Individuo madre = null;
        float fitnessTotal = 0.0f;
        float ruleta;
        float sumatoria = 0.0f;
        float porcentaje;
        
        for (int i = 0; i < tamanoPoblacion / 2; i++) {
            //Escogemos al padre
            ruleta = r.nextInt(100)+1;
            for(Individuo individuo : padres){
                porcentaje = individuo.getPorcentaje();
                sumatoria += porcentaje;
                padre = individuo;
                if(sumatoria >= ruleta){
                    break;
                }
            }
            
            //Escogemos a la madre
            sumatoria = 0.0f;
            ruleta = r.nextInt(100)+1;
            for(Individuo individuo : padres){
                if(individuo == padre){
                    continue;
                }
                porcentaje = individuo.getPorcentaje();
                sumatoria += porcentaje;
                madre = individuo;
                if(sumatoria >= ruleta){
                    break;
                }
            }
            
            fitnessTotal = reproducir(padre.getCromosoma(), madre.getCromosoma());
        }
        setPorcentaje(generacion, fitnessTotal);
        padres = hijos;
    }
    
    private float reproducir(byte brian, byte britani){
        byte kevin = (byte)(brian & ~0b1111100);
        byte lupita = (byte)(britani & ~0b1111100);
        kevin |= ((byte)(britani & ~0b1110011));
        lupita |= ((byte)(brian & ~0b1110011));
        
        Individuo individuo = new Individuo(kevin);
        hijos.add(individuo);
        float totalFitness = individuo.getFitness();
        individuo = new Individuo(lupita);
        hijos.add(individuo);
        totalFitness += individuo.getFitness();
        return totalFitness;
    }
    
    public void imprimeGneracion(){
        for(Individuo individuo : hijos){
            System.out.println(individuo);
        }
    }
    
    private void setPorcentaje(List<Individuo> generacion, float fitnessTotal){
        for(Individuo individuo : generacion){
            individuo.setPorcentaje(fitnessTotal);
        }
    }
    
    public byte IndividuoDominante(){
        return 0b0;
    }
}

class Individuo{    
    private static Random r = new Random();
    private byte cromosoma;
    private float fitness;
    private float porcentaje;
    
    static private float offset = 7f;
    
    public Individuo(){
        setCromosoma((byte)r.nextInt(15));
        setFitness();
    }
    
    public Individuo(byte cromosoma){
        setCromosoma(cromosoma);
        setFitness();
    }
    
    private void setCromosoma(byte cromosoma){
        this.cromosoma = cromosoma;
    }
    
    public byte getCromosoma(){
        return cromosoma;
    }
    
    public void setPorcentaje(float fitnessTotal){
        porcentaje = (fitness / fitnessTotal) * 100;
    }
    
    public float getPorcentaje(){
        return porcentaje;
    }
    
    private void setFitness(){
        fitness = (-AlgoritmoGenetico.Funcion(cromosoma)) + offset;
    }
    
    public float getFitness(){
        return fitness;
    }

    @Override
    public String toString() {
        return "Individuo{" + "cromosoma=" + cromosoma + ", fenotipo = " + AlgoritmoGenetico.GetFenotipo(cromosoma)
                + ", fitness=" + fitness + ", porcentaje=" + porcentaje + '}';
    }
    
    
}