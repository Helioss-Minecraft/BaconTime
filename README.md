# BaconTime
- A very simple playtime tracker that allows you to also import data from ActiveTime.
- Made for Sponge API 7.2.0
- Requires Nucleus

# 1.Commands and permissions
## 1.1 Commands 
- /playtime check <username> (Allows you to check your current afk or active time. Or specify a user to check)
- /playtime leaderboard <afk/active> (Shows you the leaderboard of top active or afk players)
- /playtime reload (Reload the plugin)
- /playtime import <plugin-name> (Import data from another plugin into this one. Currently only activetime is supported)
- /playtime set <afk/active> <amount> <user> (Set someones afk or active time. Amount is in seconds)
## 1.2 Permissions 
- bacontime.command.base (Base command)
- bacontime.command.leaderboard (Leaderboard command) 
- bacontime.command.check (Check command)
- bacontime.admin.reload (Reload command)
- bacontime.admin.import (Import command)
- bacontime.admin.set (Set command)
- bacontime.milestones (Required for users to get milestones. Example bacontime.milestones.banana would allow the user to reach the banana milestone
# 2.Config
    # The aliases the plugin uses for commands. The first value is always displayed when running the base command!
    # Requires server restart to change!
     Aliases=[
      playtime,
      bacontime,
      bt,
      ptime
    ]
    #Intervals {
       # The interval in seconds between milestone checks, will be disabled if there are no milestones.
       milestone=180
       # The interval in seconds between saving the playtime data.
       save=120
       }
    #milestones {
    "example_milestone" {
        commands=[
            "give <player> minecraft:apple 10",
            "msg <player> Apple time!"
        ]
        repeatable=true
        requiredTime=3600
    }

# 3.Support
 - For support join my discord server and tag me in the #general channel or pm me (kristi71111 | BacoNetworks#0001)
