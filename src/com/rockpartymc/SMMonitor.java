/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*TODO:  
login/logout listener, 
commands: reload, pause; 
plugin.yml, 
*/

package com.rockpartymc;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Ben
 */
public class SMMonitor extends JavaPlugin {
//    FileConfiguration config = getConfig();
    private static SMMonitor pluginReference;
    public static int interval = 600;
    private static File logPath;
    static int schedId;
 
    @Override
    public void onEnable() {
        //register commands
        this.getCommand("smmonitor").setExecutor(new Reload());
        //Create and set config values
        pluginReference = this;
        logPath = this.getDataFolder();
        getConfig().addDefault("debug-mode", false);
        getConfig().addDefault("check-ram", true);
        getConfig().addDefault("check-CPU", true);
        getConfig().addDefault("check-tps", true);
        getConfig().addDefault("list-players", true);
        getConfig().addDefault("logfile-name", "log");
        getConfig().addDefault("custom-log-path", false);
        getConfig().addDefault("custom-path-location", "plugins\\SMMonitor");
        getConfig().addDefault("log-interval", 600);
        getConfig().addDefault("debug-mode", false);
        getConfig().options().copyDefaults(true);
        saveConfig();

        //set the log interval to the amount specified in the config.
        interval = getConfig().getInt("log-interval");
        
        //check for custom path for log file
        if (getConfig().getBoolean("custom-log-path")){
            String logPathString = getConfig().getString("custom-path-location");
            System.out.println("[SMMonitor] - Custom Path set to " + logPathString);
            logPath = new File(logPathString);
            if (!logPath.isDirectory()){
                System.out.println("[SMMonitor] - Path not found.  Attempting to Create");
                if (!logPath.mkdirs()){
                    System.out.println("[SMMonitor] - Unable to create directory.  Using Default path.");
                    logPath = this.getDataFolder();
                }
                
            }
        }
        startScheduler()
    }
    
    @Override
    public void onDisable() {
    }    
   
    //returncurrent plugin reference    
    public static SMMonitor getPlugin() {
        
        return pluginReference;
    }
    
    //return the path to the log file.
    public static File getLogPath(){
        return logPath;
    }

        //sets the path for the log file to the one specified in config.yml
        public static void setCustomPath(){
        if (SMMonitor.getPlugin().getConfig().getBoolean("custom-log-path")){
            String logPathString = SMMonitor.getPlugin().getConfig().getString("custom-path-location");
            System.out.println("[SMMonitor] - Custom Path set to " + logPathString);
            logPath = new File(logPathString);
            if (!logPath.isDirectory()){
                System.out.println("[SMMonitor] - Path not found.  Attempting to Create");
                if (!logPath.mkdirs()){
                    System.out.println("[SMMonitor] - Unable to create directory.  Using Default path.");
                    logPath = SMMonitor.getPlugin().getDataFolder();
                }
                
            }
        }
    }
        //Start the scheduled task
        public static void startScheduler(){
            //schedule the task
     //      Bukkit.getServer().getScheduler().cancelTask(schedId);
           BukkitScheduler scheduler = SMMonitor.getPlugin().getServer().getScheduler();
           SMMonitor.schedId = scheduler.scheduleSyncRepeatingTask(SMMonitor.getPlugin(), new Runnable() {

           //gets current time in ms 
               @Override
               public void run() {
                   MainThread.writeToFile();

               }
               
           }, 0L, interval);
   //        Bukkit.getServer().getScheduler().cancelTask(schedId);
        }
}


