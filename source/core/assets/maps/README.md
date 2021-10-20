# Customisable Random Map Generation

```
Customisable Random Map Generation Ver. 2

Created by Jaleel "Jantoom" Abdur-Raheem
```

Random generation is customisable in the sense that floor plans and 
room interiors can be prefabricated. The algorithm will pick from 
these JSON files at random.

There are three levels of random abstraction. A Home may pick a
floor plan at random, which may pick room interiors at random for
each of its rooms. GridObjects in these files may reference the
ObjectFactory or TerrainFactory, which have been edited to support
random object generation.

Files follow a simple naming convention. The prefix describes in
words what the file is, followed by the dimensions of the file and
the unique id for that combination. This convention is for 
human-readability only, because the algorithm uses the lengths of the 
grids to determine the actual dimensions.

The file `floor_plan_testing.json` is used for development tests. 
This file will be disabled on release. This will be done by setting the
variable `useTestingFloorPlan` to `false` inside `MainGameScreen`.

If your IDE gives you several JSON token errors when viewing these files,
you may need to install the libGDX plugin and mark the file as a libGDX
Style JSON file. The steps to do this are below: 

1. Navigate to File -> Settings
2. Select Plugins
3. Search for "libGDX"
4. Install the plugin named "libGDX"
5. After installation, right-click a JSON file
6. Select "Mark as libGDX Style JSON"
7. Your file should have colours and fewer errors