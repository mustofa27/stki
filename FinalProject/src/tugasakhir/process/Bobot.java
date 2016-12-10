/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

/**
 *
 * @author LENOVO
 */
public class Bobot implements Comparable<Bobot> {

    public Bobot(double similarity, String kata) {
        this.similarity = similarity;
        this.kata = kata;
    }

    public String getKata() {
        return kata;
    }

    public void setKata(String kata) {
        this.kata = kata;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
    double similarity;
    String kata;

    @Override
    public int compareTo(Bobot o) {
        if (this.getSimilarity()<o.getSimilarity()){
            return 1;
        }else{
            return -1;
        }
    }
}
