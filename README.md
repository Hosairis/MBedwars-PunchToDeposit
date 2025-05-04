# 🥊 PunchToDeposit (PTD)
***
Lightweight, HighPerformance and Easy to use recreation of Hypixels PunchToDeposit feature for bedwars servers.
***

## 🔹 How It Works

 - **Left-Click a Container** – Moves the item from your main hand directly into the chest or container.
 - **Sneak + Left-Click a Container** – Transfers all items of the same type from your inventory into the container.

## 🚀 Commands & Permissions
### 📜 Commands
 - **/ptd reload** – Reloads configuration files. (Requires OP or proper permissions)
### 🔑 Permissions
 - **ptd.events.deposit** – Granted to everyone by default, Enables players to interact with containers using PunchToDeposit.
 - **ptd.events.notifyupdate** – Notifies players with this permission about plugin updates.
 - **ptd.commands.reload** – Allows access to the /ptd reload command.

## 🔧 Configuration
Modify the plugin settings via config.yml:

```yaml
Settings:
  Sounds:
    # Sound to play when a player deposits items to a team chest
    TeamChest: "BLOCK_CHEST_CLOSE"
    # Sound to play when a player deposits items to a private chest
    PrivateChest: "BLOCK_CHEST_CLOSE"

  Double-Click:
    # Enables double-clicking to deposit items
    Enabled: false
    # Time in ticks in which the plugin will wait for the second click (20 ticks = 1 second)
    Wait-Time: 10

  Holograms:
    # Enable holograms
    Enabled: true
    Remove-After-Deposit:
      # Removes the hologram for the player after depositing items
      Enabled: true
      # Removes both the private and the team chest hologram
      Remove-Both: true
    # Hologram text
    Text:
      - "&7PUNCH TO"
      - "&7DEPOSIT"
    # Radius that the plugin will try to locate private/team chests
    Search:
      X: 8
      Y: 4
      Z: 8
    # Hologram offset
    Offsets:
      X: 0.5
      Y: 1.0
      Z: 0.5

  # Items that won't be deposited when left-clicking a container
  BlackListed-Items:
   - "WOOD_SWORD"
   - "STONE_SWORD"
   - "IRON_SWORD"
   - "DIAMOND_SWORD"
   - "BOW"
   - "WOOD_PICKAXE"
   - "WOOD_AXE"
   - "SHEARS"

Update-Check:
  # Checks for plugin updates
  Enabled: true

Debug-Mode:
  # Enables debug mode
  Enabled: false

# DO NOT TOUCH!
Config-Version: 4
```

Modify the plugin messages via messages.yml:
```yaml
Prefix: "&7[&bPTD&7]&r"

TeamChest-Transfer: "%prefix &a✔ &7Deposited &fx%amount %item &7into &bTeam Chest&7!"
PrivateChest-Transfer: "%prefix &a✔ &7Deposited &fx%amount %item &7into &dEnder Chest&7!"
Item-BlackListed: "%prefix &c✖ &7That item is &cBlacklisted &7from being deposited"
Container-BlackListed: "%prefix &c✖ &7That container is &cBlacklisted &7from accepting items as deposits"
Reload-Success: "%prefix &a✔ &7Reload &aSuccessful"
Reload-Failed: "%prefix &c✖ &7Reload &cFailed"
Update-Found: "%prefix &c⚠ &7New update is available. Please &aUpdate &7to the latest version."
Insufficient-Permissions: "%prefix &c✖ &7You lack the required permission: &c%permission"

# DO NOT TOUCH!
Config-Version: 2
```