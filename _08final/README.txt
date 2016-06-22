How to Run the Game
-------------------
* All files are under the 'project_model' package.
* Wnterbells.java contains the main() method and should be compiled and run in IntelliJ.

Outside Sources
---------------
* The following functions/classes in the WinterBellsPanel.java class were taken from this website:
Source: http://www.programcreek.com/2013/03/java-sort-map-by-value/
	> sortMapByValue(...)
	> class Value Comparator
* What these two do is take in a hashmap and sorts it by value. Using the class ValueComparator,
it outputs the result as a TreeMap. I can use this TreeMap to look at the count of various things picked up.

Syntax
------
* I had a different game idea initially and thus the names of the classes could be confusing.
	WinterBells 		= Main() method, frame, adds the panel and the background
	WinterBellsPanel 	= Panel containing all the game specifics and rules, as well as addition of sprites to the screen.
	Cat 				= Player
	Bird 				= Skulls
	Fish 				= Things to Pick up
	Missile 			= Player's missiles
	Sprite 				= Higher class that describes sprite mechanics and gets image, sets visibility, etc.
	Sound				= Taken from Instructor Adam's game, responsible for all sound (looped and non-looped)

Future Improvements I Could Make
---------------------------------
* Make my own background gif
* Make more endings
* Incorporate how many skulls killed in the endings
* Allow player to pick between characters (I did have this initially but wanted to keep images consistent so took this out)

IMPORTANT NOTES
---------------
* I tried to move things into their own packages, but for some reason, I kept getting errors.
So I have two folders: one for the model (sprites) and another for the sounds. The other ones (the frame and the panel
and all the PNG files associated with these two) are not in a folder because I received a compilation error when I did.
I'm sure it's easily fixable but since it's due soon, I left it as is. This is something I should/could improve with more time.
* When I press 'T' in the beginning, when the game first starts, it works as expected.
There is a NullPointerException for whatever reason, but I couldn't figure out why and how to fix it.
However, the game is not affected at all.
