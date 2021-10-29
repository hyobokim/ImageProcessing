README for CS3500, Assignment 7. 
Authors: Gavin White, Hyoboem Kim

/***************************************************************************************************/

About/Overview:

The purpose of this code is to provide an Image Processing application that allows users to load in 
images or sets of images as layers, and modify those images with specific filters and transformations.
The UI allows users to input text-based commands to allow users to navigate across specific layers, 
add layers as desired, and apply filters on currently selected layers. Users can select from a variety 
of preset filters, each represented by either a kernel or linear transformation, and can export either 
the topmost layer or all layers as any widely supported image extension. 


/***************************************************************************************************/

List of Features:

-Importing existing images

	Using the import command followed by the name of the file (as well as its corresponding directories) will successfully import that image

-Exporting layers

	Exports the topmost visible layer as whatever image file extension the user specifies

-Saving file

	Saves the whole file into a folder with the name specified by the user. Includes all layers as whatever file extension user specifies, as well as a properties text file that lists all layers and their visibility level

-Loading file

	Loads a previously saved file into the current users program. Overrides all layers the user previously had and instead loads all layers inside the save file that the user loads. 

-Filters

	User can apply all of the following filters onto the currently selected layer

	-Sharpen

	-Sepia

	-Grayscale

	-Blur

	-Mosaic

-Remove

	User can remove specific layers by specifying which layer number, starting from 1

-Toggle

	Toggle the visibility of a specific layer by specifying which layer number, starting from 1

-Select

	Selects the current layer the user wants to hover. This will be the layer that will be affected whenever the user calls a filter method. 

- Resize

	Resizes all images in the program to whatever width and height the user specifies

- Mosaic

	Applies a mosaic effect onto the current image, which blends all pixels into seeds. User can specify how many seeds are to be used. 

- Load Script

	User can import in a text file that contains all text-based commands that are supported by the original text-based controller. Commands are 

	initiated as usual and the effects appear on the GUI, with all corresponding errors (if any) appearing in a single pop-up window. 

-Quit

	By closing the window (pressing X in the top right corner) the user can quit the program.

/***************************************************************************************************/

How to Run:

Navigate to the hw07/res directory, where the JAR file is stored. Then, run the command 
java -jar hw07.jar _arg0_ _arg1_ to run the JAR file.

The first argument (_arg0_) can be one of three possible inputs: 

	- "-interactive" for the Swing GUI-based application (_arg1_ not necessary)

	- "-text" if the user wants to type in the commands themselves (_arg1_ not necessary)

	- "-script" followed by the file name (_arg1_) of the text file to execute

File names of example scripts:
script1.txt (to run script 1)
script2.txt (to run script 2)

/***************************************************************************************************/

Example Runs:

Run 1 -- filename : script1.txt

Create a black and white checkerboard image as the first layer
Create a red and blue checkerboard image as a new layer
Select the first layer
Blur the selected layer
Sharpen the selected layer
Remove the first layer
Make the selected layer grayscale
Create a black and white checkerboard image as a new layer
Applies the sepia filter to the selected layer
Toggles the visibility of the second layer
Export the topmost visible layer as a png
Remove the second layer
Remove the first layer
Imports an image as a new layer
Imports an image as a new layer
Saves the multi-layered image
Remove the first layer
Remove the first layer
Loads the image in "saved"
Blurs the image
Exports the image as a ppm


Run 2 -- filename : script2.txt

Imports test1.jpg as the first layer
Creates a new checkerboard, 10 tiles on each side with each tile being 36 pixels,
to create a 360x360 image, and adds that layer to the top
Toggles the first layer (test1) to be invisible
Selects the second layer (checkerboard) as the current layer
Applies grayscale filter to the second layer (checkerboard)
Imports test1.jpg again as a new layer
Imports test1.jpg again as a new layer
Selects the third layer as current layer
Applies sepia filter to the third layer 
Toggle 4th layer to be invisible
Exports topmost visible layer as a png, which should be layer 3 since layer 4 is invisible
Removes 4th layer
Applies blur filter to current layer, which is still third layer
Selects the first layer to be current layer
Applies sharpen filter to first layer
Saves all layers and properties in a folder called TestSave, each layer saved as png
Loads TestSave
Selects the third layer to be current layer
Sharpens the third layer
Creates new checkerboard and adds that to the top
Imports test1.jpg as a new layer
Loads TestSave, which should override the current file
Removes the first layer
Toggles the second layer to be invisible
Exports top layer as NewTopmost

/***************************************************************************************************/

Design/Model Changes

hw06 - Our earlier design previously had generics, which allowed the user to input whichever representation 
of Pixel they wanted. We removed this, and now enforced our own representation of Pixel due to excessive 
amounts of flexibility preventing our ImageProcessingCommands from functioning. In order to make our
commands return the correct image type, we would have to further abstract for every possible type of 
Pixel representation, which made our code significantly more complex than required. In practicality, 
we discovered that the user could choose to represent Pixel a different way, and we provided them 
with enough tools to convert from our representation of Pixel to whatever their representation of 
Pixel was, by allowing the user to select individual pixels. For design purposes, we left it up to 
the user to choose if they wanted to convert our PixelImpl to something else. 

As a result, we removed our AbstractImageModel, which contained all ImageModels that specifically 
represented images as List<List<Pixel>>, and changed it to just our SingleImageModel, since we now
represent all of our images with our PixelImpl class. 

We also removed our extension of this abstract class. We used to handle IO such as import and export in that 
class, but we moved those to the controller, which left the class empty, so we removed it. 


/***************************************************************************************************/

Pixel Interface:

The Pixel interface provides operations to query and update the state of a singular pixel within

an image.


PixlImpl Class:

The PixlImpl class provides an implementation of the Pixel interface that stores each of a pixel’s

three channel values as integers. This class implements the functionality required by the Pixel

interface, and serves as a suitable representation of a pixel in an image.

/***************************************************************************************************/

ImageModel Interface:

Model interface for image processing. Contains methods to process/create images either manually or through

an import,and methods to manipulate the image stored using filters. Images stored within an ImageModel

are represented with List<List<Pixel>>, which represent a single pixel within an image. 

/***************************************************************************************************/

SingleImageModel class:

Model class representing an image processing application that handles just one single image. Stores one

image as a List<List<Pixel>> and provides functions that can query the state, such as getWidth() and
 
getHeight(), and also modify the stored image through kernels, linear transformations, or preset filters. 

/***************************************************************************************************/

ImageView Interface:

Interface representing the viewer of the program, and how output will be displayed. 


TextImageView: 

Implements the ImageView interface. TextImageView is a text-based viewer that appends output and messages to whatever Appendable is attached to the object. Takes in a single Appendable to write data to. 


SwingImageView:

Implements the ImageView interface. Uses built-in Java's Swing package to represent the application through a Swing-based GUI. Shows messages through a window and pop-ups that display relevant information, as well as the current layer's image at the center of the screen. 

/***************************************************************************************************/

ImageFilter Interface:

The ImageFilter Interface provides one operation to apply a filter to a given instance of an

ImageModel. This Interface represents a filter that can be applied to an image. The ImageModel interface

provides functionality to apply an ImageFilter to an image, meaning any implementing class can be used

to apply a filter to an image as represented by an ImageModel.


GreyscaleFilter Class:

The GreyscaleFilter Class provides an implementation of the ImageFilter Interface. This Class

represents a filter that turns an image to a greyscale version of itself through a linear

transformation with a specific matrix that is applied to the given model.


SepiaFilter Class:

The SepiaFilter Class provides an implementation of the ImageFilter Interface. This Class

represents a filter that turns an image to a sepia (brownish tinted) version of itself through a

linear transformation with a specific matrix that is applied to the given model.


BlurFilter Class:

The BlurFilter Class provides an implementation of the ImageFilter Interface. This Class

represents a filter that blurs an image, making pixels more similar to the ones around them

through the application of a specific image kernel that is applied to the given model.


SharpenFilter Class:

The SharpenFilter Class provides an implementation of the ImageFilter Interface. This Class

represents a filter that sharpens an image, exacerbating contrast through the application of a

specific image kernel that is applied to the given model.

/***************************************************************************************************/

ImageCreator Interface

Interface for function objects that generate an image programmatically in a certain way

specifically for images that are represented as List<List<Pixel>>. Contains one method that

initiates the creation and returns a List<List<Pixel>>, representative of the image.


CheckerboardImageCreator class:

Implementation of the ImageCreator class. Represents a method of creating images that

generates a checkerboard pattern, using two alternating colors. Constructor for this class takes

in four arguments, the size of each tile, the number of tiles in the checkerboard, and the two

colors to be alternated to create the checkerboard effect.

/***************************************************************************************************/

Channel Enum:

The Channel Enum represents the different channels of color a pixel has. This Enum

enumerates the three channels: red, green, and blue.

/***************************************************************************************************/

ImageUtil Class:

The ImageUtil Class represents a utility to read a PPM file and interpret its data as an image.

Provides an operation to read a PPM file and convert the data to a list of lists of pixels.

/***************************************************************************************************/

LayeredImageModel Interface:

Represents a model that contains layers of images, and allows image processing/filtering commands on each layer within the model. Stores layers as a List<ImageModel>, allowing each layer to be modified in the same way a SingleImageModel can be modified. 

/***************************************************************************************************/

ImageProcessingCommand Interface:

Part of the command design pattern for SimpleImageController. Represents all commands that can be run

from within the controller, and initiates a specific command. Contains one execute command, which

performs the corresponding operation onto the given LayeredImageModel. 

/-----------------/
BlurImage Class:

Implements the ImageProcessingCommand interface. Runs when the user inputs “blur”. Represents a command

that applies the blur filter onto the current layer of the given LayeredImageModel provided to it,

blurring the stored image. 
/-----------------/
GreyscaleImage class:

Implements the ImageProcessingCommand Interface. Runs when the user inputs “grayscale”. Represents a

command that applies the grayscale filter onto the current layer of the given LayeredImageModel provided

to it, turning the stored image into a monochrome version of itself. 
/-----------------/
SepiaImage class:

Implements the ImageProcessingCommand Interface. Runs when the user inputs “sepia”. Represents a 

command that applies the sepiafilter onto the current layer of the given LayeredImageModel provided 

to it, turning the stored image into a sepia-toned version of itself. 
/-----------------/
SharpenImage class:

Implements the ImageProcessingCommand Interface. Runs when the user inputs “sharpen”. Represents a

command that applies the sharpen filter onto the current layer of the given LayeredImageModel provided

to it, sharpening all of the edges of the image stored within the LayeredImageModel’s current layer. 
/-----------------/
CheckerboardImage class:

Implements the ImageProcessingCommand Interface. Runs when the user inputs “checkerboard” after 

“create” was already called. Represents a command that creates a checkerboard image with the given 

parameters, and adds it as the topmost layer of the given LayeredImageModel. User must also input 

the number of tiles, the size of each tile, and the RGB values of each alternating tile, wrapped in 

parentheses. Ex: checkerboard 10 5 (255 0 0) (0 0 255) creates a checkerboard with 10 tiles on each 

side, each tile 5x5, and alternating between RED and BLUE.
/-----------------/
CreateImage class:

Implements the ImageProcessingCommand Interface. Runs when the user inputs “create”. Represents a command 

creates a given image and adds it to the top layer of the provided LayeredImageModel. The user must 

specify after “create” the specific type of image they want to create. 
/-----------------/
RemoveLayer class:

Implements the ImageProcessingCommand Interface. Runs when the user inputs “remove”. Removes the layer 

corresponding to the layerIndex provided to the RemoveLayer object, within the LayeredImageModel. 
/-----------------/
ToggleLayer class:

Implements the ImageProcessingCommand Interface. Runs when the user inputs “toggle”. Toggles the 

visibility level either to True (Visible) or False (Invisible). Toggles the specific layer within 

the LayeredImageModel represented by the layerIndex provided to the object. 
/-----------------/
SelectLayer class:

Implements the ImageProcessingCommand Interface. Runs when the user inputs “select”. Changes the 

current layer within the LayeredImageModel to be the layer selected. Layer chosen is specified by 

the layerIndex provided to the object. 
/-----------------/
ImportImage class :

Implements the ImageProcessingCommand Interface. Runs when the user inputs “import”. Imports a 

specific image file and adds it as a new layer to the top of the LayeredImageModel. Filename is 

provided to the object as input to specify which file should be imported. 
/-----------------/
ExportImage class: 

Implements the ImageProcessingCommand Interface. Runs when the user inputs “export”. Exports the 

topmost visible layer of the LayeredImageModel provided to the object. A String representing the 

name of the exported file is provided as input to the object. 
/-----------------/
SaveImage class :

Implements the ImageProcessingCommand Interface. Runs when the user inputs “save”. Saves all layers 

of the current file as the specified image extension type. Also produces a .txt file listing all 

layers and their visibility level (either true or false). Stores all of these files into a single 

folder with a String name specified by the input provided to the object. 
/-----------------/
LoadImage class :

Implements the ImageProcessingCommand Interface. Runs when the user inputs “load”. Loads a previously 

saved file (created with the SaveImage class) and loads all layers onto the current program. Also uses 

the properties .txt file to toggle all visibility levels accordingly, to load the save file exactly as it is. 

/***************************************************************************************************/

SimpleImageController Class

Represents a SimpleImageController that runs takes in a Readable to draw input from, a view to represent 

outputs, and a LayeredImageModel to work in. Reads in input from either the user or a file that the user 

specifies as their Readable, and performs commands onto the given LayeredImageModel based on those inputs. 

Contains one start() method that takes in no inputs, and runs the program. 


SwingController Class:

Represents a SwingController that uses a GUI-based SwingView to display outputs, and a LayeredImageModel 

to work in. Uses multiple one-time controllers to initiate each command after a button press. 


/***************************************************************************************************/


/***************************************************************************************************/

Extra Credit: 


Documents both the Mosaic and the Image Downscaling extra credit processes, as well as the changes required 
to implement these new features

/-----------------/
Image Downscaling:

We chose to treat downscaling as a filter, and thus added a new function object that implements the ImageFilter

interface called ResizeFilter. Inside this function object was an apply() function that handled the downsizing.
 
In order to allow the controller to downscale an image, we had to add an additional function object ResizeLayer

that implements the ImageProcessingCommand interface. This involved creating a separate class (separate from 

the model and controller) and then adding that class into the SimpleImageController's 

Hashmap<String, Function<Scanner, ImageProcessingCommand>>.

ResizeLayer merely creates a new ResizeFilter object and applies it onto every layer of the model. 
/-----------------/
Mosaicing: 

We chose to treat mosaicing as a filter, and thus added a new function object that implements the ImageFilter

interface called MosaicFilter. Inside this function object was an apply() function that handled the mosaicing.

In order to allow the controller to mosaic an image, we had to add an additional function object MosaicImage

that implements the ImageProcessingCommand interface. This involved creating a separate class (separate from 

the model and controller) and then adding that class into the SimpleImageController's 

Hashmap<String, Function<Scanner, ImageProcessingCommand>>.
/-----------------/

/***************************************************************************************************/

Image Citations:

ex\_img1.ppm, ex\_img2.ppm are the work of Gavin White. All filtered variants were produced by

our program. TestImage.ppm and TestImage2.ppm were created by Gavin White and Hyoboem

Kim. test1.jpg, test2.jpg are the work of Gavin White and all filtered variants were produced by

our program.