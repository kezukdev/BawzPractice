package kezuk.practice.editor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.inventory.PlayerInventory;

import kezuk.practice.Practice;
import kezuk.practice.utils.InventorySerialization;

//yml save
//uuid.<ladder>.<id 1-5>

public class Editor {

    Map<String, List<PlayerInventory>> kits;
    private UUID uuid;

    public Editor(UUID uuid) {
        registerKits();
    }

    private void registerKits() {

    }

    public UUID getUuid() {
        return uuid;
    }

    public void load() {

    }

    public void save() {
        for(String key : kits.keySet()) {
            if(!kits.get(key).isEmpty()) {
                for(int i = 0; i < kits.get(key).size(); i++) {
                    PlayerInventory kit = kits.get(key).get(i);
                    String[] kit_data = InventorySerialization.playerInventoryToBase64(kit);
                    Practice.getInstance().kitConfig.set(uuid+"."+key+"."+i, kit_data);
                }
            }
        }
    }


}