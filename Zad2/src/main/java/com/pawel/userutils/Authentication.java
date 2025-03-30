package com.pawel.userutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Authentication {
    public static boolean isLoginDataCorrect(String login, String password, Role role){
        File file = new File("users.csv");
        String line;
        if(file.exists()){
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
                while((line = bufferedReader.readLine()) != null){
                    String[] params = line.split(",");
                    if(params[0].equals(login) && params[1].equals(password) && params[2].equals(role.name())){
                        return true;
                    }
                }
            }catch(IOException e){
                System.out.println("Auth error. " + e.getMessage());
            }
        }
        return false;
    }
}
