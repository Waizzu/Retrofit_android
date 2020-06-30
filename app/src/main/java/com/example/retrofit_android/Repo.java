package com.example.retrofit_android;
public class Repo {
    int id;
    String name;
    String full_name;// las variables tienen que ser las mismas que las de JSON donde atacamos

    @Override
    public String toString(){
        return id+" "+name+" > "+full_name;//la tienen todos los objetos el override no importa
    }
}
