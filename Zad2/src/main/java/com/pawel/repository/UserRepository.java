package com.pawel.repository;

import com.pawel.userutils.Role;
import com.pawel.userutils.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository{

    private final List<User> users;
    public UserRepository() {
        this.users = new ArrayList<>();
    }

    @Override
    public User getUser(String login) {
        File file = new File("users.csv");
        String line;
        User user = null;
        if(file.exists()){
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
                while((line = bufferedReader.readLine()) != null){
                    String[] params = line.split(",");
                    if(params[0].equals(login)){
                        Role role = Role.valueOf(params[2]);
                        user = new User(params[0], params[1], role, Integer.parseInt(params[3]));
                        break;
                    }
                }
            }catch(Exception e){
                throw new RuntimeException("UR getUser error. " + e.getMessage());
            }
        }

        return user;
    }

    @Override
    public List<User> getUsers() {
        return this.users;
    }

    @Override
    public void save() {
        File file = new File("users.csv");
        String line;
        try(PrintWriter printWriter = new PrintWriter(new FileWriter(file))){
            if(!file.exists()){
                file.createNewFile();
            }
            for(User u : this.users){
                line = String.format("%s,%s,%s,%d\n", u.getLogin(), u.getPassword(), u.getRole().name(),u.getCarId());
                printWriter.append(line);
            }

        }catch (IOException e){
            throw new RuntimeException("UR save error.");
        }
    }

    public void addUser(User user){
        this.users.add(user);
        save();
    }

    public void changeCarStatus(User user){
        for(User u : this.users){
            if(u.getLogin().equals(user.getLogin())){
                //u.carId = user.carId;
                u.setCarId(user.getCarId());
                save();
                break;
            }
        }
    }
}
