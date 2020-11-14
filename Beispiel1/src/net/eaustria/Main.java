/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eaustria;

import java.util.Scanner;

/**
 *
 * @author timst
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner scanener = new Scanner(System.in, "Windows-1252");
        System.out.println("Bei welcher Zahl soll angefangen werde?");
        int begin = Integer.parseInt(scanener.nextLine());
        System.out.println("Bei welcher Zahl soll aufgehört werden?");
        int end = Integer.parseInt(scanener.nextLine());
        double[] input = new double[(end - begin) + 1];
        int j = 0;
        for (int i = begin; i <= end; i++) {
            input[j] = i;
            j++;
        }

        System.out.println(ReciprocalArraySum.parManyTaskArraySum(input, 100));

    }

}
