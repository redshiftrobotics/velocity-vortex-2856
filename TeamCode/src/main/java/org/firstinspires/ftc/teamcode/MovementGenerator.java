package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by matt on 2/15/17.
 */

class MovementGenerator {
    MovementGenerator(Robot r, ArrayList<Movement> m, Telemetry t) {
        this.moves = m;
        this.robot = r;
        this.tm = t;
    }

    public ArrayList<Movement> parceSettings(String filePath){
        // Retrieve file.
        File file = new File(filePath);
        StringBuilder text = new StringBuilder();
        // Attempt to load line from file into the buffer.
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            // Ensure that the first line is not null.
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            // Close the buffer reader
            br.close();
        }
        // Catch exceptions... Or don't because that would require effort.
        catch (IOException e) {
        }
        // Provide in a more user friendly form.
        String arrText = text.toString();

        return parceText(arrText);
    }

    private ArrayList<Movement> parceText(String text) {
        /*

        This is how things should be typed in the configuration menu:

        forward-0.1|turn-45|backward-2.1

        !!! NO BEGINNING AND ENDING delineators !!! or it will break

        */
        //Movement[] returnMoves = new Movement[]{};
        ArrayList<Movement> returnMoves = new ArrayList<Movement>(100); // 1 in initial capacity... should expand
        String[] objSplit = text.split("/");
        for(int i = 0; i < objSplit.length; i++) {
            String a = "forward-2.2".split("-")[0];
            returnMoves.add(new Movement(objSplit[i].split("-")[0], Float.parseFloat(objSplit[i].split("-")[1])));
        }
        return returnMoves;
    }

    public void move() {
        if(moves != null && !moves.isEmpty()) {

            for (Movement move : moves) {
                switch(move.moveType) {
                    case "forward":
                        robot.Straight(move.amount, forward, timeout, tm);
                    case "backward":
                        robot.Straight(move.amount, backward, timeout, tm);
                    case "turn":
                        robot.AngleTurn(move.amount, timeout, tm);
                    case "line_forward":
                        //robot.MoveToLine(forward, move.amount, timeout, tm);
                    case "line_backward":
                        //robot.MoveToLine(backward, move.amount, timeout, tm);
                }
            }

        }
    }

    public int timeout = 10;
    Robot robot;
    private ArrayList<Movement> moves;
    private Telemetry tm;
    Float[] forward = new Float[]{1f,0f};
    Float[] backward = new Float[]{-1f,0f};
}

class Movement {
    Movement(String mT, Float A) {
        this.moveType = mT;
        this.amount = A;
    }
    public String moveType;
    public Float amount;
}