package kezuk.bawz.editor;

import kezuk.bawz.Practice;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// yml save
// uuid.<ladder>.<id 1-5>

public class EditorManager {

    Map<String, List<PlayerInventory>> kits;
    private UUID uuid;

    public EditorManager(UUID uuid)
    {
        registerKits();
    }

    private void registerKits()
    {

    }

    public UUID getUuid() {
        return uuid;
    }

    public void load()
    {

    }

    public void save()
    {
        for(String key : kits.keySet())
        {
            if(!kits.get(key).isEmpty())
            {
                for(int i = 0; i < kits.get(key).size(); i++)
                {
                    PlayerInventory kit = kits.get(key).get(i);
                    String[] kit_data = BukkitSerialization.playerInventoryToBase64(kit);
                    Practice.getInstance().kitConfig.set(uuid+"."+key+"."+i, kit_data);
                }
            }
        }
    }


}
