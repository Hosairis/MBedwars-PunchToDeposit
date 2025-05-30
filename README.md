
# PunchToDeposit
Wasting too much time sorting your inventory in chests during intense battles? This add-on makes it easy for you! Just punch your chest with the item in your hand, and it will be automatically deposited, as seen on popular servers.

## Requirements
- Java 8 or above
- Mbedwars 5.5.3 or above

## Installation
- Drop the addon inside the following folder: **/MBedwars/add-ons**
- Restart your server.
- Done! enjoy.

## Permissions
- **ptd.events.deposit** – Granted to everyone by default, Enables players to interact with containers using PunchToDeposit.
- **ptd.events.notifyupdate** – Notifies players with this permission about plugin updates.
- **ptd.commands.reload** – Allows access to the /ptd reload command.

## Commands
- **/ptd reload** - Reloads configuration and message files. (Requires OP or proper permissions)

## Configuration
All the configurations related to the addon can be found in **/MBedwars/add-ons/PunchToDeposit/config.yml**

## API
Currently there are 2 events available:
- **PreItemDepositEvent** - Triggered before depositing items to the container (Cancellable)
- **PostItemDepositEvent** - Triggered after depositing items to the container (Not Cancellable)

## Support
For support, the best place to start is [Discord](https://discord.gg/vSuKz7dfve). You can also check out our [GitHub](https://github.com/Hosairis/MBedwars-PunchToDeposit) for issues and updates.

