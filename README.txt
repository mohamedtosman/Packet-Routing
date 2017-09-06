
README

---------------------------------------------


Iteration #4
---------------------------------------------

Team members:

Aly Rasmy            alyrasmy@cmail.carleton.ca      100912450
Mohamed Osman        mohamedtosman@cmail.carleton.ca 100891567
Amin Qassoud         amin.qassoud@carleton.ca        100913328
Zhiheng Li (David)   zhiheng.li@carleton.ca          100859834

---------------------------------------------



1.  Changes:
---------------------------------------------

The fourth iterations builds on the first, second, and third milestones. Changes include:
	-Implementing Undo function while in Step mode
	-The ability to Save and Load a topology to be used for another simulation (**Was implemented last milestone)
	-Cleaning up code	

2. Documentation
---------------------------------------------

The documentation is generated in "doc" folder. 


3. Libraries needed to run the simulation and unit test
---------------------------------------------

- JUnit 4
- miglayout-4.0.jar
- jackson-annotations-2.8.4.jar
- jackson-core-2.8.4.jar
- jackson-databind-2.8.4.jar


4.  The Known Issues
---------------------------------------------

No known issues currently exist with the current build.


5.  The Roadmap (Leftover of project)
---------------------------------------------


6.  Set up instructions
---------------------------------------------

1 - Run "DemoApp.java"
2 - A GUI will be displayed where the simulation will be executed
3 - Click "Topology" -> "Create"
4 - 4 buttons will be added to the view. You will have the option to Add Node,
    Edit Links, Move Nodes, Start Over
  - Click "Add Node" then click anywhere to draw a node.
     Repeat, depending on how many nodes you would like in your topology
  - Click "Move Nodes" then click and drag any node you would if the original
     placement is not suitable
  - Click "Edit Links" then double click a node and click a second node to connect
    them
  - If you are not satisfied with the current topology, you can click
    "Start Over" and repeat the above process
  - Click "Submit" after drawing a topology to continue
6 - A user has the option to "Save" and "Open" a saved topology by simply
    clicking "Topology" -> "Save" then enter for example "test.json"
    to save a topology after creating it. The json file will be saved in /bin.
    You can also do "Topology" -> "Open" to load an exisiting saved topology.
7 - Click "Simulation" -> "Start"
  - A window will pop up asking the user to input "Message" to be sent,
    "Number of Messages", "Rate in seconds" and "Algorithm" of choice. 
    Hit "Enter" after filling all fields. A pop up window will appear
    notifying user of the Source and Destination nodes
8 - The user now has two options:
	- Click "Run" and packet simulation will start and end without 
	user intervention
	- Click "Step" and a packet will transmit from current node to
	next node. User has to continue clicking "Step" until the packet
	reaches its destination
	- Click "Undo" and packet will movement will be reversed.
9 - After the packet reaches its destination, a "Result" pop up will appear
    notifying the user of the algorithm that was used, the total number
    of packets transmitted, and the average number of hops each message goes
    through
10 - You can now either exit the program or click "Simulation" -> "Clear" to
    start another simulation and repeat steps 5 - 10 again

