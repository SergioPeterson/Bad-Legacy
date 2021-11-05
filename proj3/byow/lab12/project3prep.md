# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer:
For tessellating hexagons to place hexagons on screen in an algorithmic way I used what I knew about hexagons to make it look good.

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer:
Like with the hexagons we are randomly put a type we also randomly get a size and position in the board.

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab, and the process used to eventually get to tessellating hexagons.**

Answer:
A room class, a method to make a room, a method to make a hallway, a method to connect two rooms with
a hallway, and a method that does both

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer:
A hallway is a room with a height of 3 and a width of the length of the corridor. 
