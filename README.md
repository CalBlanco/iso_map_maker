# Isometric Map Maker

I am working on an isometric 2d engine as well as a map maker. I would like to be able to iterate quickly on games that I can feel more responsible for desiging (sorry unity)

## How to set up 

Follow this tutorial https://libgdx.com/wiki/start/setup to ensure you have the correct dependencies (I beleive it's just some JDK version as I was able to download this repo and go on my mac with no need for the libgdx manager)

To run on dekstop type `./gradlew desktop:run` (I believe for other platforms you should be able to just specify before run howver this project might only be setup for desktop and html don't fully remember)

## Project Structure
The core game code can be found in the directory `core/src/com/isomapmaker/game/`.

The Main driver of the program is `IsoMapMaker.java` which is called by `desktop/src/com/isomapmaker/game/DesktopLauncher.java`.

`DesktopLauncher.java` controls window size and how the build targets the platform.

`IsoMapMaker.java` is responsible for core game code.


Within the `core/src/` files you will find additional code for the map maker.

### Controls
`controls` are responsible for user input, and output. `CameraControler` pans the camera around the screen and name wise makes the most sense. 

The `AssetPlacer` is responsible for drawing/removing tiles from the actual game map. The `AssetController` is actually just the UI that helps users determine what assets they are using / want to use. 

### Map
The `map` directory contains two folders that handle tile rendering on the screen and tile information in the game.

#### Tiles
These are Graphical components `Floor, Object, Wall` which all implement the `SimpleTile` interface. This just gives a texture region a "game" definition as a Floor, Wall, or Object. It also contains some flags for later use in a game about the tile (i.e water is non-traversable, etc..)

`SimpleTiles` all implement a `getTexture()` function that returns the texture region for the asset for rendering
`SimpleTiles` all implement a `getName()` function which tells us information about what file / region this asset came from

These simple tiles are then collected into a `Tile` 
The `Tile` class contains a single `Floor` called `floor`, a `Dictionary` called `walls` containing keys `["top", "bottom", "left", "right"]` for wall placements, and a `Vector<Object>` called `objects` for containing objects later.

#### TileMaps
`TileLoader` is responsible for generating the `SimpleTiles` from assets. It parses an xml file that points to assets and provides asset information located in `assets/assets.xml`. 

`TileMap` represents a `size`x`size` array of `Tile` and allows for CRUD operations on tiles in bounds. It also provides some functions for saving and loading tiles from strings

`TileMapManager` is the class that will handle layers. I had this working in an earlier build but have broken it since so it is not fully functional right now, but it should be soon.

### Util

## Development Videos

**Current** 

Command Pattern and Commander (Singleton pattern controlling command stack) implemented



https://github.com/Jimdangle/iso_map_maker/assets/72684566/13ee0a6e-f2e7-4677-bd50-f04d2597ae19


**Chronological**


One of the second or third builds of this project. Able to place tiles down in a grid, unable to place walls ontop of tiles. A lot of the earlier commits were adding core compenents like assets for ui and the game.

https://github.com/Jimdangle/iso_map_maker/assets/72684566/9295dd02-5816-4b4e-abac-5021c81fb0e9



Building walls ontop of floor tiles. Multiple layers availble. Realizing that I will want to be able to place 4 walls on a tile

https://github.com/Jimdangle/iso_map_maker/assets/72684566/498a1783-6c9a-49a7-8bb1-6fb91755c212


4 walls can be placed, however it is extremely unintuitive to do so at this point

https://github.com/Jimdangle/iso_map_maker/assets/72684566/4a64c261-4270-44f0-861b-666c45009cdc


Better ui, visually able to determine what the nearby options will be

https://github.com/Jimdangle/iso_map_maker/assets/72684566/fee75d2b-c3ff-4351-9cf2-ab8e3127f867

Placing walls now intuitive. 

https://github.com/Jimdangle/iso_map_maker/assets/72684566/1a497255-85bf-4f6f-a0a9-721ce75d4574

Common Painting tools via Bresenham and Midpoint algorithms

https://github.com/Jimdangle/iso_map_maker/assets/72684566/d4bf88a1-09c4-4f5b-99ab-f28288c49654



## License 
This project is Licensed under the CC-BY-NC Licesne which can be found in LICENSE.txt


