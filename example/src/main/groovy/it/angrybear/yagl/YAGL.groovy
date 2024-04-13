package it.angrybear.yagl

import groovy.transform.CompileDynamic
import org.bukkit.plugin.java.JavaPlugin

@CompileDynamic
class YAGL extends JavaPlugin {

    @Override
    void onEnable() {
        def str = "Hello world"
        getLogger().info(str)
    }

}
