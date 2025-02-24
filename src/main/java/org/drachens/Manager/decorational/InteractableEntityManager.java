package org.drachens.Manager.decorational;

import org.drachens.dataClasses.other.Clientside;
import org.drachens.player_types.CPlayer;

import java.util.HashMap;

public class InteractableEntityManager {
    private final HashMap<Integer, Clientside.InteractionEntity> interactionEntityHashMap = new HashMap<>();

    public void register(Clientside.InteractionEntity entity){
        interactionEntityHashMap.put(entity.getEntityId(),entity);
    }

    public void unRegister(Clientside.InteractionEntity entity){
        interactionEntityHashMap.remove(entity.getEntityId());
    }

    public void onInteract(int id, CPlayer p){
        if (interactionEntityHashMap.containsKey(id)){
            interactionEntityHashMap.get(id).interact(p);
        }
    }
}
