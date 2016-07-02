# Plugin Description
| Key         | Value                                 |
| :---------- | :------------------------------------ |
| Name        | ShowInfo                              |
| Author      | Debe                                  |
| Version     | 1.0.0                                 |
| Description | Send information to players in server |


--------------


# Maven Info
| Key        | Value             |
| :--------- | :---------------- |
| GroupId    | debe.nukkitplugin |
| ArtifactId | debe.showinfo     |
| Version    | 1.0.0-SNAPSHOT    |



# Permission node
| Permisson                         | Default |
| :-------------------------------- | :------ |
| showinfo.command.showinfo         | TRUE    |
| showinfo.command.showinfo.on      | TRUE    |
| showinfo.command.showinfo.off     | TRUE    |
| showinfo.command.showinfo.enable  | OP      |
| showinfo.command.showinfo.disable | OP      |
| showinfo.command.showinfo.push    | OP      |
| showinfo.command.showinfo.period  | OP      |
| showinfo.command.showinfo.reload  | OP      |
| showinfo.command.showinfo.save    | OP      |
| showinfo.command.showinfo.reset   | FALSE   |

# Replaceable text
- at send information to player

| Text             | Description                            |
| :--------------- | :------------------------------------- |
| Players          | Count of players count in server       |
| MaxPlayers       | Max player count                       |
| Player           | Player's name                          |
| iPlayer          | Player's name to lowercase             |
| DisplayName      |  Player's display name                 |
| Nametag          |  Player's  nametag                     |
| UUID             | Player's UUID                          |
| Health           | Player's health                        |
| MaxHealth        | Player's max health                    |
| HealthPercentage | Percentage of player's health          |
| X                | Player's x location                    |
| Y                | Player's y location                    |
| Z                | Player's z location                    |
| Yaw              | Player's head direction (Left & Right) |
| Pitch            | Player's head direction (Up & Down)    |
| World            | Player world name                      |
| ItemID           | Item id in player's hand               |
| ItemDamage       | Item damage in player's hand           |
| ItemName         | Item name in player's hand             |
| Level            | Player's level                         |
| Exp              | Player's experience                    |
| TotalExp         | Player's total experience              |
| ExpPercentage    | Percentage of player's experience      |
| SkinModel        | Name of player's skin model            |