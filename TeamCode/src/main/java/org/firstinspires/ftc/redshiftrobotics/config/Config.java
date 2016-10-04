package org.firstinspires.ftc.redshiftrobotics.config;

import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.*;

/**
 * Created by adam on 9/29/16.
 */
public class Config <E extends Number>  { //template class can be used with any numerical constant, i.e.: double, float, long, etc.

    private Map<String, ConfigVariable<E>> variables = null; //map of names to config variables. All config variables must have the same type as the containing class.

    public Config() {
        this.variables = new HashMap<>();
    }

    public void add(String name, ConfigVariable<E> val) {
        this.variables.put(name, val);
    } //add a value

    public void readFrom(String filename) throws Exception { //fill in table with values from a json file
        FileInputStream fis;

        try {
            fis = new FileInputStream(filename);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        int read;
        StringBuilder str = new StringBuilder();

        while ((read = fis.read()) != -1) {
            str.append((char) read);
        }

        JSONObject obj = new JSONObject(str.toString()); //variables are stored in a json meta-object called variables

        /* i.e.:
            {
                "variables":{
                    "varname":{
                        "val": _,
                        "min": _,
                        "max": _"
                     },
                     ...
            }
         */

        JSONObject vars = obj.getJSONObject("variables");

        Iterator<String> keys = vars.keys(); //iterator for keys in variables json object
        JSONObject current; //the current object

        E value, min, max; //the min, max, and value data extracted from the current object

        while (keys.hasNext()) {
            String key = keys.next();
            if (vars.get(key) instanceof JSONObject) {
                current =  vars.getJSONObject(key);
                try {
                    value = (E) current.get("val");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                String name = key;
                ConfigVariable<E> c = new ConfigVariable<>(value);
                this.variables.put(name, c);
            }
        }
    }
    public ConfigVariable<E> get(String name) {
        if (this.variables.containsKey(name)) {
            return this.variables.get(name);
        } else {
            return null;
        }
    }
    public void set (String name, ConfigVariable<E> var) {
        this.variables.put(name, var);
    }
}