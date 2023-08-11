package mareza;

import javafx.scene.paint.Color;
import java.util.*;

public abstract class GameLogic {
    private static Boolean running;
    public static ArrayList<PaintNode> factory= new ArrayList<PaintNode>();
    public static ArrayList<BotPlayer> botPlayers= new ArrayList<>();
    public static ArrayList<Player> players=new ArrayList<>();
    ArrayList<Integer> rows = new ArrayList<>();
    ArrayList<Integer> columns = new ArrayList<>();
    ArrayList<PaintNode> vertex=new ArrayList<>();
    private int maxR;
    private int maxC;
    private int minR;
    private int minC;
    boolean lastM;
    boolean recentM;
    int gridSize;
    int cellSize;
    GameLogic(int gridSize,int cellSize){
        this.gridSize=gridSize;
        this.cellSize=cellSize;
        setRunning(true);
    }
    void defult(Player p,Color color,int r,int c){
        for (int i = r ; i < r+4 ; i++) {
            for (int j = c-3 ; j < c+1; j++) {
                int index=nodeExist(i,j);
                if(index>0){
                    factory.get(index).setOwner(p);
                    factory.get(index).setColor(color);
                    p.territory.add(factory.get(index));
                }
            }
        }
    }
    public int nodeExist(int r,int c){
        for(int i=0 ; i< factory.size() ; i++){
            if((factory.get(i).getColumn() == c) && (factory.get(i).getRow()==r)){
                return i;
            }
        }
        return -1;
    }
    public void deduplication(ArrayList<PaintNode> arr){
        ArrayList<PaintNode> unique = new ArrayList<PaintNode>(arr);
        for(PaintNode p: arr){
            if( !unique.contains(p) ){
                unique.add(p);
            }
        }
        arr.clear();
        arr.addAll(unique);
        unique.clear();
    }
    public void setRunning(Boolean running) {
        this.running = running;
        System.out.println(this.running);
    }
    public Boolean getRunning() {
        return running;
    }
    public abstract void kill();
    public abstract void die();
    private boolean setBoundaries(ArrayList<PaintNode> line){
        maxR=0;
        maxC=0;
        minR = rows.size();
        minC = columns.size();

        for(PaintNode p : line){
            if(p.getRow()>maxR)
                maxR=p.getRow();

            if(p.getRow()<minR)
                minR=p.getRow();

            if(p.getColumn()>maxC)
                maxC=p.getColumn();

            if(p.getColumn()<minC)
                minC=p.getColumn();
        }

        System.out.println("****************");
        System.out.println("maxR"+maxR+"\tminR"+minR+"\nmaxC"+maxC+"\tminC"+minC);
        System.out.println("****************");

        if (minR>maxR || minC>maxC)
            return false;
        else
            return true;
    }
    private void floodFill(int index, Color newClr, Color tailClr,Player player){
        Color currentColor=factory.get(index).getColor();
        if(currentColor == newClr)
            return;
        Queue<PaintNode> queue = new LinkedList<>();
        queue.offer(factory.get(index));
        while (!queue.isEmpty()){
            PaintNode temp= queue.poll();
            int i=temp.getRow();
            int j=temp.getColumn();
            if(i<minR || i>maxR || j<minC || j>maxC )
                continue;
            else if( temp.getColor()==newClr || temp.getColor()==tailClr)
                continue;
            else {
                temp.setColor(newClr);
                player.territory.add(temp);
                index=nodeExist(i+1,j);
                queue.offer(factory.get(index));
                index=nodeExist(i-1,j);
                queue.offer(factory.get(index));
                index=nodeExist(i,j+1);
                queue.offer(factory.get(index));
                index=nodeExist(i,j-1);
                queue.offer(factory.get(index));
            }
        }
    }
    public boolean isInside(int r, int c,Player player){
        boolean result;
        int count=0;
        int maxCol=0;
        for (Integer col: columns){
            if (col>maxCol)
                maxCol=col;
        }
        for (int j=c ; j<maxCol ; j++){
            int index= nodeExist(r,j);
            if(index>0){
                PaintNode n=factory.get(index);
                if(player.tail.contains(n))
                    count++;
                if(count==0 && n.getColor()==player.getColor())
                    count++;
            }
        }

        if(count%2==0)
            result=false;
        else
            result=true;

        return result;
    }
    public void conquest(Player player,boolean right){
        boolean b=setBoundaries(player.tail);
        /*
        //    System.out.println(b);
    //    int count=-1;

        if(b){
    //        count++;
            Random rand = new Random();
            int r;
            int c;
            int index;
            do{
                if(minC==maxC || minR==maxR){
                    // || count>50
                    r=minR;
                    c=minC;
                    break;
                }
                r = rand.nextInt(maxR-minR);
                r +=minR;
                c = rand.nextInt(maxC-minC);
                c +=minC;
                index=nodeExist(r,c);
                 //       System.out.println(factory.get(index).toString());
                //    System.out.println("~inside :"+!isInside(r,c,player)+"\t tail :"+player.tail.contains(factory.get(index)));
                //    System.out.println("result:"+( !isInside(r,c,player) || player.tail.contains(factory.get(index) ) ) );
                //isInside(r,c,player)==false || player.tail.contains(factory.get(index))==true
            }while (!isInside(r,c,player)|| player.tail.contains(factory.get(index)));
            index=nodeExist(r,c);
            System.out.println("finished first part");

         */
            int index=0;
            while (index != -1){
                index=findBase(player,right);
                if(index>=0)
                    floodFill(index,player.getColor(),player.getTailColor(),player);
            }
            for (PaintNode p:player.tail)
                p.setColor(player.getColor());
            player.tail.clear();
            vertex.clear();
    }
    public void paintArea(Player player,boolean right){
        ArrayList<PaintNode> inside = new ArrayList<>();
        boolean b=setBoundaries(player.tail);
        for (int i=minR ; i<=maxR ; i++)
            for (int j=minC ; j<=maxC ; j++){
                int index=nodeExist(i,j);
                System.out.println(factory.get(index));
                if( factory.get(index).getColor()==player.getTailColor() || factory.get(index).getColor()==player.getColor())
               //     System.out.println("tail or colored");
                    continue;
                else if(rayCasting(i,j,right))
                    inside.add(factory.get(index));
            }
        for (PaintNode p: inside)
            p.setColor(player.getColor());
        vertex.clear();
        for (PaintNode p:player.tail)
            p.setColor(player.getColor());
        player.tail.clear();
    }
    @Override
    public String toString() {
        String str = "factory :";
        for(PaintNode p: factory){
            str += p.toString() ;
        }
        return " horizontalMove="   + "\t verticalMove="
                +   "\n\n"+str;
    }
    public void addVertex(int r,int c){
        int index =nodeExist(r,c);
        if (recentM != lastM)
            vertex.add(factory.get(index));
    }
    public boolean rayCasting(int y,int x,boolean right){
        int count=0;
        for (int i=0 ; i<vertex.size();i++){
            PaintNode v1;
            PaintNode v2;
            if(!right && i== vertex.size()-1)
                break;
            else if(i== vertex.size()-1){
                // right==true
                v1=vertex.get(0);
                v2=vertex.get(i);
            }else {
                // i<vertex.size()-1
                v1=vertex.get(i);
                v2=vertex.get(i+1);
            }
                if((y<v1.getRow()) != (y< v2.getRow()) &&
                        x<( v1.getColumn() +((y- v1.getRow())/(v2.getRow())-v1.getRow()) * ((v2.getColumn())- v1.getColumn()) ))
                    count++;
            }
     //   }
        return count%2==1;
    }
    private int findBase(Player player,boolean right){
        int index;
        for (int i=minR ; i<=maxR ; i++)
            for (int j=minC ; j<=maxC ; j++){
                index=nodeExist(i,j);

                if( factory.get(index).getColor()==player.getTailColor() || factory.get(index).getColor()==player.getColor())
                    continue;
                else if(rayCasting(i,j,right)){
                    System.out.println("Base:"+factory.get(index));
                    return index;
                }
            }
        return -1;
    }

}
