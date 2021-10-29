USEME for CS3500, Assignment 6
Authors: Gavin White, Hyoboem Kim

/***************************************************************************************************/

HOW TO USE JAR FILE:

/***************************************************************************************************/

The jar file must be run with 1-2 arguments. The possible arguments are as follows:

-text (runs the application through the command line, allowing the user to type commands interactively)

-script _filename_ (runs the application for a given script)

-interactive (runs the application, opening a GUI)

/***************************************************************************************************/

HOW TO USE THE GUI:

/***************************************************************************************************/

The GUI displays the topmost visible layer as its image. The interface provides checkboxes to toggle

layers visibility on and off. If a box is checked, the layer is visible, if not, it is invisible.

There are also radio buttons to select the current layer. The selected radio button corresponds to

what layer is currently selected. This is the layer that filters and modifications are applied to.

Note that when a new layer is added, it automatically becomes the selected layer. The interface

provides some buttons to apply filters, and create a checkerboard image for convenience, below the

main image display.

/---------------------------------------------------------------------/
The GUI has a menu bar at the top that exposes all available features. 

-File menu:

The file menu allows for exporting, which saved an image to a file, saving, which saves progress of an

edit (all layers) for later use, loading, which loads a save file into the application to be used,

and scripting, where a script file can be selected to be run in the application.

-Add menu:

The add menu allows for layers to be added, either by creating them programmatically, or by importing

an existing image file.

-Layer menu:

The layer menu allows for layers to be toggled, selected, and removed by their indices. The image can

also be downscaled through this menu.

-Filter menu:

The filter menu allows for filters to be appliedd to the selected layer. These include blur, sharpen,

grayscale, sepia, and mosaic.
/---------------------------------------------------------------------/

/***************************************************************************************************/

HOW TO USE SCRIPT COMMANDS:

/***************************************************************************************************/

Scripts must be created with the listed supported commands. Each command and all of its arguments

must go on a new line. Any text following a '#' symbol is considered a comment and is thus ignored

by the interpreter. All commands are case-sensitive and must be completely lowercase. Note that in

the list of commands underscores indicate arguments (eg. _filename_ indicates that you would give

a filename at this location).

/***************************************************************************************************/

import _filename_ #imports a file with the given filename, adding it as a new layer on top

Example: import test1.jpg

/***************************************************************************************************/

export _filename_ _format_ #exports image to a file with the given filename as the given format.

the image exported is the topmost visible layer.

Example: export exported.png png

/***************************************************************************************************/

select _layer_ #selects a layer at the given integer index (layers are indexed starting at 1)

_layer_: valid layer index, integer

Example: select 1

/***************************************************************************************************/

remove _layer_ #removes a layer at the given integer index (layers are indexed starting at 1)

_layer_: valid layer index, integer

Example: remove 2

/***************************************************************************************************/

toggle _layer_ #toggles the visibility of the layer at the given integer index

_layer_: valid layer index, integer

Example: toggle 1

/***************************************************************************************************/

blur #blurs the selected layer

Example: blur

/***************************************************************************************************/

sharpen #sharpens the selected layer

Example: sharpen

/***************************************************************************************************/

sepia #applies the sepia filter to the selected layer

Example: sepia

/***************************************************************************************************/

grayscale #applies the grayscale filter to the selected layer

Example: grayscale

/***************************************************************************************************/

load _filename_ #loads a saved layered image at the given filename (which should be a folder)

Example: load SavedLayers

/***************************************************************************************************/

save _filename_ _format_ #saves a layered image to a file named the given filename with each layer 

saved as the given format

Example: save SavedLayers png

/***************************************************************************************************/

create _type_ _args_ #creates an image programmatically, adding it as a new layer on top

_type_: the type of image to create

_args_: the arguments that the specified type of image takes

(See below for specific examples of how create can be used)

/***************************************************************************************************/

create checkerboard _numberOfTiles_ _tileSize_ (_red_ _green_ _blue_) (_red_ _green_ _blue_)

_red_, _green_, _blue_: integers between [0, 255] inclusive.

_numberOfTiles_: non-negative integer

_tileSize_: non-negative integer

Example: create checkerboard 10 10 (0 0 0) (255 255 255)

/***************************************************************************************************/