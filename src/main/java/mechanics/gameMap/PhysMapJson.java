package mechanics.gamemap;

import com.sun.javafx.geom.Vec2d;
import org.jetbrains.annotations.Contract;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import resource.Resource;
import utils.MapReader;

/**
 * Created by Андрей on 27.10.2015.
 */
public class PhysMapJson implements PhysMap, Resource {
    private int mapWidth;
    private int mapHeight;
    private int tileWidth;
    private int tileHeight;
    private boolean[][] passabilityLayer;
    private String objectLayer = "";

    public PhysMapJson(String filename) throws NumberFormatException {
        JSONObject map = MapReader.readMap(filename);
        assert map != null;

        mapWidth = Integer.valueOf(map.get("width").toString());
        mapHeight = Integer.valueOf(map.get("height").toString());
        tileWidth = Integer.valueOf(map.get("tilewidth").toString());
        tileHeight = Integer.valueOf(map.get("tileheight").toString());

        passabilityLayer = new boolean[mapWidth][mapHeight];

        int impassableGid = getGidIsNotPassability((JSONArray)map.get("tilesets"));

        getLayers((JSONArray)map.get("layers"), impassableGid);
    }

    private void getLayers(JSONArray layers, int impassableGid){
        for(Object layer : layers){
            String layerName = ((JSONObject)layer).get("name").toString();
            if(layerName.equals("Objects")){
                objectLayer = ((JSONObject)layer).get("objects").toString();
                continue;
            }
            JSONArray layerData = (JSONArray)((JSONObject) layer).get("data");
            int x = 0;
            int y = 0;
            for(Object gid : layerData){
                switch (layerName) {
                    case "Passability":
                        passabilityLayer[x][y] = (!(((Long) gid).intValue() == 0 || ((Long) gid).intValue() == impassableGid));
                        break;
                    default: break;
                }
                ++x;
                if(x == mapWidth){
                    x = 0;
                    ++y;
                }
            }
        }
    }

    private int getGidIsNotPassability(JSONArray tilesets) throws NumberFormatException{
        int isNotPassability = 0;
        for(Object tileset : tilesets){
            if(!((JSONObject) tileset).get("name").toString().equals("passability")){
                continue;
            }

            int tilesetWidth = Integer.valueOf(((JSONObject) tileset).get("imagewidth").toString());
            int tilesetHeight = Integer.valueOf(((JSONObject) tileset).get("imageheight").toString());

            int amount = tilesetWidth/tileWidth * tilesetHeight/tileHeight;
            JSONObject tileproperties = (JSONObject) ((JSONObject) tileset).get("tileproperties");
            for(int i =0; i<amount; ++i){
                String isPassabilityString = ((JSONObject)tileproperties.get(String.valueOf(i))).get("isPassability").toString();

                if(isPassabilityString.equals("false")){
                    isNotPassability = Integer.valueOf(((JSONObject) tileset).get("firstgid").toString());
                    isNotPassability += i;
                }

            }
        }

        return  isNotPassability;
    }

    @SuppressWarnings("all")
    @Contract(pure = true)
    private boolean isPositionCorrect(int j, int i) {
        return i >= 0 && i < mapWidth && j >= 0 && j < mapHeight;
    }

    @Override
    public boolean isPassability(Vec2d cell){
        boolean result = false;
        if(isPositionCorrect((int)cell.x, (int)cell.y) ) {
            result = passabilityLayer[(int) cell.x][(int) cell.y];
        }
        return result;
    }

    @Override
    public Vec2d getSize(){
        return new Vec2d(mapWidth, mapHeight);
    }

    @Override
    public String getObjectLayer() {
        return objectLayer;
    }

}
