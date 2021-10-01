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
ObstacleFactory or TerrainFactory, which have been edited to support
random object generation.

Files follow a simple naming convention. The prefix describes in
words what the file is, followed by the dimensions of the file and
the unique id for that combination.

The file `floor_plan_testing.json` is used for development tests. 
This file will be disabled on release.