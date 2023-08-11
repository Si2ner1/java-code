package mareza;

import javafx.scene.paint.Color;
import java.util.ArrayList;
public class ColorCollection {
    private static ColorCollection instance=null;
    private ArrayList<Color> territoryColor=new ArrayList<>();
    private ArrayList<Color> tailColor=new ArrayList<>();
    private ColorCollection(){

        territoryColor.add(Color.BLUEVIOLET);
        tailColor.add(Color.MEDIUMPURPLE);

        territoryColor.add(Color.DARKBLUE);
        tailColor.add(Color.BLUE);

        territoryColor.add(Color.GREEN);
        tailColor.add(Color.CHARTREUSE);

        territoryColor.add(Color.RED);
        tailColor.add(Color.ORANGE);
    }
    public static synchronized ColorCollection getInstance(){
        if (instance==null){
            instance=new ColorCollection();
        }
        return instance;
    }
    public Color getTailColor(int num){
        return tailColor.get(num);
    }
    public Color getTerritoryColor(int num){
        return territoryColor.get(num);
    }
}
