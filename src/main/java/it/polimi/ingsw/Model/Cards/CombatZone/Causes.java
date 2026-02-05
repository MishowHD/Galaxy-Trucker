package it.polimi.ingsw.Model.Cards.CombatZone;

import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "causeType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Causes_Crew.class, name = "Causes_Crew"),
        @JsonSubTypes.Type(value = Causes_Fire_power.class, name = "Causes_Fire_power"),
        @JsonSubTypes.Type(value = Causes_Motion_power.class, name = "Causes_Motion_power"),

})

public abstract class Causes {
}
