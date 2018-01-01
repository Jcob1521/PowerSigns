# PowerSigns [![Minecraft 1.8-1.12.2](https://img.shields.io/badge/Minecraft-1.8--1.12.2-red.svg)](#) [![JDK 1.8](https://img.shields.io/badge/JDK-1.8-blue.svg)](#) [![Spigot project page](https://img.shields.io/badge/Spigot-Project%20Page-yellow.svg)](https://www.spigotmc.org/resources/powersigns.51501/)
![PowerSigns demonstration](https://i.imgur.com/BUuEPDe.png)<br>
Minecraft Bukkit/Spigot Plugin allowing players to activate redstone mechanisms in return of money. Extremly useful for casinos, mob farms, VIP areas, firework shows and much more...
Requires [Vault](https://www.spigotmc.org/resources/vault.41918/).

## Download
Download on the spigot resource page: https://www.spigotmc.org/resources/powersigns.51501/

## Making a new PowerSign
This is very straight forward. Place a sign and follow the format below:<br><br>
![Format](https://image.prntscr.com/image/QJZFxVHXTB6bksC2JoTYWw.png)

## Permissions
- ``powersigns.sign.use``: Player can click on a PowerSign to activate (Enabled by default) 
- ``powersigns.sign.create.self``: Player can create a PowerSign for themselves (Enabled by default)
- ``powersigns.sign.create.other``: Player can create a PowerSign for other players

## Bug reports, suggestions, ideas
Please create a new ticket [here](https://github.com/hallopiu/PowerSigns/issues)

## Languages
PowerSigns supports dynamic locale files allowing you to easily switch languages. All locales can be found [here](https://github.com/hallopiu/PowerSigns/tree/master/src/locale). To switch to a different language, edit the ``lang`` value in the [config file](https://github.com/hallopiu/PowerSigns/blob/master/src/config.yml).

If you can't find a translation for the language you want to use, feel free to submit a translation!

## Submitting a new translation
1. Create a new fork of this project and make a new file in ``locale/`` 
2. Copy an existing locale file so you have a template
3. Translate!
Make sure to only translate the part inside quotation marks ``"``, otherwise things may break.
4. Finally, create a pull request with your changes.
5. Profit! Wait for me to see the pull request.

## API
The plugin comes with a ``PowerSignUseEvent`` which can be registered and used like a normal listener.
