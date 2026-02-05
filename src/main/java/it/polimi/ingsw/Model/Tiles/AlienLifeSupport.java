package it.polimi.ingsw.Model.Tiles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.util.List;

public class AlienLifeSupport extends SpaceShipTile {
    private final AlienColor aliencolor;

    @JsonCreator
    public AlienLifeSupport(
            @JsonProperty("id") int id,
            @JsonProperty("aliencolor") AlienColor aliencolor,
            @JsonProperty("connectorList") List<Type_side_connector> connectorList,
            @JsonProperty("types") SSTTypes types
    ) {
        super(id, connectorList, types);
        this.aliencolor = aliencolor;
    }

    @Override
    public AlienColor getColor() {
        return aliencolor;
    }

}
